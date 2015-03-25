package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.IPublicAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.IUser;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.InternalConnectionException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public class OraclePublicAccessor implements IPublicAccessor {

    private static final Logger log = Logger.getLogger(OraclePublicAccessor.class);

    private final PreparedStatement fMostPopularGameSQL;
    private final PreparedStatement fMostExpensiveGenreSQL;
    private final PreparedStatement fLeastExpensiveGenreSQL;
    private final PreparedStatement fGamesOwnedByAllSQL;

    private static OraclePublicAccessor fInstance;

    private OraclePublicAccessor() {
        try {
            
            fMostPopularGameSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "SELECT " +Tables.GAME_ATTR_NAME+ " FROM "  +Tables.OWNS_GAME_TABLENAME+ " GROUP BY " +Tables.GAME_ATTR_NAME
                    + " HAVING COUNT(*) >= ALL (SELECT COUNT(*) FROM " +Tables.OWNS_GAME_TABLENAME+ " GROUP BY " +Tables.GAME_ATTR_NAME+ ")");
            
            fMostExpensiveGenreSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "SELECT " +Tables.GAME_ATTR_GENRE+ " FROM "  +Tables.FINALIZED_GAME_TABLENAME+ " GROUP BY " +Tables.GAME_ATTR_GENRE
                    + " HAVING AVG("+Tables.FINALIZED_GAME_ATTR_FULLPRICE+") >= ALL "
                    + " (SELECT AVG(" +Tables.FINALIZED_GAME_ATTR_FULLPRICE+ ") FROM " +Tables.FINALIZED_GAME_TABLENAME+ " GROUP BY " +Tables.GAME_ATTR_GENRE+ ")");
            
            fLeastExpensiveGenreSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "SELECT " +Tables.GAME_ATTR_GENRE+ " FROM "  +Tables.FINALIZED_GAME_TABLENAME+ " GROUP BY " +Tables.GAME_ATTR_GENRE
                    + " HAVING AVG("+Tables.FINALIZED_GAME_ATTR_FULLPRICE+") <= ALL "
                    + " (SELECT AVG(" +Tables.FINALIZED_GAME_ATTR_FULLPRICE+ ") FROM " +Tables.FINALIZED_GAME_TABLENAME+ " GROUP BY " +Tables.GAME_ATTR_GENRE+ ")");
            
            fGamesOwnedByAllSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement("SELECT " +Tables.GAME_ATTR_NAME
                    + " FROM " +Tables.OWNS_GAME_TABLENAME
                    + " WHERE " +Tables.USER_ATTR_USERID+ " IN ( SELECT " +Tables.USER_ATTR_USERID+ " FROM " +Tables.CUSTOMER_TABLENAME+ " )"
                    + " GROUP BY " +Tables.GAME_ATTR_NAME
                    + " HAVING COUNT(*) ="
                    + " ( SELECT COUNT (*) FROM " +Tables.CUSTOMER_TABLENAME+ " )");
            
        } catch (SQLException e) {
            log.error("Could not prepare statements.", e);
            throw new InternalConnectionException("Could not prepare statements.", e);
        }
    }

    public static OraclePublicAccessor getInstance() {
        if (fInstance == null) {
            fInstance = new OraclePublicAccessor();
        } 
        return fInstance;
    }

    public List<FinalizedGame> listPurchasableGames() {
        return listPurchasableGames(null, null, null, null, null, null, null, false);
    }

    public List<FinalizedGame> listPurchasableGames(
            String matchName, Genre matchGenre, String matchDeveloper, 
            Float matchLowestPrice, Float matchHighestPrice, 
            GameSortByOption sortByOption, SortDirection sortDirection, 
            boolean listOnlyDiscountedGames) {

        ResultSet results = GameQueriesHelper.queryGames(
        		Tables.FINALIZED_GAME_TABLENAME, matchName, matchGenre, 
        		matchDeveloper, matchLowestPrice, matchHighestPrice, sortByOption, 
        		sortDirection, listOnlyDiscountedGames, null);

        List<FinalizedGame> games = new ArrayList<FinalizedGame>();

        try {
            while (results.next()) {
                games.add(new FinalizedGame(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION), 
                        Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE)), 
                        results.getString(Tables.GAME_ATTR_DEVELOPER), 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_RATING), 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_FULLPRICE),
                        results.getInt(7) == 1,
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC)));
            } 
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException(e);
        }

        return games;
    }

    public List<Playtime> listGamesOwned(int gameOwnerId) throws UserNotExistsException {

        return listGamesOwned(gameOwnerId, null, null, null, null, null);
    }

    public List<Playtime> listGamesOwned(String gameOwnerEmail) throws UserNotExistsException {

        return listGamesOwned(gameOwnerEmail, null, null, null, null, null);
    }

    public List<Playtime> listGamesOwned(
            int gameOwnerId, 
            String matchName, Genre matchGenre, String matchDeveloper,
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException {

        IUser user = QueriesHelper.retrieveUser(gameOwnerId, Tables.CUSTOMER_TABLENAME);
        Customer gamer = new Customer(user.getUserId(), user.getEmail(), user.getPassword());

        ResultSet results = GameQueriesHelper.queryGames(Tables.FINALIZED_GAME_TABLENAME, matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, gameOwnerId);
        return readQueryResultsForGamesOwned(gamer, results);
    }

    public List<Playtime> listGamesOwned(
            String gameOwnerEmail, 
            String matchName, Genre matchGenre, String matchDeveloper, 
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException {

        IUser user = QueriesHelper.retrieveUser(gameOwnerEmail, Tables.CUSTOMER_TABLENAME);
        Customer gamer = new Customer(user.getUserId(), user.getEmail(), user.getPassword());

        ResultSet results = GameQueriesHelper.queryGames(Tables.FINALIZED_GAME_TABLENAME, matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, gamer.getUserId());
        return readQueryResultsForGamesOwned(gamer, results);
    }

    private static List<Playtime> readQueryResultsForGamesOwned(Customer gamer, ResultSet results) {

        List<Playtime> playtimes = new ArrayList<Playtime>();

        try {
            while (results.next()) {

                FinalizedGame game = new FinalizedGame(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION), 
                        Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE)), 
                        results.getString(Tables.GAME_ATTR_DEVELOPER), 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_RATING), 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_FULLPRICE),
                        results.getInt(7) == 1,
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC));

                Float hours = results.getFloat(Tables.OWNS_GAME_ATTR_HOURS);

                playtimes.add(new Playtime(gamer, game, hours));

            }
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException(e);
        }

        return playtimes;

    }

    @Override
    public Collection<FinalizedGame> findMostPopularGame() {
        try {
            ResultSet results = fMostPopularGameSQL.executeQuery();

            Collection<FinalizedGame> popularGames = new HashSet<FinalizedGame>();

            while (results.next()) {
                popularGames.add(Retrieves.retrieveFinalizedGame(results.getString(Tables.GAME_ATTR_NAME)));
            }

            return popularGames;
        } catch (SQLException e) {
            throw new InternalConnectionException("Failed to find most popular game.", e);
        } catch (GameNotExistException e) {
            throw new RuntimeException("This shouldn't happen.");
        }
    }

    @Override
    public Genre findMostExpensiveGenre() {
        try {
            ResultSet results = fMostExpensiveGenreSQL.executeQuery();

            if (results.next()) {
                return Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE));
            } else {
                throw new GameNotExistException("No games in database?");
            }
        } catch (SQLException e) {
            throw new InternalConnectionException("Failed to find most expensive genre.", e);
        } catch (GameNotExistException e) {
            throw new RuntimeException("This shouldn't happen.");
        }
    }

    @Override
    public Genre findLeastExpensiveGenre() {
        try {
            ResultSet results = fLeastExpensiveGenreSQL.executeQuery();

            if (results.next()) {
                Genre ret = Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE));

                if (results.next()) {
                    throw new RuntimeException("Shoot.");
                }

                return ret;
            } else {
                throw new GameNotExistException("No games in database?");
            }
        } catch (SQLException e) {
            throw new InternalConnectionException("Failed to find least expensive genre.", e);
        } catch (GameNotExistException e) {
            throw new RuntimeException("This shouldn't happen.");
        }
    }

    @Override
    public Collection<FinalizedGame> findGamesOwnedByAllCustomers() {
        
        try {
            ResultSet results = fGamesOwnedByAllSQL.executeQuery();
            
            Collection<FinalizedGame> games = new HashSet<FinalizedGame>();
            
            while (results.next()) {
                games.add(Retrieves.retrieveFinalizedGame(results.getString(Tables.GAME_ATTR_NAME)));
            }
            
            return games;
            
        } catch (SQLException e) {
            throw new InternalConnectionException("Failed to execute query.", e);
        } catch (GameNotExistException e) {
            throw new IllegalStateException("Shouldn't happen.", e);
        }
        
    }

}

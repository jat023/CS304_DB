package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.IPublicAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.IUser;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public final class OraclePublicAccessor implements IPublicAccessor {

    private static final Logger log = Logger.getLogger(OraclePublicAccessor.class);

    private static OraclePublicAccessor fInstance;

    private OraclePublicAccessor() {};

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
            String matchName, String matchGenre, String matchDeveloper, 
            Float matchLowestPrice, Float matchHighestPrice, 
            GameSortByOption sortByOption, SortDirection sortDirection, 
            boolean listOnlyDiscountedGames) {

        ResultSet results = GameQueriesHelper.queryGames(Tables.FINALIZED_GAME_TABLENAME, matchName, matchGenre, matchDeveloper, matchLowestPrice, matchHighestPrice, sortByOption, sortDirection, listOnlyDiscountedGames, null);

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
            String matchName, String matchGenre, String matchDeveloper,
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException {

        IUser user = QueriesHelper.retrieveUser(gameOwnerId, Tables.CUSTOMER_TABLENAME);
        Customer gamer = new Customer(user.getUserId(), user.getEmail(), user.getPassword());

        ResultSet results = GameQueriesHelper.queryGames(Tables.FINALIZED_GAME_TABLENAME, matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, gameOwnerId);
        return readQueryResultsForGamesOwned(gamer, results);
    }

    public List<Playtime> listGamesOwned(
            String gameOwnerEmail, 
            String matchName, String matchGenre, String matchDeveloper, 
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
}

package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.access.service.IPublicAccessor;
import ca.ubc.cs.cs304.steemproject.access.service.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.service.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;

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

        ResultSet results = GameQueriesHelper.queryGames(Tables.FINALIZED_GAME_TABLENAME, matchName, matchGenre, matchDeveloper, matchLowestPrice, matchHighestPrice, sortByOption, sortDirection, listOnlyDiscountedGames, null, null);

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

    public Map<FinalizedGame, Float> listGamesOwned(int gameOwnerId) throws UserNotExistsException {

        return listGamesOwned(gameOwnerId, null, null, null, null, null);
    }

    public Map<FinalizedGame, Float> listGamesOwned(String gameOwnerEmail) throws UserNotExistsException {

        return listGamesOwned(gameOwnerEmail, null, null, null, null, null);
    }

    public Map<FinalizedGame, Float> listGamesOwned(
            int gameOwnerId, 
            String matchName, String matchGenre, String matchDeveloper,
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException {

        if (!QueriesHelper.userExists(gameOwnerId, Tables.CUSTOMER_TABLENAME)) {
            throw new UserNotExistsException();
        }

        ResultSet results = GameQueriesHelper.queryGames(Tables.FINALIZED_GAME_TABLENAME, matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, gameOwnerId, null);
        return readQueryResultsForGamesOwned(results);
    }

    public Map<FinalizedGame, Float> listGamesOwned(
            String gameOwnerEmail, 
            String matchName, String matchGenre, String matchDeveloper, 
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException {

        if (!QueriesHelper.userExists(gameOwnerEmail, Tables.CUSTOMER_TABLENAME)) {
            throw new UserNotExistsException();
        }

        ResultSet results = GameQueriesHelper.queryGames(Tables.FINALIZED_GAME_TABLENAME, matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, null, gameOwnerEmail);
        return readQueryResultsForGamesOwned(results);
    }

    private static Map<FinalizedGame, Float> readQueryResultsForGamesOwned(ResultSet results) {

        Map<FinalizedGame, Float> gameAndTimeSpent = new HashMap<FinalizedGame, Float>();

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

                gameAndTimeSpent.put(game,hours);

            }
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException(e);
        }

        return gameAndTimeSpent;

    }
}

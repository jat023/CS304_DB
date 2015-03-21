package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.access.game.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.access.service.IPublicAccessor;
import ca.ubc.cs.cs304.steemproject.access.service.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.service.options.SortDirection;

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

        ResultSet results = queryGames(matchName, matchGenre, matchDeveloper, matchLowestPrice, matchHighestPrice, sortByOption, sortDirection, listOnlyDiscountedGames, null, null);

        List<FinalizedGame> games = new ArrayList<FinalizedGame>();

        try {
            while (results.next()) {
                games.add(new FinalizedGame(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION), 
                        results.getString(Tables.GAME_ATTR_GENRE), 
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

        if (!QueryHelper.userExists(gameOwnerId, Tables.CUSTOMER_TABLENAME)) {
            throw new UserNotExistsException();
        }

        ResultSet results = queryGames(matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, gameOwnerId, null);
        return readQueryResultsForGamesOwned(results);
    }

    public Map<FinalizedGame, Float> listGamesOwned(
            String gameOwnerEmail, 
            String matchName, String matchGenre, String matchDeveloper, 
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException {

        if (!QueryHelper.userExists(gameOwnerEmail, Tables.CUSTOMER_TABLENAME)) {
            throw new UserNotExistsException();
        }

        ResultSet results = queryGames(matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, null, gameOwnerEmail);
        return readQueryResultsForGamesOwned(results);
    }

    private static Map<FinalizedGame, Float> readQueryResultsForGamesOwned(ResultSet results) {

        Map<FinalizedGame, Float> gameAndTimeSpent = new HashMap<FinalizedGame, Float>();

        try {
            while (results.next()) {

                FinalizedGame game = new FinalizedGame(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION), 
                        results.getString(Tables.GAME_ATTR_GENRE), 
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

    private static ResultSet queryGames(
            String matchName, String matchGenre, String matchDeveloper, Float matchLowestPrice, Float matchHighestPrice, 
            GameSortByOption sortByOption, SortDirection sortDirection,
            boolean onlyDiscountedGames, Integer ownerId, String ownerEmail) {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + Tables.FINALIZED_GAME_TABLENAME);

        if (ownerId != null || ownerEmail != null) {
            queryBuilder.append(" NATURAL JOIN " + Tables.OWNS_GAME_TABLENAME);
        }

        if (matchName != null || matchGenre != null || matchDeveloper != null || matchLowestPrice != null || matchHighestPrice != null || onlyDiscountedGames) {

            queryBuilder.append(" WHERE ");

            Collection<String> constraints = new ArrayList<String>();

            if (matchName != null) {
                constraints.add("UPPER(" +Tables.GAME_ATTR_NAME+ ") LIKE '%" +matchName.toUpperCase()+ "%'");
            }

            if (matchGenre != null) {
                constraints.add(Tables.GAME_ATTR_GENRE+ "='" + matchGenre + "'");
            }

            if (matchDeveloper != null) {
                constraints.add(Tables.GAME_ATTR_DEVELOPER+ "='" + matchDeveloper + "'");
            }

            if (matchLowestPrice != null) {
                constraints.add(salePriceFormula()+ ">=" + matchLowestPrice);
            }

            if (matchHighestPrice != null) {
                constraints.add(salePriceFormula()+ "<=" + matchHighestPrice);
            }

            if (onlyDiscountedGames) {
                constraints.add(Tables.FINALIZED_GAME_ATTR_ONSPECIAL+ "=1");
            }

            if (ownerId != null) {
                constraints.add(Tables.USER_ATTR_USERID+ "=" + ownerId);
            } else if (ownerEmail != null) {
                constraints.add(Tables.USER_ATTR_EMAIL+ "=" + ownerEmail);
            }

            queryBuilder.append(StringUtils.join(constraints, " AND "));
        }

        appendSorting(sortByOption, sortDirection, queryBuilder);

        String query = queryBuilder.toString();

        log.debug("Executing query: " + query);
        return QueryHelper.runQuery(query);
    }

    private static void appendSorting(GameSortByOption sortByOption, SortDirection sortDirection, StringBuilder queryBuilder) {
        if (sortByOption != null) {

            String sortParam = null;

            switch (sortByOption) {
            case NAME:
                sortParam = Tables.GAME_ATTR_NAME;
                break;
            case GENRE:
                sortParam = Tables.GAME_ATTR_GENRE;
                break;
            case DEVELOPER:
                sortParam = Tables.GAME_ATTR_DEVELOPER;
                break;
            case SALEPRICE:
                sortParam = salePriceFormula();
                break;
            default:
                break;
            }

            queryBuilder.append(" ORDER BY " + sortParam + " " + (sortDirection == null ? SortDirection.ASC : sortDirection));
        }
    }

    private static String salePriceFormula() {
        return Tables.FINALIZED_GAME_ATTR_FULLPRICE + "* ( 1-" + Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC + ")";
    }
}

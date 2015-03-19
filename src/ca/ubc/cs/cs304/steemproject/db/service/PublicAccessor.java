package ca.ubc.cs.cs304.steemproject.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.db.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.db.service.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.db.service.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.game.PurchasableGame;

public final class PublicAccessor {

    private static final Logger log = Logger.getLogger(PublicAccessor.class);

    private PublicAccessor() {};

    public static List<PurchasableGame> listPurchasableGames() {
        return listPurchasableGames(null, null, null, null, null);
    }

    public static List<PurchasableGame> listPurchasableGames(
            String matchName, String matchGenre, String matchDeveloper, 
            Float matchLowestPrice, Float matchHighestPrice) {
        
        return listPurchasableGames(matchName, matchGenre, matchDeveloper, matchLowestPrice, matchHighestPrice, null, null, false);
    }

    public static List<PurchasableGame> listPurchasableGames(
            String matchName, String matchGenre, String matchDeveloper, 
            Float matchLowestPrice, Float matchHighestPrice, 
            GameSortByOption sortByOption, SortDirection sortDirection, 
            boolean listOnlyDiscountedGames) {

        ResultSet results = queryGames(matchName, matchGenre, matchDeveloper, matchLowestPrice, matchHighestPrice, sortByOption, sortDirection, listOnlyDiscountedGames, null, null);

        List<PurchasableGame> games = new ArrayList<PurchasableGame>();

        try {
            while (results.next()) {
                games.add(new PurchasableGame(
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

    public static Map<PurchasableGame, Float> listGamesOwned(int gameOwnerId) throws UserNotExistsException {
        
        return listGamesOwned(gameOwnerId, null, null, null, null, null);
    }
    
    public static Map<PurchasableGame, Float> listGamesOwned(String gameOwnerEmail) throws UserNotExistsException {
        
        return listGamesOwned(gameOwnerEmail, null, null, null, null, null);
    }
    
    public static Map<PurchasableGame, Float> listGamesOwned(
            int gameOwnerId, 
            String matchName, String matchGenre, String matchDeveloper,
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException {

        if (!DbQueryHelper.customerExists(gameOwnerId)) {
            throw new UserNotExistsException();
        }
        
        ResultSet results = queryGames(matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, gameOwnerId, null);
        return readQueryResultsForGamesOwned(results);
    }

    public static Map<PurchasableGame, Float> listGamesOwned(
            String gameOwnerEmail, 
            String matchName, String matchGenre, String matchDeveloper, 
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException {
        
        if (!DbQueryHelper.customerExists(gameOwnerEmail)) {
            throw new UserNotExistsException();
        }
        
        ResultSet results = queryGames(matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, null, gameOwnerEmail);
        return readQueryResultsForGamesOwned(results);
    }
    
    private static Map<PurchasableGame, Float> readQueryResultsForGamesOwned(ResultSet results) {
        
        Map<PurchasableGame, Float> gameAndTimeSpent = new HashMap<PurchasableGame, Float>();
        
        try {
            while (results.next()) {
                
                PurchasableGame game = new PurchasableGame(
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
                constraints.add(Tables.GAME_ATTR_NAME+ "='" + matchName + "'");
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
            if (ownerId != null || ownerEmail != null) {
                if (ownerId != null) {
                    constraints.add(Tables.USER_ATTR_USERID+ "=" + ownerId);
                } else if (ownerEmail != null) {
                    constraints.add(Tables.USER_ATTR_EMAIL+ "=" + ownerEmail);
                }
            }

            queryBuilder.append(StringUtils.join(constraints, " AND "));
        }

        appendSorting(sortByOption, sortDirection, queryBuilder);

        log.debug("Executing query:");
        log.debug(queryBuilder.toString());

        return DbQueryHelper.runQuery(queryBuilder.toString());
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

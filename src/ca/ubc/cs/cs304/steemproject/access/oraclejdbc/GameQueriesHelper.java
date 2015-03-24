package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.InternalConnectionException;

final class GameQueriesHelper {
    
    private static final Logger log = Logger.getLogger(GameQueriesHelper.class);
    
    public static ResultSet queryGames(
            String table,
            String matchName, Genre matchGenre, String matchDeveloper, Float matchLowestPrice, Float matchHighestPrice, 
            GameSortByOption sortByOption, SortDirection sortDirection,
            boolean onlyDiscountedGames, Integer ownerId) {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + table);

        if (ownerId != null) {
            queryBuilder.append(" NATURAL JOIN " + Tables.OWNS_GAME_TABLENAME);
        }

        if (matchName != null || matchGenre != null || matchDeveloper != null || matchLowestPrice != null || matchHighestPrice != null || onlyDiscountedGames || ownerId != null) {

            queryBuilder.append(" WHERE ");

            Collection<String> constraints = new ArrayList<String>();

            if (matchName != null) {
                constraints.add("UPPER(" +Tables.GAME_ATTR_NAME+ ") LIKE '%" +matchName.toUpperCase()+ "%'");
            }

            if (matchGenre != null) {
                constraints.add(Tables.GAME_ATTR_GENRE+ "='" + matchGenre + "'");
            }

            if (matchDeveloper != null) {
                constraints.add("UPPER(" +Tables.GAME_ATTR_DEVELOPER+ ")='" + matchDeveloper.toUpperCase() + "'");
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
            }

            queryBuilder.append(StringUtils.join(constraints, " AND "));
        }

        appendSorting(sortByOption, sortDirection, queryBuilder);

        String query = queryBuilder.toString();

        log.debug("Executing query: " + query);
        return QueriesHelper.runQuery(query);
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
    
    public static boolean gameExists(String exactName, String table) {
        String userEmailExistsQuery = "SELECT * FROM " + Tables.CUSTOMER_TABLENAME + " WHERE " + Tables.GAME_ATTR_NAME + "='" + exactName + "'";
        return QueriesHelper.exists(userEmailExistsQuery);
    }
    
    public static FinalizedGame retrieveFinalizedGame(String nameOfGame) throws GameNotExistException {

        ResultSet results;
        
        try {
            String query = "SELECT * FROM " +Tables.FINALIZED_GAME_TABLENAME+ " WHERE " + Tables.GAME_ATTR_NAME+ "='" +nameOfGame+ "'";
            results = SteemOracleDbConnector.getDefaultConnection().createStatement().executeQuery(query);
        } catch (SQLException e) {
            log.error("Could not execute query.", e);
            throw new InternalConnectionException("Could not execute query.", e);
        }

        try {
            if (results.next()) {

                return new FinalizedGame(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION),
                        Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE)), 
                        results.getString(Tables.GAME_ATTR_DEVELOPER), 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_RATING), 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_FULLPRICE), 
                        results.getInt(Tables.FINALIZED_GAME_ATTR_ONSPECIAL)==1, 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC));


            } else {
                throw new GameNotExistException(nameOfGame);
            }
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException("Could not read results.", e);
        }
    }
}

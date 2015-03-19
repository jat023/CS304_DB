package ca.ubc.cs.cs304.steemproject.db.access.general;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.db.Tables;
import ca.ubc.cs.cs304.steemproject.db.access.DbQueryHelper;
import ca.ubc.cs.cs304.steemproject.game.PurchasableGame;

public final class PublicAccessor {

    private static final Logger log = Logger.getLogger(PublicAccessor.class);

    private PublicAccessor() {};

    public static List<PurchasableGame> listPurchasableGames() {
        return listPurchasableGames(null, null, null, null, null);
    }

    public static List<PurchasableGame> listPurchasableGames(String matchName, String matchGenre, String matchDeveloper, Float matchLowestPrice, Float matchHighestPrice) {
        return listPurchasableGames(matchName, matchGenre, matchDeveloper, matchLowestPrice, matchHighestPrice, null, null, false);
    }
    
    public static List<PurchasableGame> listPurchasableGames(String matchName, String matchGenre, String matchDeveloper, Float matchLowestPrice, Float matchHighestPrice, boolean listOnlyDiscountedGames) {
        return listPurchasableGames(matchName, matchGenre, matchDeveloper, matchLowestPrice, matchHighestPrice, null, null, listOnlyDiscountedGames);
    }

    public static List<PurchasableGame> listPurchasableGames(
            String matchName, String matchGenre, String matchDeveloper, Float matchLowestPrice, Float matchHighestPrice, 
            GameSortByOption sortByOption, SortDirection sortDirection,
            boolean listOnlyDiscountedGames) {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + Tables.PURCHASABLE_GAME_TABLENAME);

        if (matchName != null || matchGenre != null || matchDeveloper != null || matchLowestPrice != null || matchHighestPrice != null || listOnlyDiscountedGames) {
            
            queryBuilder.append(" WHERE ");

            Collection<String> constraints = new ArrayList<String>();

            if (matchName != null) {
                constraints.add("gname = '" + matchName + "'");
            }

            if (matchGenre != null) {
                constraints.add("genre = '" + matchGenre + "'");
            }

            if (matchDeveloper != null) {
                constraints.add("developer = '" + matchDeveloper + "'");
            }

            if (matchLowestPrice != null) {
                constraints.add("price >= " + matchLowestPrice);
            }

            if (matchHighestPrice != null) {
                constraints.add("price <= " + matchHighestPrice);
            }
            
            if (listOnlyDiscountedGames) {
                constraints.add("onSpecial = 1");
            }

            queryBuilder.append(StringUtils.join(constraints, " AND "));
        }

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
            case PRICE:
                sortParam = Tables.PURCHASABLE_GAME_ATTR_FULLPRICE + "* ( 1-" + Tables.PURCHASABLE_GAME_ATTR_DISCOUNTPERC + ")" ;
                break;
            default:
                break;
            }

            queryBuilder.append(" ORDER BY " + sortParam + " " + (sortDirection == null ? SortDirection.ASC : sortDirection));
        }

        System.out.println(queryBuilder.toString());

        ResultSet results = DbQueryHelper.runQuery(queryBuilder.toString());

        List<PurchasableGame> games = new ArrayList<PurchasableGame>();

        try {
            while (results.next()) {
                games.add(new PurchasableGame(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION), 
                        results.getString(Tables.GAME_ATTR_GENRE), 
                        results.getString(Tables.GAME_ATTR_DEVELOPER), 
                        results.getFloat(Tables.PURCHASABLE_GAME_ATTR_RATING), 
                        results.getFloat(Tables.PURCHASABLE_GAME_ATTR_FULLPRICE),
                        results.getInt(7) == 1,
                        results.getFloat(Tables.PURCHASABLE_GAME_ATTR_DISCOUNTPERC)));
            } 
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException(e);
        }

        return games;
    }

    public static void main(String[] args) {
        for (PurchasableGame game : listPurchasableGames(null, null, null, null, null, GameSortByOption.PRICE, SortDirection.DESC, false) ) {
            System.out.println(game);
            System.out.println(game.getSalePrice());
        }
    }
}

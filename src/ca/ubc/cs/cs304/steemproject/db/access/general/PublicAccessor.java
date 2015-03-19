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

        StringBuilder queryBuilder = new StringBuilder();
        
        queryBuilder.append("SELECT * FROM " + Tables.PURCHASABLE_GAME_TABLENAME);
        
        if (matchName != null || matchGenre != null || matchDeveloper != null || matchLowestPrice != null || matchHighestPrice != null) {
            
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
            
            queryBuilder.append(StringUtils.join(constraints, " AND "));
        }
        
        System.out.println(queryBuilder.toString());
        
        ResultSet results = DbQueryHelper.runQuery(queryBuilder.toString());

        List<PurchasableGame> games = new ArrayList<PurchasableGame>();

        try {
            while (results.next()) {
                games.add(new PurchasableGame(
                        results.getString(1), 
                        results.getString(2), 
                        results.getString(3), 
                        results.getString(4), 
                        results.getFloat(5), 
                        results.getFloat(6),
                        results.getInt(7) == 1,
                        results.getFloat(8)));
            } 
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException(e);
        }

        return games;
    }
}

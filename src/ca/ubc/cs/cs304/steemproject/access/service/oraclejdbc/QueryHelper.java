package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.game.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection.SteemOracleDbConnector;


final class QueryHelper {

    private static final Logger log = Logger.getLogger(QueryHelper.class);

    private QueryHelper() {};

    public static ResultSet runQuery(String query) {

        try {
            Statement stmt = SteemOracleDbConnector.getDefaultConnection().createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            log.error("Failed to execute query: \n" + query, e);
            throw new RuntimeException("Failed to execute query: \n" + query, e);
        }
    }

    public static boolean userExists(String userEmail, String table) {
        String userEmailExistsQuery = "SELECT * FROM " + Tables.CUSTOMER_TABLENAME + " WHERE " + Tables.USER_ATTR_EMAIL + "=" + userEmail;
        return exists(userEmailExistsQuery);
    }

    public static boolean userExists(int userId, String table) {
        String userEmailExistsQuery = "SELECT * FROM " + Tables.CUSTOMER_TABLENAME + " WHERE " + Tables.USER_ATTR_USERID + "=" + userId;
        return exists(userEmailExistsQuery);
    }
    
    public static boolean exists(String query) {
        try {
            Statement stmt = SteemOracleDbConnector.getDefaultConnection().createStatement();
            ResultSet results = stmt.executeQuery(query);

            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            log.error("Could not execute query: " +query, e);
            throw new RuntimeException("Could not execute query: " +query, e);
        }
    }
    
    public static void insertNewPurchasableGame(FinalizedGame aPurchasableGame) throws SQLException {
        String insertFinalizedGameSQL = "INSERT INTO " + Tables.FINALIZED_GAME_TABLENAME
                + "("
                + Tables.GAME_ATTR_NAME+ ","
                + Tables.GAME_ATTR_DESCRIPTION+ ","
                + Tables.GAME_ATTR_GENRE+ ","
                + Tables.GAME_ATTR_DEVELOPER+ ","
                + Tables.FINALIZED_GAME_ATTR_RATING+ ","
                + Tables.FINALIZED_GAME_ATTR_FULLPRICE+ ","
                + Tables.FINALIZED_GAME_ATTR_ONSPECIAL+ ","
                + Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC
                + ") VALUES "
                + "(?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement(insertFinalizedGameSQL);
        preparedStatement.setString(1, aPurchasableGame.getName());
        preparedStatement.setString(2, aPurchasableGame.getDescription());
        preparedStatement.setString(3, aPurchasableGame.getGenre());
        preparedStatement.setString(4, aPurchasableGame.getPublisher());
        preparedStatement.setFloat(5, aPurchasableGame.getRating());
        preparedStatement.setFloat(6, aPurchasableGame.getFullPrice());
        preparedStatement.setInt(7, aPurchasableGame.isOnSpecial() ? 1 : 0);
        preparedStatement.setFloat(8, aPurchasableGame.getDiscountPercentage());
        preparedStatement.executeUpdate();
    }
}

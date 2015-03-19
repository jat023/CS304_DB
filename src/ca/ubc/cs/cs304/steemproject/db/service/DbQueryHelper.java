package ca.ubc.cs.cs304.steemproject.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.db.connection.SteemDbConnector;

final class DbQueryHelper {

    private static final Logger log = Logger.getLogger(DbQueryHelper.class);

    private DbQueryHelper() {};

    public static ResultSet runQuery(String query) {

        try {
            Statement stmt = SteemDbConnector.getDefaultConnection().createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            log.error("Failed to execute query: \n" + query, e);
            throw new RuntimeException("Failed to execute query: \n" + query, e);
        }
    }

    public static boolean customerExists(String userEmail) {
        String userEmailExistsQuery = "SELECT * FROM " + Tables.CUSTOMER_TABLENAME + " WHERE " + Tables.USER_ATTR_EMAIL + "=" + userEmail;
        return exists(userEmailExistsQuery);
    }

    public static boolean customerExists(int userId) {
        String userEmailExistsQuery = "SELECT * FROM " + Tables.CUSTOMER_TABLENAME + " WHERE " + Tables.USER_ATTR_USERID + "=" + userId;
        return exists(userEmailExistsQuery);

    }
    
    private static boolean exists(String userEmailExistsQuery) {
        try {
            Statement stmt = SteemDbConnector.getDefaultConnection().createStatement();
            ResultSet results = stmt.executeQuery(userEmailExistsQuery);

            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            log.error("Could not execute query: " +userEmailExistsQuery, e);
            throw new RuntimeException("Could not execute query: " +userEmailExistsQuery, e);
        }
    }
}

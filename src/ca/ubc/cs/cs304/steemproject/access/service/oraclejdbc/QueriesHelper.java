package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.IUser;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;


final class QueriesHelper {

    private static final Logger log = Logger.getLogger(QueriesHelper.class);

    private QueriesHelper() {};

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
        String userEmailExistsQuery = "SELECT * FROM " + table + " WHERE " + Tables.USER_ATTR_EMAIL + "=" + userEmail;
        return exists(userEmailExistsQuery);
    }

    public static boolean userExists(int userId, String table) {
        String userEmailExistsQuery = "SELECT * FROM " + table + " WHERE " + Tables.USER_ATTR_USERID + "=" + userId;
        return exists(userEmailExistsQuery);
    }
    
    public static IUser retrieveUser(String userEmail, String table) throws UserNotExistsException {
        String userEmailQuery = "SELECT * FROM " + table + " WHERE " + Tables.USER_ATTR_EMAIL + "=" + userEmail;
        return processUserQuery(userEmailQuery);
    }
    
    public static IUser retrieveUser(int userId, String table) throws UserNotExistsException {
        String userEmailQuery = "SELECT * FROM " + table + " WHERE " + Tables.USER_ATTR_USERID + "=" + userId;
        return processUserQuery(userEmailQuery);
    }

    private static IUser processUserQuery(String query)
            throws UserNotExistsException {
        try {
            Statement stmt = SteemOracleDbConnector.getDefaultConnection().createStatement();
            ResultSet results = stmt.executeQuery(query);

            if (results.next()) {
                return new Customer(
                        results.getInt(Tables.USER_ATTR_USERID), 
                        results.getString(Tables.USER_ATTR_EMAIL), 
                        results.getString(Tables.USER_ATTR_PASSWORD));
            } else {
                throw new UserNotExistsException();
            }

        } catch (SQLException e) {
            log.error("Could not execute query: " +query, e);
            throw new RuntimeException("Could not execute query: " +query, e);
        }
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
}

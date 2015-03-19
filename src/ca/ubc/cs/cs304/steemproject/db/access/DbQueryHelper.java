package ca.ubc.cs.cs304.steemproject.db.access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.db.connection.SteemDbConnector;

public final class DbQueryHelper {

    private static final Logger log = Logger.getLogger(DbQueryHelper.class);
    
    private DbQueryHelper() {};

    public static ResultSet runQuery(String query) {

        try {
            Statement statement = SteemDbConnector.getDefaultConnection().createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            log.error("Failed to execute query: \n" + query, e);
            throw new RuntimeException("Failed to execute query: \n" + query, e);
        }
    }

}

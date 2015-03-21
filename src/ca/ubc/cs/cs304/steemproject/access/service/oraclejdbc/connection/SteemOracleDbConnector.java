package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.exception.InternalConnectionException;

public class SteemOracleDbConnector {

    private final static Logger log = Logger.getLogger(SteemOracleDbConnector.class);
    
    private final static String DEFAULT_URL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
    private final static String DEFAULT_USER = "ora_j5m8";
    private final static String DEFAULT_PASSWORD = "a10798122";
    
    private static Connection defaultConnection;

    private SteemOracleDbConnector() {}

    /**
     * No multithreading support.
     * @return
     */
    public static Connection getDefaultConnection() {

        if (defaultConnection == null) {
            registerDriver();

            try {
                defaultConnection = DriverManager.getConnection(DEFAULT_URL, DEFAULT_USER, DEFAULT_PASSWORD);
                
            } catch (SQLException e) {
                log.error("Failed to establish connection to server.", e);
            }            
        }
        
        return defaultConnection;

    }

    private static void registerDriver() {

        try {

            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

        } catch (Exception e) {

            log.error("Failed to register driver.", e);
            throw new InternalConnectionException("Failed to register driver.", e);
            
        }
    }

}

package ca.ubc.cs.cs304.steemproject.db.init;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.db.Tables;
import ca.ubc.cs.cs304.steemproject.db.connection.SteemDbConnector;

public class InitializeDatabase {

    private static final Logger log = Logger.getLogger(InitializeDatabase.class);

    private static final String createCustomerSQL = "CREATE TABLE " +Tables.CUSTOMER_TABLE+ " (" +
            "userId INT," +
            "email VARCHAR(30) NOT NULL UNIQUE," +
            "password VARCHAR(30) NOT NULL," +
            "PRIMARY KEY (userId) )";

    private static final String createGameTesterSQL = "CREATE TABLE " +Tables.GAME_TESTER_TABLE+ " (" +
            "userId INT," +
            "email VARCHAR(30) NOT NULL UNIQUE," +
            "password VARCHAR(30) NOT NULL," +
            "PRIMARY KEY (userId) )";

    private static final String createFinalizedGameSQL = "CREATE TABLE " +Tables.FINALIZED_GAME_TABLE+ " (" +
            "gname VARCHAR(15)," +
            "rating NUMBER(2,1) NOT NULL," +
            "description VARCHAR(2000) NOT NULL," +
            "genre VARCHAR(15) NOT NULL," +
            "developer VARCHAR(15) NOT NULL," +
            "price NUMBER(4,2) NOT NULL," +
            "onSpecial NUMBER(1) DEFAULT 0," +
            "discount NUMBER(2,2) DEFAULT 0," +
            "PRIMARY KEY (gname)," +
            "CONSTRAINT discountConstraint CHECK (discount >= 0 AND discount <= 1)," +
            "CONSTRAINT ratingConstraint CHECK (rating >= 0 AND rating <= 10) )";

    private static final String createGameInDevelopmentSQL = "CREATE TABLE "+Tables.GAME_IN_DEVELOPMENT_TABLE+" ("+
            "gname VARCHAR(15)," +
            "description VARCHAR(2000) NOT NULL," +
            "genre VARCHAR(15) NOT NULL,"+
            "developer VARCHAR(15) NOT NULL,"+
            "version CHAR(10) NOT NULL," +
            "PRIMARY KEY (gname) )";

    private static final String createCreditCardSQL = "CREATE TABLE "+Tables.CREDIT_CARD_TABLE+" (" +
            "cardNum NUMBER(16),"+
            "address VARCHAR(30) NOT NULL,"+
            "ccv NUMBER(3) NOT NULL,"+
            "userId INT NOT NULL,"+
            "PRIMARY KEY (cardNum),"+
            "FOREIGN KEY (userId) REFERENCES " +Tables.CUSTOMER_TABLE+ " )";

    private static final String createOwnsGameSQL = "Create TABLE "+Tables.OWNS_GAME_TABLE+" (" +
            "userId INT,"+
            "gname VARCHAR(15),"+
            "hours NUMBER(2) DEFAULT 0,"+
            "PRIMARY KEY (userId, gname),"+
            "FOREIGN KEY (userId) REFERENCES " +Tables.CUSTOMER_TABLE+ ","+
            "FOREIGN KEY (gname) REFERENCES " +Tables.FINALIZED_GAME_TABLE+ " )";

    private static final String createTransactionSQL = "CREATE TABLE "+Tables.TRANSACTION_TABLE+" (" +
            "userId INT,"+
            "gname VARCHAR(15),"+
            "cardNum NUMBER(16) NOT NULL,"+
            "dateTime TIMESTAMP NOT NULL,"+
            "PRIMARY KEY (userId, gname),"+
            "FOREIGN KEY (userId, gname) REFERENCES " +Tables.OWNS_GAME_TABLE+ ","+
            "FOREIGN KEY (cardNum) REFERENCES " +Tables.CREDIT_CARD_TABLE+ " )";

    private static final String createTestSQL = "CREATE TABLE "+Tables.TEST_TABLE+" (" +
            "userId INT,"+
            "gname VARCHAR(15),"+
            "dateTime TIMESTAMP NOT NULL,"+
            "rating NUMBER(2,1) NOT NULL,"+
            "PRIMARY KEY (userId, gname, dateTime),"+
            "FOREIGN KEY (userId) REFERENCES " +Tables.GAME_TESTER_TABLE+ ","+
            "FOREIGN KEY (gname) REFERENCES " +Tables.GAME_IN_DEVELOPMENT_TABLE+ " )";

    public static void main(String args[]) throws SQLException {

        Connection con = SteemDbConnector.getDefaultConnection();

        Statement statement = con.createStatement();

        // Drop existing tables.

        dropTableIfExists(con, Tables.TEST_TABLE);
        dropTableIfExists(con, Tables.TRANSACTION_TABLE);
        dropTableIfExists(con, Tables.OWNS_GAME_TABLE);
        dropTableIfExists(con, Tables.CREDIT_CARD_TABLE);
        dropTableIfExists(con, Tables.GAME_IN_DEVELOPMENT_TABLE);
        dropTableIfExists(con, Tables.FINALIZED_GAME_TABLE);
        dropTableIfExists(con, Tables.GAME_TESTER_TABLE);
        dropTableIfExists(con, Tables.CUSTOMER_TABLE);

        // Create tables.

        statement.execute(createCustomerSQL);
        log.info("Created table " + Tables.CUSTOMER_TABLE);

        statement.execute(createGameTesterSQL);
        log.info("Created table " + Tables.GAME_TESTER_TABLE);

        statement.execute(createFinalizedGameSQL);
        log.info("Created table " + Tables.FINALIZED_GAME_TABLE);

        statement.execute(createGameInDevelopmentSQL);
        log.info("Created table " + Tables.GAME_IN_DEVELOPMENT_TABLE);

        statement.execute(createCreditCardSQL);
        log.info("Created table " + Tables.CREDIT_CARD_TABLE);

        statement.execute(createOwnsGameSQL);
        log.info("Created table " + Tables.OWNS_GAME_TABLE);

        statement.execute(createTransactionSQL);
        log.info("Created table " + Tables.TRANSACTION_TABLE);

        statement.execute(createTestSQL);
        log.info("Created table " + Tables.TEST_TABLE);

    }
    
    private static void dropTableIfExists(Connection con, String aTableName) {
        
        Statement stmt;
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            log.debug("Could not prepare statement.", e);
            throw new RuntimeException(e);
        }
        
        try {
            
            stmt.execute("DROP TABLE " + aTableName);
            log.debug("Dropped table " + aTableName);
            
        } catch (SQLException e) {
            
            // If failed because table does not exist, then we ignore.
            if (e.getMessage().contains("table or view does not exist")) {
                log.debug(aTableName + " does not exist.");
            } else {
                throw new RuntimeException("Failed to drop table " + aTableName, e);
            }
            
        }
        
    }
    
    public static void getUserTables(Connection con) throws SQLException {
        Set<String> tableNames = new HashSet<String>();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet res = meta.getTables(null, "ORA_J5M8", null, 
           new String[] {"TABLE"});
//        while (res.next()) {
//           System.out.println(
//              "   "+res.getString("TABLE_CAT") 
//             + ", "+res.getString("TABLE_SCHEM")
//             + ", "+res.getString("TABLE_NAME")
//             + ", "+res.getString("TABLE_TYPE")
//             + ", "+res.getString("REMARKS")); 
//        }
        while (res.next()) {
            tableNames.add(res.getString("TABLE_NAME"));
        }
    }

}

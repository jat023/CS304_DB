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
import ca.ubc.cs.cs304.steemproject.game.PurchasableGame;

public class InitializeDatabase {

    private static final Logger log = Logger.getLogger(InitializeDatabase.class);

    private static final String createCustomerSQL = "CREATE TABLE " +Tables.CUSTOMER_TABLENAME+ " (" +
            Tables.USER_ATTR_USERID+ " INT," +
            Tables.USER_ATTR_EMAIL+ " VARCHAR(30) NOT NULL UNIQUE," +
            Tables.USER_ATTR_PASSWORD+ " VARCHAR(30) NOT NULL," +
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ") )";

    private static final String createGameTesterSQL = "CREATE TABLE " +Tables.GAME_TESTER_TABLENAME+ " (" +
            Tables.USER_ATTR_USERID+ " INT," +
            Tables.USER_ATTR_EMAIL+ " VARCHAR(30) NOT NULL UNIQUE," +
            Tables.USER_ATTR_PASSWORD+ " VARCHAR(30) NOT NULL," +
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ") )";

    private static final String createFinalizedGameSQL = "CREATE TABLE " +Tables.PURCHASABLE_GAME_TABLENAME+ " (" +
            Tables.GAME_ATTR_NAME+ " VARCHAR(15)," +
            Tables.GAME_ATTR_DESCRIPTION+ " VARCHAR(2000) NOT NULL," +
            Tables.GAME_ATTR_GENRE+ " VARCHAR(15) NOT NULL," +
            Tables.GAME_ATTR_DEVELOPER+ " VARCHAR(15) NOT NULL," +
            Tables.PURCHASABLE_GAME_ATTR_RATING+ " NUMBER(3,1) NOT NULL," +
            Tables.PURCHASABLE_GAME_ATTR_FULLPRICE+ " NUMBER(4,2) NOT NULL," +
            Tables.PURCHASABLE_GAME_ATTR_ONSPECIAL+ " NUMBER(1) DEFAULT 0," +
            Tables.PURCHASABLE_GAME_ATTR_DISCOUNTPERC+ " NUMBER(3,2)," +
            "PRIMARY KEY (" +Tables.GAME_ATTR_NAME+ ")," +
            "CONSTRAINT specialContraint CHECK (" +Tables.PURCHASABLE_GAME_ATTR_ONSPECIAL+ " = 0 OR " +Tables.PURCHASABLE_GAME_ATTR_ONSPECIAL+ " = 1),"+
            "CONSTRAINT ratingConstraint CHECK (" +Tables.PURCHASABLE_GAME_ATTR_RATING+ " >= 0 AND " +Tables.PURCHASABLE_GAME_ATTR_RATING+ " <= 10),"+
            "CONSTRAINT discountConstraint CHECK (" +Tables.PURCHASABLE_GAME_ATTR_DISCOUNTPERC+ " <= 1 AND " +Tables.PURCHASABLE_GAME_ATTR_DISCOUNTPERC+ " >= 0) )";

    private static final String createGameInDevelopmentSQL = "CREATE TABLE "+Tables.DEVELOPMENT_GAMETABLENAME+" ("+
            Tables.GAME_ATTR_NAME+ " VARCHAR(15)," +
            Tables.GAME_ATTR_DESCRIPTION+ " VARCHAR(2000) NOT NULL," +
            Tables.GAME_ATTR_GENRE+ " VARCHAR(15) NOT NULL,"+
            Tables.GAME_ATTR_DEVELOPER+ " VARCHAR(15) NOT NULL,"+
            Tables.DEVELOPMENT_GAME_ATTR_VERSION+ " CHAR(10) NOT NULL," +
            "PRIMARY KEY (" +Tables.GAME_ATTR_NAME+ ") )";

    private static final String createCreditCardSQL = "CREATE TABLE "+Tables.CREDIT_CARD_TABLENAME+" (" +
            Tables.CREDIT_CARD_ATTR_CARDNUM+ " NUMBER(16),"+
            Tables.CREDIT_CARD_ATTR_ADDRESS+ " VARCHAR(30) NOT NULL,"+
            Tables.CREDIT_CARD_ATTR_CCV+ " NUMBER(3) NOT NULL,"+
            Tables.USER_ATTR_USERID+ " INT NOT NULL,"+
            "PRIMARY KEY (" + Tables.CREDIT_CARD_ATTR_CARDNUM+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.CUSTOMER_TABLENAME+ " )";

    private static final String createOwnsGameSQL = "Create TABLE "+Tables.OWNS_GAME_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(15),"+
            Tables.OWNS_GAME_ATTR_HOURS+ " NUMBER(2) DEFAULT 0,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.CUSTOMER_TABLENAME+ ","+
            "FOREIGN KEY (" +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.PURCHASABLE_GAME_TABLENAME+ " )";

    private static final String createTransactionSQL = "CREATE TABLE "+Tables.TRANSACTION_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(15),"+
            Tables.CREDIT_CARD_ATTR_CARDNUM+ " NUMBER(16) NOT NULL,"+
            "dateTime TIMESTAMP NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.OWNS_GAME_TABLENAME+ ","+
            "FOREIGN KEY (" + Tables.CREDIT_CARD_ATTR_CARDNUM+ ") REFERENCES " +Tables.CREDIT_CARD_TABLENAME+ " )";

    private static final String createTestSQL = "CREATE TABLE "+Tables.TEST_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(15),"+
            "dateTime TIMESTAMP NOT NULL,"+
            Tables.PURCHASABLE_GAME_ATTR_RATING+ " NUMBER(2,1) NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ ", dateTime),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.GAME_TESTER_TABLENAME+ ","+
            "FOREIGN KEY (" +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.DEVELOPMENT_GAMETABLENAME+ " )";

    public static void main(String args[]) throws SQLException {

        Connection con = SteemDbConnector.getDefaultConnection();
        Statement statement = con.createStatement();

        // Drop existing tables.

        dropTableIfExists(con, Tables.TEST_TABLENAME);
        dropTableIfExists(con, Tables.TRANSACTION_TABLENAME);
        dropTableIfExists(con, Tables.OWNS_GAME_TABLENAME);
        dropTableIfExists(con, Tables.CREDIT_CARD_TABLENAME);
        dropTableIfExists(con, Tables.DEVELOPMENT_GAMETABLENAME);
        dropTableIfExists(con, Tables.PURCHASABLE_GAME_TABLENAME);
        dropTableIfExists(con, Tables.GAME_TESTER_TABLENAME);
        dropTableIfExists(con, Tables.CUSTOMER_TABLENAME);

        // Create tables.

        statement.execute(createCustomerSQL);
        log.info("Created table " + Tables.CUSTOMER_TABLENAME);

        statement.execute(createGameTesterSQL);
        log.info("Created table " + Tables.GAME_TESTER_TABLENAME);

        statement.execute(createFinalizedGameSQL);
        log.info("Created table " + Tables.PURCHASABLE_GAME_TABLENAME);

        statement.execute(createGameInDevelopmentSQL);
        log.info("Created table " + Tables.DEVELOPMENT_GAMETABLENAME);

        statement.execute(createCreditCardSQL);
        log.info("Created table " + Tables.CREDIT_CARD_TABLENAME);

        statement.execute(createOwnsGameSQL);
        log.info("Created table " + Tables.OWNS_GAME_TABLENAME);

        statement.execute(createTransactionSQL);
        log.info("Created table " + Tables.TRANSACTION_TABLENAME);

        statement.execute(createTestSQL);
        log.info("Created table " + Tables.TEST_TABLENAME);

        Tables.addNewPurchasableGame(new PurchasableGame("game1","fun game", "RPG", "Bob", 10f, 1.00f, false, 0f));
        Tables.addNewPurchasableGame(new PurchasableGame("game2","fun game", "PUZZLE", "Dan Inc.", 8.8f, 9.99f, true, 0.4f));
        Tables.addNewPurchasableGame(new PurchasableGame("game3","fun game", "ACTION", "Dan", 5f, 59.99f, false, 0.2f));
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

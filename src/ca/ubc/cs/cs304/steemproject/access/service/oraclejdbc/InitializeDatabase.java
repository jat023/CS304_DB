package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.game.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection.SteemOracleDbConnector;

class InitializeDatabase {

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

    private static final String createFinalizedGameSQL = "CREATE TABLE " +Tables.FINALIZED_GAME_TABLENAME+ " (" +
            Tables.GAME_ATTR_NAME+ " VARCHAR(15)," +
            Tables.GAME_ATTR_DESCRIPTION+ " VARCHAR(2000) NOT NULL," +
            Tables.GAME_ATTR_GENRE+ " VARCHAR(15) NOT NULL," +
            Tables.GAME_ATTR_DEVELOPER+ " VARCHAR(15) NOT NULL," +
            Tables.FINALIZED_GAME_ATTR_RATING+ " NUMBER(3,1) NOT NULL," +
            Tables.FINALIZED_GAME_ATTR_FULLPRICE+ " NUMBER(4,2) NOT NULL," +
            Tables.FINALIZED_GAME_ATTR_ONSPECIAL+ " NUMBER(1) DEFAULT 0 NOT NULL," +
            Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC+ " NUMBER(3,2) DEFAULT 0 NOT NULL," +
            "PRIMARY KEY (" +Tables.GAME_ATTR_NAME+ ")," +
            "CONSTRAINT specialContraint CHECK (" +Tables.FINALIZED_GAME_ATTR_ONSPECIAL+ " = 0 OR " +Tables.FINALIZED_GAME_ATTR_ONSPECIAL+ " = 1),"+
            "CONSTRAINT ratingConstraint CHECK (" +Tables.FINALIZED_GAME_ATTR_RATING+ " >= 0 AND " +Tables.FINALIZED_GAME_ATTR_RATING+ " <= 10),"+
            "CONSTRAINT discountConstraint CHECK (" +Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC+ " <= 1 AND " +Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC+ " >= 0) )";

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

    private static final String createOwnsGameSQL = "CREATE TABLE "+Tables.OWNS_GAME_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(15),"+
            Tables.OWNS_GAME_ATTR_HOURS+ " NUMBER(5) DEFAULT 0 NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.CUSTOMER_TABLENAME+ ","+
            "FOREIGN KEY (" +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.FINALIZED_GAME_TABLENAME+ " )";

    private static final String createTransactionSQL = "CREATE TABLE "+Tables.TRANSACTION_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(15),"+
            Tables.CREDIT_CARD_ATTR_CARDNUM+ " NUMBER(16),"+
            Tables.TRANSACTION_DATE+ " TIMESTAMP NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.OWNS_GAME_TABLENAME+ ","+
            "FOREIGN KEY (" + Tables.CREDIT_CARD_ATTR_CARDNUM+ ") REFERENCES " +Tables.CREDIT_CARD_TABLENAME+ " )";

    private static final String createTestSQL = "CREATE TABLE "+Tables.TEST_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(15),"+
            Tables.TEST_ATTR_DATE+ " TIMESTAMP NOT NULL,"+
            Tables.TEST_ATTR_RATING+ " NUMBER(2,1) NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ ", " +Tables.TEST_ATTR_DATE+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.GAME_TESTER_TABLENAME+ ","+
            "FOREIGN KEY (" +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.DEVELOPMENT_GAMETABLENAME+ " )";

    private static void init() throws SQLException {

        Connection con = SteemOracleDbConnector.getDefaultConnection();
        Statement statement = con.createStatement();

        // Drop existing tables.

        dropTableIfExists(con, Tables.TEST_TABLENAME);
        dropTableIfExists(con, Tables.TRANSACTION_TABLENAME);
        dropTableIfExists(con, Tables.OWNS_GAME_TABLENAME);
        dropTableIfExists(con, Tables.CREDIT_CARD_TABLENAME);
        dropTableIfExists(con, Tables.DEVELOPMENT_GAMETABLENAME);
        dropTableIfExists(con, Tables.FINALIZED_GAME_TABLENAME);
        dropTableIfExists(con, Tables.GAME_TESTER_TABLENAME);
        dropTableIfExists(con, Tables.CUSTOMER_TABLENAME);

        // Create tables.

        statement.execute(createCustomerSQL);
        log.info("Created table " + Tables.CUSTOMER_TABLENAME);

        statement.execute(createGameTesterSQL);
        log.info("Created table " + Tables.GAME_TESTER_TABLENAME);

        statement.execute(createFinalizedGameSQL);
        log.info("Created table " + Tables.FINALIZED_GAME_TABLENAME);

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

        QueryHelper.insertNewPurchasableGame(new FinalizedGame("game1","fun game", "RPG", "Bob", 10f, 1.00f, false, 0f));
        QueryHelper.insertNewPurchasableGame(new FinalizedGame("game2","fun game", "PUZZLE", "Dan Inc.", 8.8f, 9.99f, true, 0.4f));
        QueryHelper.insertNewPurchasableGame(new FinalizedGame("game3","fun game", "ACTION", "Dan", 5f, 59.99f, false, 0.2f));
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
    
    public static void main(String[] args) {
        try {
            init();
            log.info("Database and tables have been successfully reset.");
        } catch (Exception e) {
            log.error("Database initialization failed.", e);
        }
    }

}

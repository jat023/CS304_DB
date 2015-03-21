package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;

final class InitializeDatabase {

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
            Tables.TRANSACTION_ATTR_TIME+ " DATE NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.OWNS_GAME_TABLENAME+ ","+
            "FOREIGN KEY (" + Tables.CREDIT_CARD_ATTR_CARDNUM+ ") REFERENCES " +Tables.CREDIT_CARD_TABLENAME+ " )";

    private static final String createTestSQL = "CREATE TABLE "+Tables.FEEDBACK_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(15),"+
            Tables.FEEDBACK_ATTR_TIME+ " DATE NOT NULL,"+
            Tables.FEEDBACK_ATTR_RATING+ " NUMBER(2,1) NOT NULL,"+
            Tables.FEEDBACK_ATTR_FEEDBACK+ " VARCHAR(2000) NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ ", " +Tables.FEEDBACK_ATTR_TIME+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.GAME_TESTER_TABLENAME+ ","+
            "FOREIGN KEY (" +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.DEVELOPMENT_GAMETABLENAME+ " )";

    
    private static Connection fConnection;
    private static PreparedStatement fInsertGameStatement;
    
    private static void init() throws SQLException {
        
        fConnection = SteemOracleDbConnector.getDefaultConnection();
        fInsertGameStatement = fConnection.prepareStatement("INSERT INTO " + Tables.FINALIZED_GAME_TABLENAME
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
                + "(?,?,?,?,?,?,?,?)");
        
        Statement statement = fConnection.createStatement();

        // Drop existing tables.

        dropTableIfExists(fConnection, Tables.FEEDBACK_TABLENAME);
        dropTableIfExists(fConnection, Tables.TRANSACTION_TABLENAME);
        dropTableIfExists(fConnection, Tables.OWNS_GAME_TABLENAME);
        dropTableIfExists(fConnection, Tables.CREDIT_CARD_TABLENAME);
        dropTableIfExists(fConnection, Tables.DEVELOPMENT_GAMETABLENAME);
        dropTableIfExists(fConnection, Tables.FINALIZED_GAME_TABLENAME);
        dropTableIfExists(fConnection, Tables.GAME_TESTER_TABLENAME);
        dropTableIfExists(fConnection, Tables.CUSTOMER_TABLENAME);

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
        log.info("Created table " + Tables.FEEDBACK_TABLENAME);

        // Insert at least five rows into all the tables.
        
        insertNewPurchasableGame(new FinalizedGame("game1","fun game", Genre.RPG, "Bob", 10f, 1.00f, false, 0f));
        insertNewPurchasableGame(new FinalizedGame("game2","fun game", Genre.STRATEGY, "Dan Inc.", 8.8f, 9.99f, true, 0.4f));
        insertNewPurchasableGame(new FinalizedGame("game3","fun game", Genre.ACTION, "Dan", 5f, 59.99f, false, 0.2f));
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
    
    private static void insertNewPurchasableGame(FinalizedGame aPurchasableGame) throws SQLException {
        fInsertGameStatement.setString(1, aPurchasableGame.getName());
        fInsertGameStatement.setString(2, aPurchasableGame.getDescription());
        fInsertGameStatement.setString(3, aPurchasableGame.getGenre().name());
        fInsertGameStatement.setString(4, aPurchasableGame.getDeveloper());
        fInsertGameStatement.setFloat(5, aPurchasableGame.getRating());
        fInsertGameStatement.setFloat(6, aPurchasableGame.getFullPrice());
        fInsertGameStatement.setInt(7, aPurchasableGame.isOnSpecial() ? 1 : 0);
        fInsertGameStatement.setFloat(8, aPurchasableGame.getDiscountPercentage());
        fInsertGameStatement.executeUpdate();
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

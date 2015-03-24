package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.development.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.development.GameTesterFeedback;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;

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
            Tables.CREDIT_CARD_ATTR_CARDNUM+ " CHAR(16),"+
            Tables.CREDIT_CARD_ATTR_ADDRESS+ " VARCHAR(30) NOT NULL,"+
            Tables.CREDIT_CARD_ATTR_CCV+ " CHAR(3) NOT NULL,"+
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
            Tables.CREDIT_CARD_ATTR_CARDNUM+ " CHAR(16),"+
            Tables.TRANSACTION_ATTR_TIME+ " DATE NOT NULL,"+
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
    
    private static void init() throws SQLException {
        
        fConnection = SteemOracleDbConnector.getDefaultConnection();
        
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
        
        Customer customer1 = new Customer(1, "customer1@gmail.com", "apple123");
        Customer customer2 = new Customer(2, "customer2@gmail.com", "orange123");
        Customer customer3 = new Customer(3, "customer3@gmail.com", "banana123");
        Customer customer4 = new Customer(4, "customer4@gmail.com", "apple123");
        Customer customer5 = new Customer(5, "customer5@gmail.com", "pear123");
        
        FinalizedGame game1 = new FinalizedGame("game1","fun game", Genre.RPG, "Bob", 10f, 1.00f, false, 0f);
        FinalizedGame game2 = new FinalizedGame("game2","fun game", Genre.STRATEGY, "Dan Inc.", 8.8f, 9.99f, true, 0.4f);
        FinalizedGame game3 = new FinalizedGame("game3","fun game", Genre.ACTION, "Dan", 5f, 59.99f, false, 0.2f);
        FinalizedGame game4 = new FinalizedGame("game4","fun game", Genre.RPG, "Blazzard", 8.2f, 39.99f, true, 0.1f);
        FinalizedGame game5 = new FinalizedGame("game5","fun game", Genre.SPORTS, "Dan", 3.5f, 29.99f, false, 0f);
        
        GameTester tester1 = new GameTester(1, "gametester1@gmail.com", "Pass1");
        GameTester tester2 = new GameTester(2, "gametester2@gmail.com", "Pass2");
        GameTester tester3 = new GameTester(3, "gametester3@gmail.com", "Pass3");
        GameTester tester4 = new GameTester(4, "gametester4@gmail.com", "Pass4");
        GameTester tester5 = new GameTester(5, "gametester5@gmail.com", "Pass5");
        
        GameInDevelopment gameInDev1 = new GameInDevelopment("Developing Game 1","fun game", Genre.RPG, "Bob", "0.10.10");
        GameInDevelopment gameInDev2 = new GameInDevelopment("Developing Game 2","not a fun game", Genre.INDIE, "Dude", "1.0.0");
        GameInDevelopment gameInDev3 = new GameInDevelopment("Developing Game 3","????", Genre.CASUAL, "Blazzard", "0.1.2");
        GameInDevelopment gameInDev4 = new GameInDevelopment("Developing Game 4","fun game", Genre.RPG, "Bobby", "0.3.5");
        GameInDevelopment gameInDev5 = new GameInDevelopment("Developing Game 5","fun game", Genre.SIMULATION, "bobb", "0.3.5");
        
        CreditCard card1 = new CreditCard(customer1, "1111222233334444", "922", "12 Neighbourhood Drive");
        CreditCard card2 = new CreditCard(customer2, "1111222233335555", "214", "100 Broadway Street");
        CreditCard card3 = new CreditCard(customer3, "1111222233336666", "522", "1202 Maple Street");
        CreditCard card4 = new CreditCard(customer4, "1111222233337777", "152", "14 Neighbourhood Drive");
        CreditCard card5 = new CreditCard(customer5, "1111222233338888", "177", "16 Neighbourhood Drive");
        
        Date date1 = createDate(2009,05,15);
        Date date2 = createDate(2010,06,22);
        Date date3 = createDate(2011,06,22);
        Date date4 = createDate(2012,02,25);
        
        Transaction transaction1 = new Transaction( customer1, card1, game1, date1 );
        Transaction transaction2 = new Transaction( customer2, card2, game2, date2 );
        Transaction transaction3 = new Transaction( customer3, card3, game3, date3 );
        Transaction transaction4 = new Transaction( customer4, card4, game4, date4 );
        Transaction transaction5 = new Transaction( customer1, card1, game2, date2 );
        
        GameTesterFeedback feedback1 = new GameTesterFeedback(gameInDev1, tester1, date1, 2.1f, "Very Buggy");
        GameTesterFeedback feedback2 = new GameTesterFeedback(gameInDev1, tester2, date2, 0.1f, "Game too hard");
        GameTesterFeedback feedback3 = new GameTesterFeedback(gameInDev2, tester3, date3, 8.8f, "AWESOME");
        GameTesterFeedback feedback4 = new GameTesterFeedback(gameInDev3, tester4, date3, 3.0f, "Early Development");
        GameTesterFeedback feedback5 = new GameTesterFeedback(gameInDev4, tester1, date4, 8.1f, "After 20 patches..");
        
        
        Inserts.insertCustomer(customer1);
        Inserts.insertCustomer(customer2);
        Inserts.insertCustomer(customer3);
        Inserts.insertCustomer(customer4);
        Inserts.insertCustomer(customer5);
        
        Inserts.insertGameTester(tester1);
        Inserts.insertGameTester(tester2);
        Inserts.insertGameTester(tester3);
        Inserts.insertGameTester(tester4);
        Inserts.insertGameTester(tester5);
        
        Inserts.insertFinalizedGame(game1);
        Inserts.insertFinalizedGame(game2);
        Inserts.insertFinalizedGame(game3);
        Inserts.insertFinalizedGame(game4);
        Inserts.insertFinalizedGame(game5);
        
        Inserts.insertGameInDevelopment(gameInDev1);
        Inserts.insertGameInDevelopment(gameInDev2);
        Inserts.insertGameInDevelopment(gameInDev3);
        Inserts.insertGameInDevelopment(gameInDev4);
        Inserts.insertGameInDevelopment(gameInDev5);
        
        Inserts.insertOwnsGame(new Playtime(customer1, game1, 5.2f));
        Inserts.insertOwnsGame(new Playtime(customer2, game2, 5.2f));
        Inserts.insertOwnsGame(new Playtime(customer3, game3, 5.2f));
        Inserts.insertOwnsGame(new Playtime(customer4, game4, 5.2f));
        Inserts.insertOwnsGame(new Playtime(customer5, game5, 5.2f));
        
        Inserts.insertCreditCard(card1);
        Inserts.insertCreditCard(card2);
        Inserts.insertCreditCard(card3);
        Inserts.insertCreditCard(card4);
        Inserts.insertCreditCard(card5);
        
        Inserts.insertTransaction(transaction1);
        Inserts.insertTransaction(transaction2);
        Inserts.insertTransaction(transaction3);
        Inserts.insertTransaction(transaction4);
        Inserts.insertTransaction(transaction5);
        
        Inserts.insertFeedback(feedback1);
        Inserts.insertFeedback(feedback2);
        Inserts.insertFeedback(feedback3);
        Inserts.insertFeedback(feedback4);
        Inserts.insertFeedback(feedback5);
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
    
    /**
     * Creates a Date and sets the year, month, and day to specified values.
     * Hours, minutes, etc. set to current time.
     **/
    private static Date createDate(int year, int month, int day) {
    	Calendar tempCal = Calendar.getInstance();
        tempCal.set(year, month, day);
        Date tempDate = new Date();
        tempDate = tempCal.getTime();
        return tempDate;
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

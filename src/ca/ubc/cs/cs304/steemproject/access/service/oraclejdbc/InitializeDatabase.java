package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.development.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.development.GameTesterFeedback;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;

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
    private static PreparedStatement fInsertCustomerStatement;
    private static PreparedStatement fInsertGameTesterStatement;
    private static PreparedStatement fInsertGameStatement;
    private static PreparedStatement fInsertGameInDevelopmentStatement;
    private static PreparedStatement fInsertCreditCardStatement;
    private static PreparedStatement fInsertOwnsGameStatement;
    private static PreparedStatement fInsertTransactionStatement;
    private static PreparedStatement fInsertFeedbackStatement;
    
    private static void init() throws SQLException {
        
        fConnection = SteemOracleDbConnector.getDefaultConnection();
        
        //Customer
        fInsertCustomerStatement = fConnection.prepareStatement("INSERT INTO " + Tables.CUSTOMER_TABLENAME
                + "("
                + Tables.USER_ATTR_USERID+ ","
                + Tables.USER_ATTR_EMAIL+ ","
                + Tables.USER_ATTR_PASSWORD+ ","
                + ") VALUES "
                + "(?,?,?)");
        
        //Game Tester
        fInsertGameTesterStatement = fConnection.prepareStatement("INSERT INTO " + Tables.GAME_TESTER_TABLENAME
                + "("
                + Tables.USER_ATTR_USERID+ ","
                + Tables.USER_ATTR_EMAIL+ ","
                + Tables.USER_ATTR_PASSWORD+ ","
                + ") VALUES "
                + "(?,?,?)");
        
        //Finalized Game
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
        
        //Game in Development
        fInsertGameInDevelopmentStatement = fConnection.prepareStatement("INSERT INTO " + Tables.DEVELOPMENT_GAMETABLENAME
                + "("
                + Tables.GAME_ATTR_NAME+ ","
                + Tables.GAME_ATTR_DESCRIPTION+ ","
                + Tables.GAME_ATTR_GENRE+ ","
                + Tables.GAME_ATTR_DEVELOPER+ ","
                + Tables.DEVELOPMENT_GAME_ATTR_VERSION+ ","
                + ") VALUES "
                + "(?,?,?,?,?)");
        
        //Credit Card
        fInsertCreditCardStatement = fConnection.prepareStatement("INSERT INTO " + Tables.CREDIT_CARD_TABLENAME
                + "("
                + Tables.CREDIT_CARD_ATTR_CARDNUM+ ","
                + Tables.CREDIT_CARD_ATTR_ADDRESS+ ","
                + Tables.CREDIT_CARD_ATTR_CCV+ ","
                + Tables.USER_ATTR_USERID+ ","
                + ") VALUES "
                + "(?,?,?,?)");
        
        //Owns Game
        fInsertOwnsGameStatement = fConnection.prepareStatement("INSERT INTO " + Tables.OWNS_GAME_TABLENAME
                + "("
                + Tables.USER_ATTR_USERID+ ","
                + Tables.GAME_ATTR_NAME+ ","
                + Tables.OWNS_GAME_ATTR_HOURS+ ","
                + ") VALUES "
                + "(?,?,?)");
        
        //Transaction
        fInsertTransactionStatement = fConnection.prepareStatement("INSERT INTO " + Tables.TRANSACTION_TABLENAME
                + "("
                + Tables.USER_ATTR_USERID+ ","
                + Tables.GAME_ATTR_NAME+ ","
                + Tables.CREDIT_CARD_ATTR_CARDNUM+ ","
                + Tables.TRANSACTION_ATTR_TIME+ ","
                + ") VALUES "
                + "(?,?,?,?)");
        
        //Test/Feedback
        fInsertFeedbackStatement = fConnection.prepareStatement("INSERT INTO " + Tables.FEEDBACK_TABLENAME
                + "("
                + Tables.USER_ATTR_USERID+ ","
                + Tables.GAME_ATTR_NAME+ ","
                + Tables.FEEDBACK_ATTR_TIME+ ","
                + Tables.FEEDBACK_ATTR_RATING+ ","
                + Tables.FEEDBACK_ATTR_FEEDBACK+ ","
                + ") VALUES "
                + "(?,?,?,?,?)");
        
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
        
        insertNewCustomer(customer1);
        insertNewCustomer(customer2);
        insertNewCustomer(customer3);
        insertNewCustomer(customer4);
        insertNewCustomer(customer5);
        
        insertNewGameTester(tester1);
        insertNewGameTester(tester2);
        insertNewGameTester(tester3);
        insertNewGameTester(tester4);
        insertNewGameTester(tester5);
        
        insertNewPurchasableGame(game1);
        insertNewPurchasableGame(game2);
        insertNewPurchasableGame(game3);
        insertNewPurchasableGame(game4);
        insertNewPurchasableGame(game5);
        
        insertNewGameInDevelopment(gameInDev1);
        insertNewGameInDevelopment(gameInDev2);
        insertNewGameInDevelopment(gameInDev3);
        insertNewGameInDevelopment(gameInDev4);
        insertNewGameInDevelopment(gameInDev5);
        
        insertNewOwnsGame(new Playtime(customer1, game1, 5.2f));
        insertNewOwnsGame(new Playtime(customer2, game2, 5.2f));
        insertNewOwnsGame(new Playtime(customer3, game3, 5.2f));
        insertNewOwnsGame(new Playtime(customer4, game4, 5.2f));
        insertNewOwnsGame(new Playtime(customer5, game5, 5.2f));
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
    
    private static void insertNewCustomer(Customer aCustomer) throws SQLException {
    	fInsertCustomerStatement.setInt(1, aCustomer.getUserId());
    	fInsertCustomerStatement.setString(2, aCustomer.getEmail());
    	fInsertCustomerStatement.setString(3, aCustomer.getPassword());
    	fInsertCustomerStatement.executeUpdate();
    }
    
    private static void insertNewGameTester(GameTester aGameTester) throws SQLException {
    	fInsertGameTesterStatement.setInt(1, aGameTester.getUserId());
    	fInsertGameTesterStatement.setString(2, aGameTester.getEmail());
    	fInsertGameTesterStatement.setString(3, aGameTester.getPassword());
    	fInsertGameTesterStatement.executeUpdate();
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
    
    private static void insertNewGameInDevelopment(GameInDevelopment aGameInDevelopment) throws SQLException {
    	fInsertGameInDevelopmentStatement.setString(1, aGameInDevelopment.getName());
    	fInsertGameInDevelopmentStatement.setString(2, aGameInDevelopment.getDescription());
    	fInsertGameInDevelopmentStatement.setString(3, aGameInDevelopment.getGenre().name());
    	fInsertGameInDevelopmentStatement.setString(4, aGameInDevelopment.getDeveloper());
    	fInsertGameInDevelopmentStatement.setString(5, aGameInDevelopment.getVersion());
    	fInsertGameInDevelopmentStatement.executeUpdate();
    }
    
    /*
    private static void insertNewCreditCard(CreditCard aCreditCard) throws SQLException {
    	fInsertCreditCardStatement.setInt(1, aCreditCard.//getCardNum());
    	fInsertCreditCardStatement.setString(2, aCreditCard.//getaddress);
    	fInsertCreditCardStatement.setInt(3, aCreditCard.//getCCV());
    	fInsertCreditCardStatement.setInt(4, aCreditCard.getUser().getUserId());
    	fInsertCreditCardStatement.executeUpdate();
    }*/
    
    private static void insertNewOwnsGame(Playtime aPlaytime) throws SQLException {
    	fInsertOwnsGameStatement.setInt(1, aPlaytime.getUser().getUserId());
    	fInsertOwnsGameStatement.setString(2, aPlaytime.getGame().getName());
    	fInsertOwnsGameStatement.setFloat(3, aPlaytime.getHoursSpent());
    	fInsertOwnsGameStatement.executeUpdate();
    }
    
    /*
    private static void insertNewTransaction(Transaction aTransaction) throws SQLException {
    	fInsertTransactionStatement.setInt(1, aTransaction.getUser().getUserId());
    	fInsertTransactionStatement.setString(2, aTransaction.getGame().getGameName());
    	fInsertTransactionStatement.setInt(3, aTransaction.//getCreditCard().getCardNum());
    	fInsertTransactionStatement.setDate(4, aTransaction.//getDate());
    	fInsertTransactionStatement.executeUpdate();
    }*/
    
    /* The GameTesterFeedback Class uses user email but should be user id?
     * 
    private static void insertNewFeedback(GameTesterFeedback aFeedback) throws SQLException {
    	java.sql.Date tempDate;
    	fInsertFeedbackStatement.setInt(1, aFeedback.getTester().getUserId());
    	fInsertFeedbackStatement.setString(2, aFeedback.getGame().getName());
    	tempDate = new java.sql.Date(aFeedback.getDate().getTime());
    	fInsertFeedbackStatement.setDate(3, tempDate);
    	fInsertFeedbackStatement.setFloat(4, aFeedback.getRating());
    	fInsertFeedbackStatement.setString(5, aFeedback.getFeedback());
    	fInsertFeedbackStatement.executeUpdate();
    }
    */
    
    public static void main(String[] args) {
        try {
            init();
            log.info("Database and tables have been successfully reset.");
        } catch (Exception e) {
            log.error("Database initialization failed.", e);
        }
    }

}

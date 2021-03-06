package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

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
            Tables.USER_ATTR_EMAIL+ " VARCHAR(50) NOT NULL UNIQUE," +
            Tables.USER_ATTR_PASSWORD+ " VARCHAR(50) NOT NULL," +
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ") )";

    private static final String createGameTesterSQL = "CREATE TABLE " +Tables.GAME_TESTER_TABLENAME+ " (" +
            Tables.USER_ATTR_USERID+ " INT," +
            Tables.USER_ATTR_EMAIL+ " VARCHAR(50) NOT NULL UNIQUE," +
            Tables.USER_ATTR_PASSWORD+ " VARCHAR(50) NOT NULL," +
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ") )";

    private static final String createFinalizedGameSQL = "CREATE TABLE " +Tables.FINALIZED_GAME_TABLENAME+ " (" +
            Tables.GAME_ATTR_NAME+ " VARCHAR(30)," +
            Tables.GAME_ATTR_DESCRIPTION+ " VARCHAR(2000) NOT NULL," +
            Tables.GAME_ATTR_GENRE+ " VARCHAR(15) NOT NULL," +
            Tables.GAME_ATTR_DEVELOPER+ " VARCHAR(30) NOT NULL," +
            Tables.FINALIZED_GAME_ATTR_RATING+ " NUMBER(3,1) NOT NULL," +
            Tables.FINALIZED_GAME_ATTR_FULLPRICE+ " NUMBER(4,2) NOT NULL," +
            Tables.FINALIZED_GAME_ATTR_ONSPECIAL+ " NUMBER(1) DEFAULT 0 NOT NULL," +
            Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC+ " NUMBER(3,2) DEFAULT 0 NOT NULL," +
            "PRIMARY KEY (" +Tables.GAME_ATTR_NAME+ ")," +
            "CONSTRAINT specialContraint CHECK (" +Tables.FINALIZED_GAME_ATTR_ONSPECIAL+ " = 0 OR " +Tables.FINALIZED_GAME_ATTR_ONSPECIAL+ " = 1),"+
            "CONSTRAINT ratingConstraint CHECK (" +Tables.FINALIZED_GAME_ATTR_RATING+ " >= 0 AND " +Tables.FINALIZED_GAME_ATTR_RATING+ " <= 10),"+
            "CONSTRAINT discountConstraint CHECK (" +Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC+ " <= 1 AND " +Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC+ " >= 0) )";

    private static final String createGameInDevelopmentSQL = "CREATE TABLE "+Tables.DEVELOPMENT_GAMETABLENAME+" ("+
            Tables.GAME_ATTR_NAME+ " VARCHAR(30)," +
            Tables.GAME_ATTR_DESCRIPTION+ " VARCHAR(2000) NOT NULL," +
            Tables.GAME_ATTR_GENRE+ " VARCHAR(15) NOT NULL,"+
            Tables.GAME_ATTR_DEVELOPER+ " VARCHAR(30) NOT NULL,"+
            Tables.DEVELOPMENT_GAME_ATTR_VERSION+ " CHAR(10) NOT NULL," +
            "PRIMARY KEY (" +Tables.GAME_ATTR_NAME+ ") )";

    private static final String createCreditCardSQL = "CREATE TABLE "+Tables.CREDIT_CARD_TABLENAME+" (" +
            Tables.CREDIT_CARD_ATTR_CARDNUM+ " CHAR(16),"+
            Tables.CREDIT_CARD_ATTR_ADDRESS+ " VARCHAR(30) NOT NULL,"+
            Tables.CREDIT_CARD_ATTR_CCV+ " CHAR(3) NOT NULL,"+
            Tables.USER_ATTR_USERID+ " INT NOT NULL,"+
            "PRIMARY KEY (" + Tables.CREDIT_CARD_ATTR_CARDNUM+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.CUSTOMER_TABLENAME+ " )";

    private static final String createTransactionSQL = "CREATE TABLE "+Tables.TRANSACTION_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(30),"+
            Tables.CREDIT_CARD_ATTR_CARDNUM+ " CHAR(16),"+
            Tables.TRANSACTION_ATTR_TIME+ " DATE NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ "),"+
            "FOREIGN KEY (" + Tables.CREDIT_CARD_ATTR_CARDNUM+ ") REFERENCES " +Tables.CREDIT_CARD_TABLENAME+ " ON DELETE CASCADE)";

    private static final String createOwnsGameSQL = "CREATE TABLE "+Tables.OWNS_GAME_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(30),"+
            Tables.OWNS_GAME_ATTR_HOURS+ " NUMBER(5) DEFAULT 0 NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.CUSTOMER_TABLENAME+ " ON DELETE CASCADE ,"+
            "FOREIGN KEY (" +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.FINALIZED_GAME_TABLENAME+ " ON DELETE CASCADE ," +
            "CONSTRAINT positivePlaytime CHECK(" +Tables.OWNS_GAME_ATTR_HOURS+ ">=0))";

    private static final String createTestSQL = "CREATE TABLE "+Tables.FEEDBACK_TABLENAME+" (" +
            Tables.USER_ATTR_USERID+ " INT,"+
            Tables.GAME_ATTR_NAME+ " VARCHAR(30),"+
            Tables.FEEDBACK_ATTR_TIME+ " DATE NOT NULL,"+
            Tables.FEEDBACK_ATTR_RATING+ " NUMBER(2,1) NOT NULL,"+
            Tables.FEEDBACK_ATTR_FEEDBACK+ " VARCHAR(2000) NOT NULL,"+
            "PRIMARY KEY (" +Tables.USER_ATTR_USERID+ ", " +Tables.GAME_ATTR_NAME+ ", " +Tables.FEEDBACK_ATTR_TIME+ "),"+
            "FOREIGN KEY (" +Tables.USER_ATTR_USERID+ ") REFERENCES " +Tables.GAME_TESTER_TABLENAME+ " ON DELETE CASCADE ,"+
            "FOREIGN KEY (" +Tables.GAME_ATTR_NAME+ ") REFERENCES " +Tables.DEVELOPMENT_GAMETABLENAME+ " ON DELETE CASCADE )";

    private static Random fRandom = new Random(123);

    private static Connection fConnection;

    private static void init() throws SQLException {

        fConnection = SteemOracleDbConnector.getDefaultConnection();

        Statement statement = fConnection.createStatement();

        // Drop existing tables.

        dropTableIfExists(fConnection, Tables.CUSTOMER_TABLENAME);
        dropTableIfExists(fConnection, Tables.GAME_TESTER_TABLENAME);
        dropTableIfExists(fConnection, Tables.FINALIZED_GAME_TABLENAME);
        dropTableIfExists(fConnection, Tables.DEVELOPMENT_GAMETABLENAME);
        dropTableIfExists(fConnection, Tables.CREDIT_CARD_TABLENAME);
        dropTableIfExists(fConnection, Tables.OWNS_GAME_TABLENAME);
        dropTableIfExists(fConnection, Tables.TRANSACTION_TABLENAME);
        dropTableIfExists(fConnection, Tables.FEEDBACK_TABLENAME);

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

        statement.execute(createTransactionSQL);
        log.info("Created table " + Tables.TRANSACTION_TABLENAME);

        statement.execute(createOwnsGameSQL);
        log.info("Created table " + Tables.OWNS_GAME_TABLENAME);

        statement.execute(createTestSQL);
        log.info("Created table " + Tables.FEEDBACK_TABLENAME);

        // Insert at least five rows into all the tables.

        Customer customer1 = new Customer(1, "customer1@gmail.com", "apple123");
        Customer customer2 = new Customer(2, "customer2@gmail.com", "banana123");
        Customer customer3 = new Customer(3, "customer3@gmail.com", "cherry123");
        Customer customer4 = new Customer(4, "customer4@gmail.com", "date123");
        Customer customer5 = new Customer(5, "customer5@gmail.com", "fig123");

        FinalizedGame game1 = new FinalizedGame("Sneaky Thief","So sneaky.", Genre.RPG, "Bob", 10f, 10.00f, false, 0f);
        FinalizedGame game2 = new FinalizedGame("Amazing Horse","RIDE TO GLORY!", Genre.STRATEGY, "Dan Inc.", 8.8f, 9.99f, true, 0.4f);
        FinalizedGame game3 = new FinalizedGame("Amazing Irish Luck","Fun for the leprechauns.", Genre.ACTION, "Dan", 5f, 59.99f, true, 0.2f);
        FinalizedGame game4 = new FinalizedGame("Fun Classroom Game","Fun for the whole family.", Genre.RPG, "Blazzard", 8.2f, 39.99f, true, 0.1f);
        FinalizedGame game5 = new FinalizedGame("Some CS304 Project","So fun you won't even notice it isn't a game.", Genre.SPORTS, "Dan", 3.5f, 29.99f, false, 0f);
        FinalizedGame game6 = new FinalizedGame("Bunjee","Test your jumping skills!", Genre.CASUAL, "Beema Studios", 7f, 14.49f, false, 0f);
        FinalizedGame game7 = new FinalizedGame("Car 2 GO","Be the first at the finish line!", Genre.RACING, "EC Sports", 6.6f, 25.99f, true, 0.8f);
        FinalizedGame game8 = new FinalizedGame("Truck Smash","Like Monster trucks, but better.", Genre.RACING, "EC Sports", 8.2f, 7.49f, false, 0f);
        FinalizedGame game9 = new FinalizedGame("Tunnel Run","How long can you go?", Genre.CASUAL, "Beema Studios", 9f, 8.99f, false, 0f);
        FinalizedGame game10 = new FinalizedGame("Dancing with the Stars","Rock it, Shake it, Move it.", Genre.CASUAL, "Cumon Entertainment", 9.7f, 49.99f, true, 0.5f);

        GameTester tester1 = new GameTester(1, "gametester1@gmail.com", "Pass1");
        GameTester tester2 = new GameTester(2, "gametester2@gmail.com", "Pass2");
        GameTester tester3 = new GameTester(3, "gametester3@gmail.com", "Pass3");
        GameTester tester4 = new GameTester(4, "gametester4@gmail.com", "Pass4");
        GameTester tester5 = new GameTester(5, "gametester5@gmail.com", "Pass5");

        GameInDevelopment gameInDev1 = new GameInDevelopment("Dev1","Coming to theatres 2016.", Genre.RPG, "Bob", "0.10.10");
        GameInDevelopment gameInDev2 = new GameInDevelopment("The Game","The description.", Genre.INDIE, "Captn Obvious", "1.0.0");
        GameInDevelopment gameInDev3 = new GameInDevelopment("Mind Games","????", Genre.CASUAL, "Blazzard", "0.1.2");
        GameInDevelopment gameInDev4 = new GameInDevelopment("Dev4","This is test game. Please don't upvote.", Genre.RPG, "Bobby", "0.0.0");
        GameInDevelopment gameInDev5 = new GameInDevelopment("The Hack You Never Thought Of","Answer to life is 32.", Genre.SIMULATION, "32", "32");

        CreditCard card1 = new CreditCard(customer1, "1111222233334444", "922", "12 Neighbourhood Drive");
        CreditCard card2 = new CreditCard(customer2, "1111222233335555", "214", "100 Broadway Street");
        CreditCard card3 = new CreditCard(customer3, "1111222233336666", "522", "1202 Maple Street");
        CreditCard card4 = new CreditCard(customer4, "1111222233337777", "152", "14 Cambo Drive");
        CreditCard card5 = new CreditCard(customer5, "1111222233338888", "177", "16 Neighbourhood Drive");
        CreditCard card6 = new CreditCard(customer1, "1111222233339999", "102", "12 Neighbourhood Drive");

        Transaction transaction1 = new Transaction( customer1, card1, game1, generateRandomDate() );
        Transaction transaction2 = new Transaction( customer2, card2, game2, generateRandomDate() );
        Transaction transaction3 = new Transaction( customer3, card3, game3, generateRandomDate() );
        Transaction transaction4 = new Transaction( customer4, card4, game4, generateRandomDate() );
        Transaction transaction5 = new Transaction( customer1, card1, game2, generateRandomDate() );
        Transaction transaction6 = new Transaction( customer3, card3, game2, generateRandomDate() );
        Transaction transaction7 = new Transaction( customer4, card4, game2, generateRandomDate() );
        Transaction transaction8 = new Transaction( customer5, card5, game2, generateRandomDate() );
        Transaction transaction9 = new Transaction( customer2, card2, game6, generateRandomDate() );
        Transaction transaction10 = new Transaction( customer3, card3, game7, generateRandomDate() );
        Transaction transaction11 = new Transaction( customer1, card1, game8, generateRandomDate() );
        Transaction transaction12 = new Transaction( customer5, card5, game9, generateRandomDate() );
        Transaction transaction13 = new Transaction( customer5, card5, game10, generateRandomDate() );
        Transaction transaction14 = new Transaction( customer5, card5, game4, generateRandomDate() );
        Transaction transaction15 = new Transaction( customer5, card5, game3, generateRandomDate() );

        //Transaction transaction16 = new Transaction( customer1, card1, game10, generateRandomDate() );
        Transaction transaction17 = new Transaction( customer2, card2, game10, generateRandomDate() );
        Transaction transaction18 = new Transaction( customer3, card3, game10, generateRandomDate() );
        Transaction transaction19 = new Transaction( customer4, card4, game10, generateRandomDate() );
        
        GameTesterFeedback feedback1 = new GameTesterFeedback(gameInDev1, tester1, generateRandomDate(), 2.1f, "Very Buggy");
        GameTesterFeedback feedback2 = new GameTesterFeedback(gameInDev1, tester2, generateRandomDate(), 0.1f, "Game too hard");
        GameTesterFeedback feedback3 = new GameTesterFeedback(gameInDev2, tester3, generateRandomDate(), 8.8f, "AWESOME");
        GameTesterFeedback feedback4 = new GameTesterFeedback(gameInDev3, tester4, generateRandomDate(), 3.0f, "Early Development");
        GameTesterFeedback feedback5 = new GameTesterFeedback(gameInDev4, tester1, generateRandomDate(), 8.1f, "After 20 patches...");

        log.info("Inserting customers.");

        Inserts.insertCustomer(customer1);
        Inserts.insertCustomer(customer2);
        Inserts.insertCustomer(customer3);
        Inserts.insertCustomer(customer4);
        Inserts.insertCustomer(customer5);

        log.info("Inserting game testers.");

        Inserts.insertGameTester(tester1);
        Inserts.insertGameTester(tester2);
        Inserts.insertGameTester(tester3);
        Inserts.insertGameTester(tester4);
        Inserts.insertGameTester(tester5);

        log.info("Inserting finalized games.");

        Inserts.insertFinalizedGame(game1);
        Inserts.insertFinalizedGame(game2);
        Inserts.insertFinalizedGame(game3);
        Inserts.insertFinalizedGame(game4);
        Inserts.insertFinalizedGame(game5);
        Inserts.insertFinalizedGame(game6);
        Inserts.insertFinalizedGame(game7);
        Inserts.insertFinalizedGame(game8);
        Inserts.insertFinalizedGame(game9);
        Inserts.insertFinalizedGame(game10);

        log.info("Inserting games in development.");

        Inserts.insertGameInDevelopment(gameInDev1);
        Inserts.insertGameInDevelopment(gameInDev2);
        Inserts.insertGameInDevelopment(gameInDev3);
        Inserts.insertGameInDevelopment(gameInDev4);
        Inserts.insertGameInDevelopment(gameInDev5);

        log.info("Inserting credit cards.");

        Inserts.insertCreditCard(card1);
        Inserts.insertCreditCard(card2);
        Inserts.insertCreditCard(card3);
        Inserts.insertCreditCard(card4);
        Inserts.insertCreditCard(card5);
        Inserts.insertCreditCard(card6);

        log.info("Inserting transactions");

        Inserts.insertTransaction(transaction1);
        Inserts.insertTransaction(transaction2);
        Inserts.insertTransaction(transaction3);
        Inserts.insertTransaction(transaction4);
        Inserts.insertTransaction(transaction5);
        Inserts.insertTransaction(transaction6);
        Inserts.insertTransaction(transaction7);
        Inserts.insertTransaction(transaction8);
        Inserts.insertTransaction(transaction9);
        Inserts.insertTransaction(transaction10);
        Inserts.insertTransaction(transaction11);
        Inserts.insertTransaction(transaction12);
        Inserts.insertTransaction(transaction13);
        Inserts.insertTransaction(transaction14);
        Inserts.insertTransaction(transaction15);
        
        Inserts.insertTransaction(transaction17);
        Inserts.insertTransaction(transaction18);
        Inserts.insertTransaction(transaction19);

        log.info("Inserting play times.");

        Inserts.insertOwnsGame(new Playtime(transaction1.getBuyer(), transaction1.getGame(), 5.2f));
        Inserts.insertOwnsGame(new Playtime(transaction2.getBuyer(), transaction2.getGame(), 2.4f));
        Inserts.insertOwnsGame(new Playtime(transaction3.getBuyer(), transaction3.getGame(), 0.2f));
        Inserts.insertOwnsGame(new Playtime(transaction4.getBuyer(), transaction4.getGame(), 66f));
        Inserts.insertOwnsGame(new Playtime(transaction5.getBuyer(), transaction5.getGame(), 742.0f));
        Inserts.insertOwnsGame(new Playtime(transaction6.getBuyer(), transaction6.getGame(), 14.8f));
        Inserts.insertOwnsGame(new Playtime(transaction7.getBuyer(), transaction7.getGame(), 19.0f));
        Inserts.insertOwnsGame(new Playtime(transaction8.getBuyer(), transaction8.getGame(), 36.4f));
        
        Inserts.insertOwnsGame(new Playtime(transaction9.getBuyer(), transaction9.getGame(), 19.0f));
        Inserts.insertOwnsGame(new Playtime(transaction10.getBuyer(), transaction10.getGame(), 12.2f));
        Inserts.insertOwnsGame(new Playtime(transaction11.getBuyer(), transaction11.getGame(), 6.0f));
        Inserts.insertOwnsGame(new Playtime(transaction12.getBuyer(), transaction12.getGame(), 7.1f));
        Inserts.insertOwnsGame(new Playtime(transaction13.getBuyer(), transaction13.getGame(), 164.4f));
        Inserts.insertOwnsGame(new Playtime(transaction14.getBuyer(), transaction14.getGame(), 1086.0f));
        Inserts.insertOwnsGame(new Playtime(transaction15.getBuyer(), transaction15.getGame(), 41.0f));
        
        Inserts.insertOwnsGame(new Playtime(transaction17.getBuyer(), transaction17.getGame(), 0.0f));
        Inserts.insertOwnsGame(new Playtime(transaction18.getBuyer(), transaction18.getGame(), 0.0f));
        Inserts.insertOwnsGame(new Playtime(transaction19.getBuyer(), transaction19.getGame(), 0.0f));

        log.info("Inserting feedbacks.");

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

            stmt.execute("DROP TABLE " + aTableName + " CASCADE CONSTRAINTS");
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
        Date tempDate = tempCal.getTime();
        tempDate.setTime(tempDate.getTime() + randInt(-86400000, 86400000)); // plus or minus a day
        return tempDate;
    }

    private static Date generateRandomDate() {
        return createDate(randInt(2000,2015), randInt(1,12), randInt(1,28));
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {

        int randomNum = fRandom.nextInt((max - min) + 1) + min;

        return randomNum;
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

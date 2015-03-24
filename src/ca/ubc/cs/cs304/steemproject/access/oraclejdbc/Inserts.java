package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.development.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.development.GameTesterFeedback;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;

final class Inserts {

    private static PreparedStatement fInsertCustomerStatement;
    private static PreparedStatement fInsertGameTesterStatement;
    private static PreparedStatement fInsertFinalizedGameStatement;
    private static PreparedStatement fInsertGameInDevelopmentStatement;
    private static PreparedStatement fInsertCreditCardStatement;
    private static PreparedStatement fInsertOwnsGameStatement;
    private static PreparedStatement fInsertTransactionStatement;
    private static PreparedStatement fInsertFeedbackStatement;

    public static void insertCustomer(Customer aCustomer) throws SQLException {

        if (fInsertCustomerStatement == null) {
            fInsertCustomerStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement("INSERT INTO " + Tables.CUSTOMER_TABLENAME
                    + "("
                    + Tables.USER_ATTR_USERID+ ","
                    + Tables.USER_ATTR_EMAIL+ ","
                    + Tables.USER_ATTR_PASSWORD
                    + ") VALUES "
                    + "(?,?,?)");
        }

        fInsertCustomerStatement.setInt(1, aCustomer.getUserId());
        fInsertCustomerStatement.setString(2, aCustomer.getEmail());
        fInsertCustomerStatement.setString(3, aCustomer.getPassword());
        fInsertCustomerStatement.executeUpdate();
    }

    public static void insertGameTester(GameTester aGameTester) throws SQLException {

        if (fInsertGameTesterStatement == null) {
            fInsertGameTesterStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "INSERT INTO " + Tables.GAME_TESTER_TABLENAME
                    + "("
                    + Tables.USER_ATTR_USERID+ ","
                    + Tables.USER_ATTR_EMAIL+ ","
                    + Tables.USER_ATTR_PASSWORD
                    + ") VALUES "
                    + "(?,?,?)");
        }
        
        fInsertGameTesterStatement.setInt(1, aGameTester.getUserId());
        fInsertGameTesterStatement.setString(2, aGameTester.getEmail());
        fInsertGameTesterStatement.setString(3, aGameTester.getPassword());
        fInsertGameTesterStatement.executeUpdate();
    }

    public static void insertFinalizedGame(FinalizedGame aPurchasableGame) throws SQLException {

        if (fInsertFinalizedGameStatement == null) {
            fInsertFinalizedGameStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "INSERT INTO " +Tables.FINALIZED_GAME_TABLENAME
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
        }

        fInsertFinalizedGameStatement.setString(1, aPurchasableGame.getName());
        fInsertFinalizedGameStatement.setString(2, aPurchasableGame.getDescription());
        fInsertFinalizedGameStatement.setString(3, aPurchasableGame.getGenre().name());
        fInsertFinalizedGameStatement.setString(4, aPurchasableGame.getDeveloper());
        fInsertFinalizedGameStatement.setFloat(5, aPurchasableGame.getRating());
        fInsertFinalizedGameStatement.setFloat(6, aPurchasableGame.getFullPrice());
        fInsertFinalizedGameStatement.setInt(7, aPurchasableGame.isOnSpecial() ? 1 : 0);
        fInsertFinalizedGameStatement.setFloat(8, aPurchasableGame.getDiscountPercentage());
        fInsertFinalizedGameStatement.executeUpdate();
    }

    public static void insertGameInDevelopment(GameInDevelopment aGameInDevelopment) throws SQLException {

        if (fInsertGameInDevelopmentStatement == null) {
            fInsertGameInDevelopmentStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement("INSERT INTO " + Tables.DEVELOPMENT_GAMETABLENAME
                    + "("
                    + Tables.GAME_ATTR_NAME+ ","
                    + Tables.GAME_ATTR_DESCRIPTION+ ","
                    + Tables.GAME_ATTR_GENRE+ ","
                    + Tables.GAME_ATTR_DEVELOPER+ ","
                    + Tables.DEVELOPMENT_GAME_ATTR_VERSION
                    + ") VALUES "
                    + "(?,?,?,?,?)");
        }

        fInsertGameInDevelopmentStatement.setString(1, aGameInDevelopment.getName());
        fInsertGameInDevelopmentStatement.setString(2, aGameInDevelopment.getDescription());
        fInsertGameInDevelopmentStatement.setString(3, aGameInDevelopment.getGenre().name());
        fInsertGameInDevelopmentStatement.setString(4, aGameInDevelopment.getDeveloper());
        fInsertGameInDevelopmentStatement.setString(5, aGameInDevelopment.getVersion());
        fInsertGameInDevelopmentStatement.executeUpdate();
    }

    public static void insertCreditCard(CreditCard aCreditCard) throws SQLException {
        if (fInsertCreditCardStatement == null) {
            fInsertCreditCardStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement("INSERT INTO " + Tables.CREDIT_CARD_TABLENAME
                    + "("
                    + Tables.CREDIT_CARD_ATTR_CARDNUM+ ","
                    + Tables.CREDIT_CARD_ATTR_ADDRESS+ ","
                    + Tables.CREDIT_CARD_ATTR_CCV+ ","
                    + Tables.USER_ATTR_USERID
                    + ") VALUES "
                    + "(?,?,?,?)");
        }

        fInsertCreditCardStatement.setString(1, aCreditCard.getCardNumber());
        fInsertCreditCardStatement.setString(2, aCreditCard.getAddress());
        fInsertCreditCardStatement.setString(3, aCreditCard.getCcv());
        fInsertCreditCardStatement.setInt(4, aCreditCard.getCardOwner().getUserId());
        fInsertCreditCardStatement.executeUpdate();
    }

    public static void insertOwnsGame(Playtime aPlaytime) throws SQLException {

        if (fInsertOwnsGameStatement == null) {
            fInsertOwnsGameStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement("INSERT INTO " + Tables.OWNS_GAME_TABLENAME
                    + "("
                    + Tables.USER_ATTR_USERID+ ","
                    + Tables.GAME_ATTR_NAME+ ","
                    + Tables.OWNS_GAME_ATTR_HOURS
                    + ") VALUES "
                    + "(?,?,?)");
        }

        fInsertOwnsGameStatement.setInt(1, aPlaytime.getUser().getUserId());
        fInsertOwnsGameStatement.setString(2, aPlaytime.getGame().getName());
        fInsertOwnsGameStatement.setFloat(3, aPlaytime.getHoursSpent());
        fInsertOwnsGameStatement.executeUpdate();
    }

    public static void insertTransaction(Transaction aTransaction) throws SQLException {

        if (fInsertTransactionStatement == null) {
            fInsertTransactionStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement("INSERT INTO " + Tables.TRANSACTION_TABLENAME
                    + "("
                    + Tables.USER_ATTR_USERID+ ","
                    + Tables.GAME_ATTR_NAME+ ","
                    + Tables.CREDIT_CARD_ATTR_CARDNUM+ ","
                    + Tables.TRANSACTION_ATTR_TIME
                    + ") VALUES "
                    + "(?,?,?,?)");
        }

        fInsertTransactionStatement.setInt(1, aTransaction.getBuyer().getUserId());
        fInsertTransactionStatement.setString(2, aTransaction.getGame().getName());
        fInsertTransactionStatement.setString(3, aTransaction.getCreditCard().getCardNumber());
        fInsertTransactionStatement.setTimestamp(4, new java.sql.Timestamp(aTransaction.getDateOfPurchase().getTime()));
        fInsertTransactionStatement.executeUpdate();
    }

    public static void insertFeedback(GameTesterFeedback aFeedback) throws SQLException {

        if (fInsertFeedbackStatement == null) {
            fInsertFeedbackStatement = SteemOracleDbConnector.getDefaultConnection().prepareStatement("INSERT INTO " + Tables.FEEDBACK_TABLENAME
                    + "("
                    + Tables.USER_ATTR_USERID+ ","
                    + Tables.GAME_ATTR_NAME+ ","
                    + Tables.FEEDBACK_ATTR_TIME+ ","
                    + Tables.FEEDBACK_ATTR_RATING+ ","
                    + Tables.FEEDBACK_ATTR_FEEDBACK
                    + ") VALUES "
                    + "(?,?,?,?,?)");
        }
        
        fInsertFeedbackStatement.setInt(1, aFeedback.getTester().getUserId());
        fInsertFeedbackStatement.setString(2, aFeedback.getGame().getName());
        fInsertFeedbackStatement.setTimestamp(3, new Timestamp(aFeedback.getDate().getTime()));
        fInsertFeedbackStatement.setFloat(4, aFeedback.getRating());
        fInsertFeedbackStatement.setString(5, aFeedback.getFeedback());
        fInsertFeedbackStatement.executeUpdate();
    }
}
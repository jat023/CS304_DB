package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.ICustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.InternalConnectionException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public class OracleCustomerAccessor implements ICustomerAccessor {

    private static final Logger log = Logger.getLogger(OracleCustomerAccessor.class);

    private static OracleCustomerAccessor fInstance;

    private final PreparedStatement fRetrieveCreditCardsSQL;
    private final PreparedStatement fDeleteCreditCardSQL;
    private final PreparedStatement fListTransactionHistorySQL;
    private final PreparedStatement fGetCreditCardSQL;
    private final PreparedStatement fGetFinalizedGameSQL;

    private OracleCustomerAccessor() {

        Connection con = SteemOracleDbConnector.getDefaultConnection();

        try {
            fRetrieveCreditCardsSQL = con.prepareStatement("SELECT * FROM " +Tables.CREDIT_CARD_TABLENAME+ " WHERE " +Tables.USER_ATTR_USERID+ "=?");
            fDeleteCreditCardSQL = con.prepareStatement("DELETE FROM " +Tables.CREDIT_CARD_TABLENAME+ " WHERE " +Tables.CREDIT_CARD_ATTR_CARDNUM+ "=?");
            fListTransactionHistorySQL = con.prepareStatement("SELECT * FROM " +Tables.TRANSACTION_TABLENAME
                    + " WHERE " +Tables.USER_ATTR_USERID+ "=? AND "
                    + Tables.TRANSACTION_ATTR_TIME+ " BETWEEN ? AND ? ORDER BY " +Tables.TRANSACTION_ATTR_TIME+ " ASC");
            fGetCreditCardSQL = con.prepareStatement("SELECT * FROM " +Tables.CREDIT_CARD_TABLENAME+ " WHERE " +Tables.CREDIT_CARD_ATTR_CARDNUM+ "=?");
            fGetFinalizedGameSQL = con.prepareStatement("SELECT * FROM " +Tables.FINALIZED_GAME_TABLENAME+ " WHERE " + Tables.GAME_ATTR_NAME+ "=?");
        } catch (SQLException e) {
            log.error("Failed to prepare statements.", e);
            throw new InternalConnectionException("Failed to prepare statements.", e);
        }

    }

    public static OracleCustomerAccessor getInstance() {
        if (fInstance == null) {
            fInstance = new OracleCustomerAccessor();
        } 
        return fInstance;
    }

    @Override
    public List<CreditCard> listCreditCards(Customer aCardOwner) throws UserNotExistsException {
        ResultSet results;

        try {
            fRetrieveCreditCardsSQL.setInt(1, aCardOwner.getUserId());
            results = fRetrieveCreditCardsSQL.executeQuery();
        } catch (SQLException e) {
            log.error("Could not execute query.", e);
            throw new InternalConnectionException("Could not execute query.", e);
        }

        List<CreditCard> creditCards = new ArrayList<CreditCard>();

        try {
            while (results.next()) {
                creditCards.add(new CreditCard(
                        aCardOwner, 
                        results.getString(Tables.CREDIT_CARD_ATTR_CARDNUM), 
                        results.getString(Tables.CREDIT_CARD_ATTR_CCV), 
                        results.getString(Tables.CREDIT_CARD_ATTR_ADDRESS)));
            }
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new InternalConnectionException("Could not read results.", e);
        }

        return creditCards;
    }

    @Override
    public void deleteCreditCard(CreditCard aCreditCard) throws UserNotExistsException {
        try {
            if (QueriesHelper.userExists(aCreditCard.getCardOwner().getUserId(), Tables.CUSTOMER_TABLENAME)) {

                fDeleteCreditCardSQL.setString(1, aCreditCard.getCardNumber());
                fDeleteCreditCardSQL.executeUpdate();

            } else {
                throw new UserNotExistsException();
            }
        } catch (SQLException e) {
            log.error("Could not delete credit card.", e);
            throw new InternalConnectionException("Could not delete credit card.", e);
        }
    }

    @Override
    public void addNewCreditCard(CreditCard aCreditCard) throws UserNotExistsException {
        try {
            if (QueriesHelper.userExists(aCreditCard.getCardOwner().getUserId(), Tables.CUSTOMER_TABLENAME)) {

                Inserts.insertCreditCard(aCreditCard);

            } else {
                throw new UserNotExistsException();
            }
        } catch (SQLException e) {
            log.error("Could not add credit card.", e);
            throw new InternalConnectionException("Could not add credit card.", e);
        }
    }

    @Override
    public void purchaseGame(Customer aCustomer, CreditCard aCreditCard, FinalizedGame aFinalizedGame) throws UserNotExistsException, GameNotExistException {
        if (!QueriesHelper.userExists(aCustomer.getUserId(), Tables.CUSTOMER_TABLENAME)) {
            throw new UserNotExistsException();
        } else if (!GameQueriesHelper.gameExists(aFinalizedGame.getName(), Tables.FINALIZED_GAME_TABLENAME)) {
            throw new GameNotExistException();
        } else if (!QueriesHelper.exists("SELECT * FROM " +Tables.CREDIT_CARD_TABLENAME+ " WHERE " +Tables.CREDIT_CARD_ATTR_CARDNUM+ "='" +aCreditCard.getCardNumber()+"'")) {
            throw new IllegalArgumentException("Credit card does not exist in system. Please add before making purchase.");
        }

        try {
            Inserts.insertTransaction(new Transaction(aCustomer, aCreditCard, aFinalizedGame, new Date(System.currentTimeMillis())));
        } catch (SQLException e) {
            log.error("Could not complete purchase.", e);
            throw new InternalConnectionException("Could not complete purchase.", e);
        }

        try {
            Inserts.insertOwnsGame(new Playtime(aCustomer, aFinalizedGame, 0.0f));
        } catch (SQLException e) {
            log.error("Could not add game to " + Tables.OWNS_GAME_TABLENAME + " table.", e);
            throw new InternalConnectionException("Could not add game to " + Tables.OWNS_GAME_TABLENAME + " table.", e);
        }

    }

    @Override
    public List<Transaction> history(Customer aCustomer, Date aBeforeDate, Date aAfterDate) throws UserNotExistsException {

        ResultSet results;

        try {
            fListTransactionHistorySQL.setInt(1, aCustomer.getUserId());
            fListTransactionHistorySQL.setTimestamp(2, new Timestamp(aBeforeDate.getTime()));
            fListTransactionHistorySQL.setTimestamp(3, new Timestamp(aAfterDate.getTime()));
            results = fListTransactionHistorySQL.executeQuery();
        } catch (SQLException e) {
            log.error("Could not execute query.", e);
            throw new InternalConnectionException("Could not execute query.", e);
        }

        List<Transaction> transactions = new ArrayList<Transaction>();

        try {
            while (results.next()) {
                transactions.add(new Transaction(
                        aCustomer, 
                        retrieveCreditCard(aCustomer, results.getString(Tables.CREDIT_CARD_ATTR_CARDNUM)), 
                        retrieveFinalizedGame(results.getString(Tables.GAME_ATTR_NAME)),
                        new Date(results.getTimestamp(Tables.TRANSACTION_ATTR_TIME).getTime())));
            }
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new InternalConnectionException("Could not read results.", e);
        } catch (GameNotExistException e) {
            log.error("Game not exist.", e);
            throw new InternalConnectionException("Game not exist.", e);
        }
        
        return transactions;
    }

    @Override
    public float userPlayed(Customer aCustomer, FinalizedGame aFinalizedGame, float additionalHours) throws UserNotExistsException, GameNotExistException {
        // TODO Auto-generated method stub
        return 0;
    }

    private CreditCard retrieveCreditCard(Customer aCardOwner, String aCardNumber) {

        try {
            fGetCreditCardSQL.setString(1, aCardNumber);
            ResultSet results = fGetCreditCardSQL.executeQuery();

            if (results.next()) {
                return new CreditCard(
                        aCardOwner, 
                        results.getString(Tables.CREDIT_CARD_ATTR_CARDNUM), 
                        results.getString(Tables.CREDIT_CARD_ATTR_CCV), 
                        results.getString(Tables.CREDIT_CARD_ATTR_ADDRESS));
            } else {
                log.error("Could not find credit card.");
                throw new InternalConnectionException("Could not find credit card.");
            }

        } catch (SQLException e) {
            log.error("Could not find credit card.", e);
            throw new InternalConnectionException("Could not find credit card.", e);
        }

    }

    private FinalizedGame retrieveFinalizedGame(String nameOfGame) throws GameNotExistException {
        ResultSet results;

        try {
            fGetFinalizedGameSQL.setString(1, nameOfGame);
            results = fGetFinalizedGameSQL.executeQuery();
        } catch (SQLException e) {
            log.error("Could not execute query.", e);
            throw new InternalConnectionException("Could not execute query.", e);
        }

        try {
            if (results.next()) {

                return new FinalizedGame(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION),
                        Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE)), 
                        results.getString(Tables.GAME_ATTR_DEVELOPER), 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_RATING), 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_FULLPRICE), 
                        results.getInt(Tables.FINALIZED_GAME_ATTR_ONSPECIAL)==1, 
                        results.getFloat(Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC));


            } else {
                throw new GameNotExistException(nameOfGame);
            }
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException("Could not read results.", e);
        }
    }
}

package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.ICustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
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
    
    private OracleCustomerAccessor() {
        
        Connection con = SteemOracleDbConnector.getDefaultConnection();
        
        try {
            fRetrieveCreditCardsSQL = con.prepareStatement("SELECT * FROM " +Tables.CREDIT_CARD_TABLENAME+ " WHERE " +Tables.USER_ATTR_USERID+ "=?");
            fDeleteCreditCardSQL = con.prepareStatement("DELETE FROM " +Tables.CREDIT_CARD_TABLENAME+ " WHERE " +Tables.CREDIT_CARD_ATTR_CARDNUM+ "=?");
            fListTransactionHistorySQL = con.prepareStatement("SELECT * FROM " +Tables.TRANSACTION_TABLENAME
                    + " WHERE " +Tables.USER_ATTR_USERID+ "=? AND "
                    + Tables.TRANSACTION_ATTR_TIME+ " BETWEEN ? AND ? ORDER BY " +Tables.TRANSACTION_ATTR_TIME+ " ASC");
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteCreditCard(CreditCard aCreditCard) throws UserNotExistsException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addNewCreditCard(CreditCard aCreditCard) throws UserNotExistsException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void purchaseGame(Customer aCustomer, CreditCard aCreditCard, FinalizedGame aFinalizedGame) throws UserNotExistsException, GameNotExistException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Transaction> history(Customer aCustomer, Date aBeforeDate, Date aAfterDate) throws UserNotExistsException {
        // TODO Auto-generated method stub
        return null;
    }


}

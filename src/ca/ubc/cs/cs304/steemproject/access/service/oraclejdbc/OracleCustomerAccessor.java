package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.exception.InternalConnectionException;
import ca.ubc.cs.cs304.steemproject.access.service.ICustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;

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
    public List<CreditCard> listCreditCards(Customer aCardOwner) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean deleteCreditCard(CreditCard aCreditCard) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addNewCreditCard(CreditCard aCreditCard) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean purchaseGame(Customer aCustomer, CreditCard aCreditCard,
            FinalizedGame aFinalizedGame) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Transaction> history(Customer aCustomer, Date aBeforeDate,
            Date aAfterDate) {
        // TODO Auto-generated method stub
        return null;
    }

}

package ca.ubc.cs.cs304.steemproject.access;

import java.util.Date;
import java.util.List;

import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.UserHasExistingCreditCards;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public interface ICustomerAccessor {

    /**
     * Query for list of credit cards that the user owns.
     * @param aCardOwner
     * @return
     */
    public List<CreditCard> listCreditCards(Customer aCardOwner) throws UserNotExistsException;

    /**
     * Delete a credit card that the user owns from the system.
     * @param aCreditCard
     * @return
     */
    public void deleteCreditCard(CreditCard aCreditCard) throws UserNotExistsException;

    /**
     * Add a new credit card associated with the user.
     * @param aCreditCard
     * @return
     */
    public void addNewCreditCard(CreditCard aCreditCard) throws UserNotExistsException;

    /**
     * Purchase a game using a credit card.
     * @param aCustomer
     * @param aCreditCard
     * @param aFinalizedGame
     * @return
     */
    public void purchaseGame(Customer aCustomer, CreditCard aCreditCard, FinalizedGame aFinalizedGame) throws UserNotExistsException, GameNotExistException;

    /**
     * Query for history of transaction details, which includes the times of purchase and the
     * names of each game that the user owns, sorted based on time of purchase.
     */
    public List<Transaction> history(Customer aCustomer, Date aBeforeDate, Date aAfterDate) throws UserNotExistsException;

    /**
     * UPDATE QUERY.
     * Update the CCV of an existing credit card.
     * @return new credit card with the CCV changed.
     */
    public CreditCard updateCCV(String aCardNumber, String aNewCcv);
    
    /**
     * DELETION QUERY.
     * Removes user account from database and removes all associated entries. (ie. transaction and per-game playtime)
     */
    public void removeAccount(Customer aCustomer) throws UserNotExistsException, UserHasExistingCreditCards;
}

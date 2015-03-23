package ca.ubc.cs.cs304.steemproject.access.service;

import java.util.Date;
import java.util.List;

import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;

public interface ICustomerAccessor {

    /**
     * Query for list of credit cards that the user owns.
     * @param aCardOwner
     * @return
     */
    public List<CreditCard> listCreditCards(Customer aCardOwner);

    /**
     * Delete a credit card that the user owns from the system.
     * @param aCreditCard
     * @return
     */
    public boolean deleteCreditCard(CreditCard aCreditCard);

    /**
     * Add a new credit card associated with the user.
     * @param aCreditCard
     * @return
     */
    public boolean addNewCreditCard(CreditCard aCreditCard);

    /**
     * Purchase a game using a credit card.
     * @param aCustomer
     * @param aCreditCard
     * @param aFinalizedGame
     * @return
     */
    public boolean purchaseGame(Customer aCustomer, CreditCard aCreditCard, FinalizedGame aFinalizedGame);

    /**
     * Query for history of transaction details, which includes the times of purchase and the
     * names of each game that the user owns, sorted based on time of purchase.
     */
    public List<Transaction> history(Customer aCustomer, Date aBeforeDate, Date aAfterDate);

}

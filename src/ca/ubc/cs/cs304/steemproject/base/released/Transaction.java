package ca.ubc.cs.cs304.steemproject.base.released;

import java.util.Date;

public class Transaction {

    private final Customer fCustomer;
    private final CreditCard fCreditCard;
    private final FinalizedGame fGame;
    private final Date fDate;
    
    public Transaction(Customer aCustomer, CreditCard aCreditCard, FinalizedGame aFinalizedGame, Date aDate) {
        
        if (aCustomer == null) {
            throw new IllegalArgumentException("Buyer cannot be null.");
        }
        if (aCreditCard == null) {
            throw new IllegalArgumentException("Credit card cannot be null.");
        } else if (!aCreditCard.getCardOwner().equals(aCustomer)) {
            throw new IllegalArgumentException("Given credit card is not owned by the given buyer.");
        }
        if (aFinalizedGame == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }
        if (aDate == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        
        fCustomer = aCustomer;
        fCreditCard = aCreditCard;
        fGame = aFinalizedGame;
        fDate = aDate;
    }
    
    public Customer getBuyer() {
        return fCustomer;
    }
    
    public CreditCard getCreditCard() {
        return fCreditCard;
    }
    
    public FinalizedGame getGame() {
        return fGame;
    }
    
    public Date getDateOfPurchase() {
        return fDate;
    }
}

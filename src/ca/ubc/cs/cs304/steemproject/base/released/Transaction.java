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

    @Override
    public String toString() {
        return "Transaction [fCustomer=" + fCustomer + ", fCreditCard="
                + fCreditCard + ", fGame=" + fGame + ", fDate=" + fDate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fCreditCard == null) ? 0 : fCreditCard.hashCode());
        result = prime * result
                + ((fCustomer == null) ? 0 : fCustomer.hashCode());
        result = prime * result + ((fDate == null) ? 0 : fDate.hashCode());
        result = prime * result + ((fGame == null) ? 0 : fGame.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (fCreditCard == null) {
            if (other.fCreditCard != null)
                return false;
        } else if (!fCreditCard.equals(other.fCreditCard))
            return false;
        if (fCustomer == null) {
            if (other.fCustomer != null)
                return false;
        } else if (!fCustomer.equals(other.fCustomer))
            return false;
        if (fDate == null) {
            if (other.fDate != null)
                return false;
        } else if (!fDate.equals(other.fDate))
            return false;
        if (fGame == null) {
            if (other.fGame != null)
                return false;
        } else if (!fGame.equals(other.fGame))
            return false;
        return true;
    }
    
    
}

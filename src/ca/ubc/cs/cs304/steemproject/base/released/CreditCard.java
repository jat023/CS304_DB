package ca.ubc.cs.cs304.steemproject.base.released;

public class CreditCard {
    
    private final Customer fCardOwner;
    private final int fCardNumber;
    private final int fCcv;
    private final String fAddress;

    public CreditCard(Customer aCardOwner, int aCardNumber, int aCcv, String aAddress) {
        
        if (aCardOwner == null) {
            throw new IllegalArgumentException("Card owner cannot be null.");
        }
        if (Math.floor(Math.log10(aCardNumber))+1 != 16) {
            throw new IllegalArgumentException("Card number must be a 16 digit number.");
        }
        if (Math.floor(Math.log10(aCcv))+1 != 3) {
            throw new IllegalArgumentException("CCV must be a 3 digit number.");
        }
        if (aAddress == null) {
            throw new IllegalArgumentException("Address cannot be null.");
        }
        
        fCardOwner = aCardOwner;
        fCardNumber = aCardNumber;
        fCcv = aCcv;
        fAddress = aAddress;
    }
    
    public Customer getCardOwner() {
        return fCardOwner;
    }
    
    public int getCardNumber() {
        return fCardNumber;
    }
    
    public int getCcv() {
        return fCcv;
    }
    
    public String getAddress() {
        return fAddress;
    }
}

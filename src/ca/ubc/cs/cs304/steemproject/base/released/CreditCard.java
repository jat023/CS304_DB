package ca.ubc.cs.cs304.steemproject.base.released;

public class CreditCard {
    
    private final Customer fCardOwner;
    private final String fCardNumber;
    private final String fCcv;
    private final String fAddress;

    public CreditCard(Customer aCardOwner, String aCardNumber, String aCcv, String aAddress) {
        
        if (aCardOwner == null) {
            throw new IllegalArgumentException("Card owner cannot be null.");
        }
        if (aCardNumber.matches("[0-9]+") == false || aCardNumber.length() != 16) {
            throw new IllegalArgumentException("Card number must be a 16 digit number.");
        }
        if (aCcv.matches("[0-9]+") == false || aCcv.length() != 3) {
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
    
    public String getCardNumber() {
        return fCardNumber;
    }
    
    public String getCcv() {
        return fCcv;
    }
    
    public String getAddress() {
        return fAddress;
    }

    @Override
    public String toString() {
        return "CreditCard [fCardOwner=" + fCardOwner + ", fCardNumber="
                + fCardNumber + ", fCcv=" + fCcv + ", fAddress=" + fAddress
                + "]";
    }
}

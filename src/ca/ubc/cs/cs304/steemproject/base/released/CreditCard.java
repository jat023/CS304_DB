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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fAddress == null) ? 0 : fAddress.hashCode());
        result = prime * result
                + ((fCardNumber == null) ? 0 : fCardNumber.hashCode());
        result = prime * result
                + ((fCardOwner == null) ? 0 : fCardOwner.hashCode());
        result = prime * result + ((fCcv == null) ? 0 : fCcv.hashCode());
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
        CreditCard other = (CreditCard) obj;
        if (fAddress == null) {
            if (other.fAddress != null)
                return false;
        } else if (!fAddress.equals(other.fAddress))
            return false;
        if (fCardNumber == null) {
            if (other.fCardNumber != null)
                return false;
        } else if (!fCardNumber.equals(other.fCardNumber))
            return false;
        if (fCardOwner == null) {
            if (other.fCardOwner != null)
                return false;
        } else if (!fCardOwner.equals(other.fCardOwner))
            return false;
        if (fCcv == null) {
            if (other.fCcv != null)
                return false;
        } else if (!fCcv.equals(other.fCcv))
            return false;
        return true;
    }
    
    
}

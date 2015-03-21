package ca.ubc.cs.cs304.steemproject.base.released;

import ca.ubc.cs.cs304.steemproject.base.IUser;

public class Customer implements IUser {

    private int fUserId;
    private String fEmail;
    private String fPassword;
    
    public Customer(int aUserId, String aEmail, String aPassword) {
        
        if (aEmail == null) {
            throw new IllegalArgumentException("Email cannot be null.");
        }
        if (aPassword == null) {
            throw new IllegalArgumentException("Password cannot be null.");
        }
        
        this.fUserId = aUserId;
        this.fEmail = aEmail;
        this.fPassword = aPassword;
    }
    
    @Override
    public String getEmail() {
        return fEmail;
    }

    @Override
    public int getUserId() {
        return fUserId;
    }

    @Override
    public String getPassword() {
        return fPassword;
    }

}

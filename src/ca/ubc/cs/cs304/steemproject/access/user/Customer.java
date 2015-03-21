package ca.ubc.cs.cs304.steemproject.access.user;

public class Customer implements IUser {

    private int fUserId;
    private String fEmail;
    private String fPassword;
    
    public Customer(int aUserId, String aEmail, String aPassword) {
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

package ca.ubc.cs.cs304.steemproject.base.development;

import ca.ubc.cs.cs304.steemproject.base.IUser;

public class GameTester implements IUser {

    private final int fUserId;
    private final String fEmail;
    private final String fPassword;
    
    public GameTester(int aUserId, String aEmail, String aPassword) {
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

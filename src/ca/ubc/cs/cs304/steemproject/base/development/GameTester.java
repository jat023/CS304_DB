package ca.ubc.cs.cs304.steemproject.base.development;

import ca.ubc.cs.cs304.steemproject.base.IUser;

public class GameTester implements IUser {

    private final int fUserId;
    private final String fEmail;
    private final String fPassword;
    
    public GameTester(int aUserId, String aEmail, String aPassword) {
        
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

    @Override
    public String toString() {
        return "GameTester [fUserId=" + fUserId + ", fEmail=" + fEmail
                + ", fPassword=" + fPassword + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fEmail == null) ? 0 : fEmail.hashCode());
        result = prime * result
                + ((fPassword == null) ? 0 : fPassword.hashCode());
        result = prime * result + fUserId;
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
        GameTester other = (GameTester) obj;
        if (fEmail == null) {
            if (other.fEmail != null)
                return false;
        } else if (!fEmail.equals(other.fEmail))
            return false;
        if (fPassword == null) {
            if (other.fPassword != null)
                return false;
        } else if (!fPassword.equals(other.fPassword))
            return false;
        if (fUserId != other.fUserId)
            return false;
        return true;
    }
    
    
}

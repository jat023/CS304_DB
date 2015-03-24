package ca.ubc.cs.cs304.steemproject.base.development;

import java.util.Date;


public class GameTesterFeedback {
    
    private final GameInDevelopment fGameInDevelopment;
    private final GameTester fGameTester;
    private final Date fDate;
    private final float fRating;
    private final String fFeedback;
    
    public GameTesterFeedback(GameInDevelopment aGameInDevelopment, GameTester aGameTester, Date aDate, float aRating, String aFeedback) {
        
        if (aGameInDevelopment == null) {
            throw new IllegalArgumentException("Game in development cannot be null.");
        }
        if (aGameTester == null) {
            throw new IllegalArgumentException("Game tester cannot be null.");
        }
        if (aDate == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        if (aFeedback == null) {
            throw new IllegalArgumentException("Feedback cannot be null.");
        }
        
        fGameTester = aGameTester;
        fGameInDevelopment = aGameInDevelopment;
        fDate = aDate;
        fRating = aRating;
        fFeedback = aFeedback;
        
    }
    
    public GameInDevelopment getGame() {
        return fGameInDevelopment;
    }
    
    public GameTester getTester() {
        return fGameTester;
    }
    
    public Date getDate() {
        return fDate;
    }
    
    public float getRating() {
        return fRating;
    }
    
    public String getFeedback() {
        return fFeedback;
    }

    @Override
    public String toString() {
        return "GameTesterFeedback [fGameInDevelopment=" + fGameInDevelopment
                + ", fGameTester=" + fGameTester + ", fDate=" + fDate
                + ", fRating=" + fRating + ", fFeedback=" + fFeedback + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fDate == null) ? 0 : fDate.hashCode());
        result = prime * result
                + ((fFeedback == null) ? 0 : fFeedback.hashCode());
        result = prime
                * result
                + ((fGameInDevelopment == null) ? 0 : fGameInDevelopment
                        .hashCode());
        result = prime * result
                + ((fGameTester == null) ? 0 : fGameTester.hashCode());
        result = prime * result + Float.floatToIntBits(fRating);
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
        GameTesterFeedback other = (GameTesterFeedback) obj;
        if (fDate == null) {
            if (other.fDate != null)
                return false;
        } else if (!fDate.equals(other.fDate))
            return false;
        if (fFeedback == null) {
            if (other.fFeedback != null)
                return false;
        } else if (!fFeedback.equals(other.fFeedback))
            return false;
        if (fGameInDevelopment == null) {
            if (other.fGameInDevelopment != null)
                return false;
        } else if (!fGameInDevelopment.equals(other.fGameInDevelopment))
            return false;
        if (fGameTester == null) {
            if (other.fGameTester != null)
                return false;
        } else if (!fGameTester.equals(other.fGameTester))
            return false;
        if (Float.floatToIntBits(fRating) != Float
                .floatToIntBits(other.fRating))
            return false;
        return true;
    }
    
    
}

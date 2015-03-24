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
}

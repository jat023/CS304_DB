package ca.ubc.cs.cs304.steemproject.base.development;

import java.util.Date;


public class GameTesterFeedback {
    
    private final GameInDevelopment fGameInDevelopment;
    private final String fGameTesterEmail;
    private final Date fDate;
    private final float fRating;
    private final String fFeedback;
    
    public GameTesterFeedback(GameInDevelopment aGameInDevelopment, String aGameTesterEmail, Date aDate, float aRating, String aFeedback) {
        
        if (aGameInDevelopment == null) {
            throw new IllegalArgumentException("Game in development cannot be null.");
        }
        if (aGameTesterEmail == null) {
            throw new IllegalArgumentException("Game tester cannot be null.");
        }
        if (aDate == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        if (aFeedback == null) {
            throw new IllegalArgumentException("Feedback cannot be null.");
        }
        
        fGameTesterEmail = aGameTesterEmail;
        fGameInDevelopment = aGameInDevelopment;
        fDate = aDate;
        fRating = aRating;
        fFeedback = aFeedback;
        
    }
    
    public GameInDevelopment getGame() {
        return fGameInDevelopment;
    }
    
    public String getTesterEmail() {
        return fGameTesterEmail;
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

}

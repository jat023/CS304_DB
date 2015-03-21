package ca.ubc.cs.cs304.steemproject.base.development;


public class GameTesterFeedback {
    
    private final GameInDevelopment fGameInDevelopment;
    private final String fGameTesterEmail;
    private final float fRating;
    private final String fFeedback;
    
    public GameTesterFeedback(GameInDevelopment aGameInDevelopment, String aGameTesterEmail, float aRating, String aFeedback) {
        
        if (aGameInDevelopment == null) {
            throw new RuntimeException("Game in development cannot be null.");
        }
        if (aGameTesterEmail == null) {
            throw new RuntimeException("Game tester cannot be null.");
        }
        if (aFeedback == null) {
            throw new RuntimeException("Feedback cannot be null.");
        }
        
        fGameTesterEmail = aGameTesterEmail;
        fGameInDevelopment = aGameInDevelopment;
        fRating = aRating;
        fFeedback = aFeedback;
        
    }
    
    public GameInDevelopment getGame() {
        return fGameInDevelopment;
    }
    
    public String getTesterEmail() {
        return fGameTesterEmail;
    }
    
    public float getRating() {
        return fRating;
    }
    
    public String getFeedback() {
        return fFeedback;
    }

}

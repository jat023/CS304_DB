package ca.ubc.cs.cs304.steemproject.exception;

public class UserHasExistingCreditCards extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserHasExistingCreditCards(){
        super();
    }

    public UserHasExistingCreditCards(String message){
        super(message);
    }
    
    public UserHasExistingCreditCards(String message, Throwable cause){
        super(message, cause);
    }
}

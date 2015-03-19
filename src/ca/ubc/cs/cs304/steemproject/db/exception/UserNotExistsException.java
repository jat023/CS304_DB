package ca.ubc.cs.cs304.steemproject.db.exception;

public class UserNotExistsException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserNotExistsException(){
        super();
    }

    public UserNotExistsException(String message){
        super(message);
    }
    
    public UserNotExistsException(String message, Throwable cause){
        super(message, cause);
    }
}

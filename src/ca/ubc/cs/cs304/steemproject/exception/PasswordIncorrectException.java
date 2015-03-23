package ca.ubc.cs.cs304.steemproject.exception;

public class PasswordIncorrectException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PasswordIncorrectException(){
        super();
    }

    public PasswordIncorrectException(String message){
        super(message);
    }
    
    public PasswordIncorrectException(String message, Throwable cause){
        super(message, cause);
    }
}

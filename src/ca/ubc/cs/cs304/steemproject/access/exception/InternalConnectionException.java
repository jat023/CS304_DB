package ca.ubc.cs.cs304.steemproject.access.exception;

public class InternalConnectionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InternalConnectionException(){
        super();
    }

    public InternalConnectionException(String message){
        super(message);
    }
    
    public InternalConnectionException(String message, Throwable cause){
        super(message, cause);
    }
}

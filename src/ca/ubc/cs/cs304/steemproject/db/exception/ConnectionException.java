package ca.ubc.cs.cs304.steemproject.db.exception;

public class ConnectionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ConnectionException(){
        super();
    }

    public ConnectionException(String message){
        super(message);
    }
    
    public ConnectionException(String message, Throwable cause){
        super(message, cause);
    }
}

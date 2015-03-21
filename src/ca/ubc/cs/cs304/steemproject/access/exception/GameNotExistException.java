package ca.ubc.cs.cs304.steemproject.access.exception;

public class GameNotExistException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GameNotExistException(){
        super();
    }

    public GameNotExistException(String message){
        super(message);
    }
    
    public GameNotExistException(String message, Throwable cause){
        super(message, cause);
    }
}

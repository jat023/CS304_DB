package ca.ubc.cs.cs304.steemproject.access.service;

import ca.ubc.cs.cs304.steemproject.access.exception.PasswordIncorrectException;
import ca.ubc.cs.cs304.steemproject.access.exception.UserNotExistsException;

public interface ILoginAccessor {
    
    /**
     * returns true if email and password correspond to an existing customer.
     * @param email
     * @param password
     * @return
     */
    public boolean loginCustomer(String email, String password) throws UserNotExistsException, PasswordIncorrectException;
    
    /**
     * returns true if email and password correspond to an existing game tester.
     * @param email
     * @param password
     * @return
     */
    public boolean loginGametester(String email, String password) throws UserNotExistsException, PasswordIncorrectException;

}

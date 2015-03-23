package ca.ubc.cs.cs304.steemproject.access;

import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.exception.PasswordIncorrectException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

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

    public Customer lookupCustomer(String email) throws UserNotExistsException;
    public Customer lookupCustomer(int userId) throws UserNotExistsException;
    
    public GameTester lookupGameTester(String email) throws UserNotExistsException;
    public GameTester lookupGameTester(int userId) throws UserNotExistsException;
}

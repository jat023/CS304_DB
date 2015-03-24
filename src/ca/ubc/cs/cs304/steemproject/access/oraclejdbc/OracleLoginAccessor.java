package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.ILoginAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.IUser;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.exception.InternalConnectionException;
import ca.ubc.cs.cs304.steemproject.exception.PasswordIncorrectException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public class OracleLoginAccessor implements ILoginAccessor {

    private static final Logger log = Logger.getLogger(OracleLoginAccessor.class);
    
    private static OracleLoginAccessor fInstance;

    private OracleLoginAccessor() {}

    public static OracleLoginAccessor getInstance() {
        if (fInstance == null) {
            fInstance = new OracleLoginAccessor();
        } 
        return fInstance;
    }

    @Override
    public boolean loginCustomer(String email, String password) throws UserNotExistsException, PasswordIncorrectException {
        return login(email, password, Tables.CUSTOMER_TABLENAME);        
    }

    @Override
    public boolean loginGametester(String email, String password) throws UserNotExistsException, PasswordIncorrectException {
        return login(email, password, Tables.GAME_TESTER_TABLENAME);
    }

    private boolean login(String email, String password, String tablename) throws UserNotExistsException, PasswordIncorrectException {

        if (!QueriesHelper.userExists(email, tablename)) {
            throw new UserNotExistsException();
        }

        String query = "SELECT * FROM " +tablename+ 
                " WHERE " +Tables.USER_ATTR_EMAIL+ "='" +email+ "' AND " +Tables.USER_ATTR_PASSWORD+ "='" +password + "'";

        if (!QueriesHelper.exists(query)) {
            throw new PasswordIncorrectException();
        }

        return true;
    }
    
    @Override
    public Customer lookupCustomer(String email) throws UserNotExistsException {
        IUser user = lookupUser(Tables.CUSTOMER_TABLENAME, email, null);        
        return new Customer(user.getUserId(), user.getEmail(), user.getPassword());
    }

    @Override
    public Customer lookupCustomer(int userId) throws UserNotExistsException {
        IUser user = lookupUser(Tables.CUSTOMER_TABLENAME, null, userId);
        return new Customer(user.getUserId(), user.getEmail(), user.getPassword());
    }

    @Override
    public GameTester lookupGameTester(String email) throws UserNotExistsException {
        IUser user = lookupUser(Tables.GAME_TESTER_TABLENAME, email, null);
        return new GameTester(user.getUserId(), user.getEmail(), user.getPassword());
    }

    @Override
    public GameTester lookupGameTester(int userId) throws UserNotExistsException {
        IUser user = lookupUser(Tables.GAME_TESTER_TABLENAME, null, userId);
        return new GameTester(user.getUserId(), user.getEmail(), user.getPassword());
    }
    
    private IUser lookupUser(String table, String email, Integer userId) throws UserNotExistsException {
        
        if (email == null && userId == null) {
            log.error("No email or user ID supplied.");
            throw new IllegalArgumentException("Exactly one of user email and ID must be given.");
        } else if (email != null && userId != null) {
            log.error("Both email and user ID supplied.");
            throw new IllegalArgumentException("Exactly one of user email and ID must be given.");
        }
        
        String query = "SELECT * FROM " + table + " WHERE " 
        + (email == null ? Tables.USER_ATTR_USERID + "=" + userId : Tables.USER_ATTR_EMAIL + "='" + email + "'");
        
        try {
            
            ResultSet results = SteemOracleDbConnector.getDefaultConnection().createStatement().executeQuery(query);
            
            if (results.next()) {
                
                IUser user = new Customer(results.getInt(Tables.USER_ATTR_USERID), results.getString(Tables.USER_ATTR_EMAIL), results.getString(Tables.USER_ATTR_PASSWORD));
                
                if (results.next()) {
                    throw new RuntimeException("Result returned two users?");
                }
                
                return user;
                
            } else {
                throw new UserNotExistsException();
            }
            
        } catch (SQLException e) {
            log.error("User lookup failed.",e);
            throw new InternalConnectionException("User lookup failed.", e);
        }
        
    }

}

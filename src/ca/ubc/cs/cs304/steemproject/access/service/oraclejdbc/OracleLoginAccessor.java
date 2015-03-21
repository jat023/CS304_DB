package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import ca.ubc.cs.cs304.steemproject.access.exception.PasswordIncorrectException;
import ca.ubc.cs.cs304.steemproject.access.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.access.service.ILoginAccessor;

public class OracleLoginAccessor implements ILoginAccessor {

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

        if (!QueryHelper.userExists(email, tablename)) {
            throw new UserNotExistsException();
        }

        String query = "SELECT * FROM " +tablename+ 
                " WHERE " +Tables.USER_ATTR_EMAIL+ "=" +email+ " AND " +Tables.USER_ATTR_PASSWORD+ "=" +password;

        if (!QueryHelper.exists(query)) {
            throw new PasswordIncorrectException();
        }

        return true;
    }

}

package ca.ubc.cs.cs304.steemproject.access;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.OracleCustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.OracleGameTesterAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.OracleLoginAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.OraclePublicAccessor;

public class Accessors {
    
    public static ILoginAccessor getLoginAccessor() {
        return OracleLoginAccessor.getInstance();
    }
    
    public static IPublicAccessor getPublicAccessor() {
        return OraclePublicAccessor.getInstance();
    }
    
    public static IGameTesterAccessor getGameTesterAccessor() {
        return OracleGameTesterAccessor.getInstance();
    }

    public static ICustomerAccessor getCustomerAccessor() {
        return OracleCustomerAccessor.getInstance();
    }
}

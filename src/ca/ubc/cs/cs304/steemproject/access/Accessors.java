package ca.ubc.cs.cs304.steemproject.access;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.OracleCustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.OracleGameTesterAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.OracleLoginAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.OraclePublicAccessor;

public class Accessors {
    
    public ILoginAccessor getLoginAccessor() {
        return OracleLoginAccessor.getInstance();
    }
    
    public IPublicAccessor getPublicAccessor() {
        return OraclePublicAccessor.getInstance();
    }
    
    public IGameTesterAccessor getGameTesterAccessor() {
        return OracleGameTesterAccessor.getInstance();
    }

    public ICustomerAccessor getCustomerAccessor() {
        return OracleCustomerAccessor.getInstance();
    }
}

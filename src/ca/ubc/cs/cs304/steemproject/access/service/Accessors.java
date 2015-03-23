package ca.ubc.cs.cs304.steemproject.access.service;

import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.OracleCustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.OracleGameTesterAccessor;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.OracleLoginAccessor;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.OraclePublicAccessor;

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

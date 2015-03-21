package ca.ubc.cs.cs304.steemproject.access.service;

import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.OracleLoginAccessor;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.OraclePublicAccessor;

public class Accessors {
    
    public ILoginAccessor getLoginAccessor() {
        return OracleLoginAccessor.getInstance();
    }
    
    public IPublicAccessor getPublicAccessor() {
        return OraclePublicAccessor.getInstance();
    }

}
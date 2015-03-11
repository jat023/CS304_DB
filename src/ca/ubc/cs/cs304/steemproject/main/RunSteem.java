package ca.ubc.cs.cs304.steemproject.main;

import java.sql.Connection;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.db.connection.SteemDbConnector;
import ca.ubc.cs.cs304.steemproject.ui.ConsoleUI;
import ca.ubc.cs.cs304.steemproject.ui.IUI;

public class RunSteem {
    
    public final static Logger log = Logger.getLogger(RunSteem.class);
    
    public static void main(String args[]) {
        IUI ui = new ConsoleUI();
        
        Connection con = SteemDbConnector.getDefaultConnection();
        
        // TODO
    }

}

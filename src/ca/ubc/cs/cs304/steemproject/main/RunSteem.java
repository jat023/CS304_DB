package ca.ubc.cs.cs304.steemproject.main;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
        
        if (con != null) {
        	System.out.println("Connection established");
        }
        else {
        	System.out.println("Connected failed to connect");
        }
        
        // TODO
    }

}

package ca.ubc.cs.cs304.steemproject.main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.database.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.ui.ConsoleUI;
import ca.ubc.cs.cs304.steemproject.ui.IUI;

public class RunSteem extends JFrame{
    
    public final static Logger log = Logger.getLogger(RunSteem.class);
	
    public RunSteem moveFirst() {
    	RunSteem p = new RunSteem();
    	try {
    		rowSet.first();
            p.setPersonId(rowSet.getInt("personId"));
            p.setFirstName(rowSet.getString("firstName"));
            p.setMiddleName(rowSet.getString("middleName"));
            p.setLastName(rowSet.getString("lastName"));
            p.setEmail(rowSet.getString("email"));
            p.setPhone(rowSet.getString("phone"));

        } 
    	catch (SQLException ex) {
            ex.printStackTrace();
    	}
    	return p;
    }
    
    
	public static void main(String args[]) {
        IUI ui = new ConsoleUI();

        Connection con = SteemOracleDbConnector.getDefaultConnection();
        
        if (con != null) {
        	System.out.println("Connection established");
        }
        else {
        	System.out.println("Connected failed to connect");
        }
     
    }

}

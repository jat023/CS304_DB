package ca.ubc.cs.cs304.steemproject.main;

import javax.swing.*;
import java.sql.*;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.ui.ConsoleUI;



public class RunSteem {
    
	public final static Logger log = Logger.getLogger(RunSteem.class);
	
	/*
	 * Basic constructor function for building the UI interface
	 */
	public static void createUI() {
		ConsoleUI mainConsole = new ConsoleUI();
		mainConsole.buildGUI();
	}
	
	public static void main(String args[]) {

        Connection con = SteemOracleDbConnector.getDefaultConnection();
        
        if (con != null) {
        	System.out.println("Connection established");
        }
        else {
        	System.out.println("Connected failed to connect");
        }
     
        createUI();
    }

}

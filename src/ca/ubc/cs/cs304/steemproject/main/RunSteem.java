package ca.ubc.cs.cs304.steemproject.main;

import javax.swing.*;
import java.sql.*;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.ui.MainPanelUI;



public class RunSteem {

    public final static Logger log = Logger.getLogger(RunSteem.class);

    /*
     * Basic constructor function for building the UI interface
     */
    public static void createUI() {
        MainPanelUI mainConsole = new MainPanelUI();
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
        try {
            createUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

}

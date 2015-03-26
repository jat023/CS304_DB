package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.DBCustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;

public class DatabaseUI {
	
	private JFrame mainWindow;
	private JPanel optionsPanel;
	private JPanel buttonPanel;
	private JCheckBox option1check;
	private JCheckBox option2check;
	private JCheckBox option3check;
	private JCheckBox option4check;
	private JButton refreshButton;
	private DBCustomerAccessor customerAccessorDB;
	
	DatabaseUI() throws SQLException {		
		mainWindow = new JFrame("Database UI");
		mainWindow.setSize(700,700);
		mainWindow.setLayout(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		optionsPanel = initCheckBoxesPanel();
		buttonPanel = initRefreshButton();
		//JTable table = new JTable(data, columnNames);
		
		mainWindow.add(optionsPanel);
		mainWindow.add(buttonPanel);
		
		setComponentLocations();
		customerAccessorDB = new DBCustomerAccessor();
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(option1check.isSelected()) {
					List<Integer> userIDs = new ArrayList<Integer>();
					List<String> userEmails = new ArrayList<String>();
					List<String> userPasswords = new ArrayList<String>();
					try {
						userIDs = customerAccessorDB.getIDs();
						userEmails = customerAccessorDB.getEmails();
						userPasswords = customerAccessorDB.getPasswords();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("All user IDs:");
					for( int i = 0; i < userIDs.size(); i++) {
						System.out.println(userIDs.get(i).toString());
					}
					System.out.println("All user Emails:");
					for( int i = 0; i < userEmails.size(); i++) {
						System.out.println(userEmails.get(i).toString());
					}
					System.out.println("All user Passwords:");
					for( int i = 0; i < userPasswords.size(); i++) {
						System.out.println(userPasswords.get(i).toString());
					}
				}
				if(option2check.isSelected()) {
					System.out.println("N/A");
				}
				if(option3check.isSelected()) {
					System.out.println("N/A");
				}
				if(option4check.isSelected()) {
					System.out.println("N/A");
				}
			}
		});
	}
	
	private void setComponentLocations() {
		optionsPanel.setLocation(100, 25);
		buttonPanel.setLocation(225, 125);
	}
	
	private JPanel initRefreshButton() {
		JPanel aButtonPanel = new JPanel();
		aButtonPanel.setSize(250, 100);
		
		refreshButton = new JButton("Refresh");
		aButtonPanel.add(refreshButton);
		
		return aButtonPanel;
	}
	
	private JPanel initCheckBoxesPanel() {
		JPanel aOptionsPanel = new JPanel();
		aOptionsPanel.setSize(500, 100);
		Border optionsBorder = BorderFactory.createTitledBorder("Show:");
		aOptionsPanel.setBorder(optionsBorder);
		
		option1check = new JCheckBox("Customers");
		aOptionsPanel.add(option1check);
		option2check = new JCheckBox("Game Testers");
		aOptionsPanel.add(option2check);
		option3check = new JCheckBox("Credit Cards");
		aOptionsPanel.add(option3check);
		option4check = new JCheckBox("Games");
		aOptionsPanel.add(option4check);
		
		return aOptionsPanel;
	}
	
	public void show() {
		mainWindow.setVisible(true);
	}
	
	public void hide() {
		mainWindow.setVisible(true);
	}
	
	public static void main(String args[]) throws SQLException {
        Connection con = SteemOracleDbConnector.getDefaultConnection();
        
        if (con != null) {
        	System.out.println("Connection established");
        }
        else {
        	System.out.println("Connected failed to connect");
        }

		DatabaseUI tablesUI = new DatabaseUI();
		tablesUI.show();
		
	}
}

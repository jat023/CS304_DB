package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.DBGeneralAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;

public class DatabaseUI {
	
	private JFrame mainWindow;
	private JPanel optionsPanel;
	private JPanel buttonPanel;
	private JPanel tablePanel;
	private JPanel tablePanel2;
	private JCheckBox option1check;
	private JCheckBox option2check;
	private JCheckBox option3check;
	private JCheckBox option4check;
	private JButton refreshButton;
	private DBGeneralAccessor generalAccessorDB;
	
	DatabaseUI() throws SQLException {		
		mainWindow = new JFrame("Database UI");
		mainWindow.setSize(700,700);
		mainWindow.setLayout(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		optionsPanel = initCheckBoxesPanel();
		buttonPanel = initRefreshButton();
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setSize(600, 100);
		tablePanel2 = new JPanel();
		tablePanel2.setLayout(new BorderLayout());
		tablePanel2.setSize(600, 100);
		
		mainWindow.add(optionsPanel);
		mainWindow.add(buttonPanel);
		mainWindow.add(tablePanel);
		mainWindow.add(tablePanel2);
		
		setComponentLocations();
		generalAccessorDB = new DBGeneralAccessor();
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(option1check.isSelected()) {
					List<Integer> userIDs;
					List<String> userEmails;
					List<String> userPasswords;
					try {
						userIDs = generalAccessorDB.getCustomers().getIDs();
						userEmails = generalAccessorDB.getCustomers().getEmails();
						userPasswords = generalAccessorDB.getCustomers().getPasswords();
						
						String columnNames[] = {"Customer ID","Customer Email","Customer Password"};
						DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
						JTable customerTable = new JTable(tableModel);

						for( int i = 0; i < userIDs.size(); i++ ) {
							Object[] rowObj = {userIDs.get(i).toString(), userEmails.get(i).toString(),
									userPasswords.get(i).toString()};
							tableModel.addRow(rowObj);
						}
						
						JScrollPane scrollPane = new JScrollPane(customerTable);
					    tablePanel.add(scrollPane, BorderLayout.CENTER);
						
					    tablePanel.setVisible(true);
						tablePanel.revalidate();
						tablePanel.repaint();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					tablePanel.removeAll();
					tablePanel.revalidate();
					tablePanel.repaint();
					tablePanel.setVisible(false);
				}
				if(option2check.isSelected()) {
					List<Integer> testerIDs;
					List<String> testerEmails;
					List<String> testerPasswords;
					try {
						testerIDs = generalAccessorDB.getTesters().getIDs();
						testerEmails = generalAccessorDB.getTesters().getEmails();
						testerPasswords = generalAccessorDB.getTesters().getPasswords();
						
						String columnNames[] = {"Tester ID","Tester Email","Tester Password"};
						DefaultTableModel tableModel2 = new DefaultTableModel(columnNames, 0);
						JTable testerTable = new JTable(tableModel2);

						for( int i = 0; i < testerIDs.size(); i++ ) {
							Object[] rowObj = {testerIDs.get(i).toString(), testerEmails.get(i).toString(),
									testerPasswords.get(i).toString()};
							tableModel2.addRow(rowObj);
						}
						
						JScrollPane scrollPane2 = new JScrollPane(testerTable);
					    tablePanel2.add(scrollPane2, BorderLayout.CENTER);
						
					    tablePanel2.setVisible(true);
						tablePanel2.revalidate();
						tablePanel2.repaint();
					}catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					tablePanel2.removeAll();
					tablePanel2.revalidate();
					tablePanel2.repaint();
					tablePanel2.setVisible(false);
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
		tablePanel.setLocation(50,175);
		tablePanel2.setLocation(50,280);
	}
	
	private JPanel initRefreshButton() {
		JPanel aButtonPanel = new JPanel();
		aButtonPanel.setSize(250, 50);
		
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

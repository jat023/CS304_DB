package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.ui.panels.CustomerPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.GameTesterPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.LoginPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.LoginPanel.LoginStatus;
import ca.ubc.cs.cs304.steemproject.ui.panels.PublicPanel;


public class MainPanelUI {
		
	/*
	 * The main function for building the GUI
	 * 				that is called from main in RunSteem.java
	 */
	public void buildGUI() {
		JFrame mainWindow = new JFrame("Steem");
		final JTabbedPane tabbedPane = new JTabbedPane();
		
		final JButton logOut = new JButton("Log out");
		logOut.setBounds(460,3,90,25);
		mainWindow.add(logOut);
		
		mainWindow.setSize(550,700);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final LoginPanel theLoginPanel = new LoginPanel(Accessors.getLoginAccessor());
		tabbedPane.add("  Log In  ", theLoginPanel);
		tabbedPane.add("  Search  ", new PublicPanel(Accessors.getPublicAccessor()));
		
		mainWindow.add(tabbedPane);
		mainWindow.setVisible(true);
		
			// LOGIN to account based on whether CUSTOMER or GAMETESTER is selected
		theLoginPanel.getLoginStatusButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if( theLoginPanel.getLoginStatus().equals(LoginStatus.CUSTOMER) ) {
					tabbedPane.add("  Customer  ", new CustomerPanel(Accessors.getCustomerAccessor(), theLoginPanel.getCustomer() ));
				}
				if( theLoginPanel.getLoginStatus().equals(LoginStatus.GAMETESTER) ) {
					tabbedPane.add("  Tester  ", new GameTesterPanel(Accessors.getGameTesterAccessor(), theLoginPanel.getGameTester() ));
				}
			}
		});
		
			// LOGOUT of the current user [close tab(s)]
		logOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if( theLoginPanel.getLoginStatus().equals(LoginStatus.CUSTOMER) ) {
					theLoginPanel.logout();
					tabbedPane.remove(tabbedPane.getTabCount() - 1);
				}
				if( theLoginPanel.getLoginStatus().equals(LoginStatus.GAMETESTER) ) {
					theLoginPanel.logout();
					tabbedPane.remove(tabbedPane.getTabCount() - 1);
				}
			}
		});
	}
}
   

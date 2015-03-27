package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
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
		
		mainWindow.setSize(550,700);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final LoginPanel theLoginPanel = new LoginPanel(Accessors.getLoginAccessor());
		tabbedPane.add(theLoginPanel);
		tabbedPane.add(new PublicPanel(Accessors.getPublicAccessor()));
		
		mainWindow.add(tabbedPane);
		mainWindow.setVisible(true);
		
		theLoginPanel.getLoginStatusButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if( theLoginPanel.getLoginStatus().equals(LoginStatus.CUSTOMER) ) {
					tabbedPane.add(new CustomerPanel(Accessors.getCustomerAccessor(), theLoginPanel.getCustomer() ));
				}
				if( theLoginPanel.getLoginStatus().equals(LoginStatus.GAMETESTER) ) {
					tabbedPane.add(new GameTesterPanel(Accessors.getGameTesterAccessor(), theLoginPanel.getGameTester() ));
				}
			}
		 });
	}
}
   

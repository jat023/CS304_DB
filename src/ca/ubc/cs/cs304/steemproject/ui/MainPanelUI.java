package ca.ubc.cs.cs304.steemproject.ui;

import javax.swing.*;
import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.ui.panels.CustomerPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.GameTesterPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.LoginPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.PublicPanel;


public class MainPanelUI {
		
	/*
	 * The main function for building the GUI
	 * 				that is called from main in RunSteem.java
	 */
	public void buildGUI() {
		JFrame mainWindow = new JFrame();
		JTabbedPane tabbedPane = new JTabbedPane();
		//LoginPanel loginScreen = new LoginPanel(Accessors.getLoginAccessor());
		
		mainWindow.setSize(550,700);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabbedPane.add(new LoginPanel(Accessors.getLoginAccessor()));
		tabbedPane.add(new PublicPanel(Accessors.getPublicAccessor()));
		tabbedPane.add(new GameTesterPanel(Accessors.getGameTesterAccessor(), new GameTester(1, "gametester1@gmail.com", "Pass1")));
		tabbedPane.add(new CustomerPanel(Accessors.getCustomerAccessor(), new Customer(1, "customer1@gmail.com", "apple123")));

		mainWindow.add(tabbedPane);
		//mainWindow.add(loginScreen);

		mainWindow.setVisible(true);
	}
}
   

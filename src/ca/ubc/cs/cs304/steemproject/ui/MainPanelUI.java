package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import ca.ubc.cs.cs304.steemproject.ui.panels.CustomerPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.GameTesterPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.LoginPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.PublicPanel;

import java.sql.*;

public class MainPanelUI {
	
	/*
	 * The main function for building the GUI
	 * 				that is called from main in RunSteem.java
	 */
	public void buildGUI() {
		JFrame mainWindow = new JFrame();
		mainWindow.setSize(700,700);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new GridLayout(2,2));
		
		//mainPanel.add(new PublicPanel());
		//mainPanel.add(new CustomerPanel());
		//mainPanel.add(new GameTesterPanel());
		//mainPanel.add(new LoginPanel());

		mainWindow.add(mainPanel);
		mainWindow.setVisible(true);
	}
}
   

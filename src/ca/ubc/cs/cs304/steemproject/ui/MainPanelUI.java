package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ca.ubc.cs.cs304.steemproject.access.IPublicAccessor;
import ca.ubc.cs.cs304.steemproject.ui.panels.CustomerPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.GameTesterPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.LoginPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.PublicPanel;

import java.sql.*;

public class MainPanelUI implements ActionListener {
	
	/*
	 * The main function for building the GUI
	 * 				that is called from main in RunSteem.java
	 */
	public void buildGUI() {
		JFrame mainWindow = new JFrame();
		mainWindow.setSize(700,700);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new GridLayout(2,2));
		
		mainPanel.add(new PublicPanel());
		//mainPanel.add(new CustomerPanel());
		//mainPanel.add(new GameTesterPanel());
		//mainPanel.add(new LoginPanel());

		mainWindow.add(mainPanel);
		mainWindow.setVisible(true);
	}
	
	/*
	 * ActionEvent to listen for button changes in the GUI
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		//if (source == update) { updateAction(); }
		//if (source == delete) { deleteAction(); }
		//if (source == selQuery) { selQueryAction(); }
		//if (source == projQuery) { projQueryAction(); }
		//if (source == joinQuery) { joinQueryAction(); }
		//if (source == divQuery) { divQueryAction(); }
		//if (source == clear) { clearAction(); }
	}
	
//------------------------------------------------------------------------------------
//------------ Action functions for Queries ------------------------------------------	
//------------------------------------------------------------------------------------
	
	public void updateAction() {
		// TODO Auto_generated method stub
	}
	
	public void deleteAction() {
		// TODO Auto-generated method stub
	}
	
	public void selQueryAction() {
		// TODO Auto-generated method stub
	}
	
	public void projQueryAction() {
		// TODO Auto-generated method stub
	}
	
	public void joinQueryAction() {
		// TODO Auto-generated method stub
	}
	
	public void divQueryAction() {
		// TODO Auto-generated method stub
	}
	
	public void clearAction() {
		// TODO Auto-generated stub
	}
	
	public void findAction() {
		// TODO Auto-generated method stub
	}
}
   

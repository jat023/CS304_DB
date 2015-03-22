package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class ConsoleUI implements ActionListener {

	Statement state;
	ResultSet result;
	
	// Global variables for use for building and querying from UI	
	private JTextArea inputField = new JTextArea(5, 20);
	private ButtonGroup radioGroup = new ButtonGroup();
	private JRadioButton update = new JRadioButton("Update");
	private JRadioButton delete = new JRadioButton("Delete");
	private JRadioButton selQuery = new JRadioButton("SELECT");
	private JRadioButton projQuery = new JRadioButton("PROJECT");
	private JRadioButton joinQuery = new JRadioButton("JOIN");
	private JRadioButton divQuery = new JRadioButton("DIVIDE");
	/*
	private JButton save = new JButton("Save");
	private JButton newW = new JButton("New");
	private JButton find = new JButton("Find");
	*/
	private JButton clear = new JButton("Clear");
	
	
	/*
	 * The main function for building the GUI
	 * 				that is called from main in RunSteem.java
	 */
	public void buildGUI() {
		JFrame mainWindow = new JFrame();
		mainWindow.setSize(500,500);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel BG = new JPanel();
		
		// sets layout and buttons of our GUI panel
		BG.setLayout(new BorderLayout(5,5));
		BG.add(initButtons(), BorderLayout.SOUTH);
		BG.add(initTextfields(), BorderLayout.CENTER);
		
		// groups the radio buttons together (for clearing purposes)
		radioGroup.add(update);
		radioGroup.add(delete);
		radioGroup.add(selQuery);
		radioGroup.add(projQuery);
		radioGroup.add(divQuery);
		radioGroup.add(joinQuery);
		
		// Sets scroll for the text field (if the query ever gets that long)
		JScrollPane scrollPane = new JScrollPane(inputField);
		BG.setPreferredSize(new Dimension(200,200));
		BG.add(scrollPane, BorderLayout.CENTER);
		inputField.setLineWrap(true);

		mainWindow.add(BG);
		mainWindow.setVisible(true);
	}

	/*
	 * Initializes all JButton and JRadioButtons for adding to the main UI frame
	 * Adds action listeners for JButtons and JRadioButtons
	 */
	public JPanel initButtons() {
		JPanel BG = new JPanel();
		
		BG.add(clear);
		/*
		BG.add(find);
		BG.add(newW);
		BG.add(save);
		*/
		BG.add(update);
		BG.add(delete);
		BG.add(selQuery);
		BG.add(projQuery);
		BG.add(joinQuery);
		BG.add(divQuery);
		
		
		clear.addActionListener(this);
		/*
		find.addActionListener(this);
		newW.addActionListener(this);
		save.addActionListener(this);
		*/
		update.addActionListener(this);
		delete.addActionListener(this);
		selQuery.addActionListener(this);
		projQuery.addActionListener(this);
		joinQuery.addActionListener(this);
		divQuery.addActionListener(this);
		
		return BG;
	}
	
	/*
	 * Initializes all JTextfields for adding to the UI Frame
	 */
	public JPanel initTextfields() {
		JPanel BG = new JPanel();	
		BG.add(inputField);
		return BG;
	}
	
	/*
	 * Initializes all JLabels for adding to UI frame
	 */
	public JPanel initLabels() {
		JPanel BG = new JPanel();
		return BG;
	}
	
	/*
	 * ActionEvent to listen for button changes in the GUI
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == update) { updateAction(); }
		if (source == delete) { deleteAction(); }
		if (source == selQuery) { selQueryAction(); }
		if (source == projQuery) { projQueryAction(); }
		if (source == joinQuery) { joinQueryAction(); }
		if (source == divQuery) { divQueryAction(); }
		if (source == clear) { clearAction(); }
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
		String searchItem = inputField.getText().trim();
		
		try {
			 
		}
		catch (Exception X) {
			
		}
		
		inputField.setText("");
	}
	
	public void projQueryAction() {
		// TODO Auto-generated method stub
		String searchItem = inputField.getText().trim();
		
		try {
			 
		}
		catch (Exception X) {
			
		}
		
		inputField.setText("");
	}
	
	public void joinQueryAction() {
		// TODO Auto-generated method stub
		String searchItem = inputField.getText().trim();
		
		try {
			 
		}
		catch (Exception X) {
			
		}
		
		inputField.setText("");
	}
	
	public void divQueryAction() {
		// TODO Auto-generated method stub
		String searchItem = inputField.getText().trim();
		
		try {
			 
		}
		catch (Exception X) {
			
		}
		
		inputField.setText("");
	}
	
	public void clearAction() {
		inputField.setText("");
		radioGroup.clearSelection();
	}
	
	public void findAction() {
		// TODO Auto-generated method stub
	}
}
   

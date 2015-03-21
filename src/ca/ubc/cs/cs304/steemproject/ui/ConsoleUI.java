package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConsoleUI implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	// Global variables for use for building and querying from UI
	private JLabel userLabel = new JLabel("User");
	private JLabel emailLabel = new JLabel("Email");
	private JLabel passwordLabel = new JLabel("Password");		
	private JLabel gameLabel = new JLabel("Game");
	private JLabel descriptionLabel = new JLabel("Description");
	private JLabel genreLabel = new JLabel("Genre");
	private JLabel publisherLabel = new JLabel("Publisher");
	
	private JTextField userIDTextfield = new JTextField(30);
	private JTextField userEmailTextfield = new JTextField(30);
	private JTextField passwordTextfield = new JTextField(30);
	private JTextField gameTextfield = new JTextField(30);
	private JTextField descriptionTextfield = new JTextField(30);
	private JTextField genreTextfield = new JTextField(30);
	private JTextField publisherTextfield = new JTextField(30);
	
		// these may or may not get used
	private JButton next = new JButton("Next");
	private JButton prev = new JButton("Previous");
	private JButton first = new JButton("First");
	private JButton last = new JButton("Last");
	
	private JButton update = new JButton("Update");
	private JButton delete = new JButton("Delete");
	private JButton save = new JButton("Save");
	private JButton newW = new JButton("New");
	private JButton find = new JButton("Find");
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
		BG.setLayout(new BorderLayout(5,5));
		
		BG.add(initButtons(), BorderLayout.SOUTH);
		BG.add(initLabels(), BorderLayout.NORTH);
		BG.add(initTextfields(), BorderLayout.CENTER);

		mainWindow.add(BG);
		mainWindow.setVisible(true);
	}

	/*
	 * Initializes all JButtons for adding to the main UI frame
	 */
	public JPanel initButtons() {
		JPanel BG = new JPanel();
		
		BG.add(next);
		BG.add(prev);
		BG.add(first);
		BG.add(last);
		BG.add(clear);
		BG.add(find);
		BG.add(newW);
		BG.add(save);
		BG.add(update);
		BG.add(delete);
		
		next.addActionListener(this);
		prev.addActionListener(this);
		first.addActionListener(this);
		last.addActionListener(this);
		clear.addActionListener(this);
		find.addActionListener(this);
		newW.addActionListener(this);
		save.addActionListener(this);
		update.addActionListener(this);
		delete.addActionListener(this);
		
		return BG;
	}
	
	/*
	 * Initializes all JTextfields for adding to the UI Frame
	 */
	public JPanel initTextfields() {
		JPanel BG = new JPanel();	
		
		BG.add(userIDTextfield);
		BG.add(userEmailTextfield);
		BG.add(passwordTextfield);
		BG.add(gameTextfield);
		BG.add(descriptionTextfield);
		BG.add(genreTextfield);
		BG.add(publisherTextfield);
		
		return BG;
	}
	
	/*
	 * Initializes all JLabels for adding to UI frame
	 */
	public JPanel initLabels() {
		JPanel BG = new JPanel();
		
		BG.add(userLabel);
		BG.add(emailLabel);
		BG.add(passwordLabel);
		BG.add(gameLabel);
		BG.add(descriptionLabel);
		BG.add(genreLabel);
		BG.add(publisherLabel);
		
		return BG;
	}
	
	/*
	 * ActionEvent to listen for button changes in the GUI
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	
	public void udpateAction() {
		/*
		 try {
			 RESULT.updateInt("ID", Integer.parseInt(userIDTextfield.getUserID()));
			 RESULT.updateString("Email", userEmailTextfield.getEmail());
			 RESULT.updateString("Password", passwordTextfield.getPassword());
			 */
	}
	
	public void saveAction() {
		// TODO Auto-generated method stub
	}
	
	public void deleteAction() {
		// TODO Auto-generated method stub
	}
	
	public void newAction() {
		// TODO Auto-generated method stub
	}
	
	public void findAction() {
		// TODO Auto-generated method stub
	}
	
	public void clearAction() {
		// TODO Auto-generated method stub
	}
	
}
   

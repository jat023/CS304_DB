package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ConsoleUI implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	// Global variables for use for building and querying from UI
	JLabel userLabel = new JLabel("User");
	JLabel emailLabel = new JLabel("Email");
	JLabel passwordLabel = new JLabel("Password");		
	JLabel gameLabel = new JLabel("Game");
	JLabel descriptionLabel = new JLabel("Description");
	JLabel genreLabel = new JLabel("Genre");
	JLabel publisherLabel = new JLabel("Publisher");
	
	JTextField userIDTextfield = new JTextField(30);
	JTextField userEmailTextfield = new JTextField(30);
	JTextField passwordTextfield = new JTextField(30);
	JTextField gameTextfield = new JTextField(30);
	JTextField descriptionTextfield = new JTextField(30);
	JTextField genreTextfield = new JTextField(30);
	JTextField publisherTextfield = new JTextField(30);
	
	JButton next = new JButton("Next");
	JButton prev = new JButton("Previous");
	JButton first = new JButton("First");
	JButton last = new JButton("Last");
	JButton update = new JButton("Update");
	JButton delete = new JButton("Delete");
	JButton save = new JButton("Save");
	JButton newW = new JButton("New");
	JButton find = new JButton("Find");
	JButton clear = new JButton("Clear");
	
	/*
	 * The main function for building the GUI
	 * 				that is called from main in RunSteem.java
	 */
	public void buildGUI() {
		
		JFrame mainWindow = new JFrame();
		mainWindow.setSize(700,700);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel BG = new JPanel();
		
		BG.add(userLabel);
		BG.add(emailLabel);
		BG.add(passwordLabel);
		BG.add(gameLabel);
		BG.add(descriptionLabel);
		BG.add(genreLabel);
		BG.add(publisherLabel);
		
		BG.add(userIDTextfield);
		BG.add(userEmailTextfield);
		BG.add(passwordTextfield);
		BG.add(gameTextfield);
		BG.add(descriptionTextfield);
		BG.add(genreTextfield);
		BG.add(publisherTextfield);
		
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

		mainWindow.add(BG);
		mainWindow.setVisible(true);
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
   

package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import ca.ubc.cs.cs304.steemproject.main.RunSteem;

public class ConsoleUI extends JPanel implements IUI {

	private JTextField idField = new JTextField(10);
	private JTextField fnameField = new JTextField(30);
	private JTextField mnameField = new JTextField(30);
	private JTextField lnameField = new JTextField(30);
	private JTextField emailField = new JTextField(50);
	private JTextField phoneField = new JTextField(10);
	
	private JButton updateButton = new JButton ("updateButton");
	private JButton deleteButton = new JButton ("deleteButton");
	private JButton firstButton = new JButton ("firstButton");
	private JButton prevButton = new JButton ("prevButton");
	private JButton nextButton = new JButton ("nextButton");
	private JButton lastButton = new JButton ("lastButton");
	
	private RunSteem steemApp = new RunSteem();
	
	public void consoleUI() {
		setBorder(new TitledBorder(new EtchedBorder(), "Person Details"));
		setLayout(new BorderLayout(5, 5));
		add(initFields(), BorderLayout.NORTH);
		add(initButtons(), BorderLayout.CENTER);
		setFieldData(steemApp.moveFirst());
	}
	
	private JPanel initFields() {
	    JPanel panel = new JPanel();
	    panel.setLayout(new MigLayout());
	    panel.add(new JLabel("ID"), "align label");
	    panel.add(idField, "wrap");
	    idField.setEnabled(false);
	    
	    panel.add(new JLabel("First Name"), "align label");
	    panel.add(fnameField, "wrap");
	    panel.add(new JLabel("Last Name"), "align label");
	    panel.add(lnameField, "wrap");
	    panel.add(new JLabel("Middle Name"), "align label");
	    panel.add(mnameField, "wrap");
	    panel.add(new JLabel("ID"), "align label");
	    panel.add(idField, "wrap");
	    panel.add(new JLabel("Email Name"), "align label");
	    panel.add(emailField, "wrap");
	    panel.add(new JLabel("Phone"), "align label");
	    panel.add(phoneField, "wrap");
	    return panel;		
	}
	
	private JPanel initButtons() {
		JPanel panel = new JPanel();
	    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
	    panel.add(updateButton);
	    updateButton.addActionListener(new ButtonHandler());
	    panel.add(deleteButton);
	    deleteButton.addActionListener(new ButtonHandler());
	    panel.add(firstButton);
	    firstButton.addActionListener(new ButtonHandler());
	    panel.add(prevButton);
	    prevButton.addActionListener(new ButtonHandler());
	    panel.add(nextButton);
	    nextButton.addActionListener(new ButtonHandler());
	    panel.add(lastButton);
	    lastButton.addActionListener(new ButtonHandler());
	    return panel;
	}
	
	private RunSteem setFieldData() {
		
	}
	
	private RunSteem getFieldData() {
		
	}
	
    @Override
    public void printError(String errorMessage) {
        System.out.println(errorMessage);
    }

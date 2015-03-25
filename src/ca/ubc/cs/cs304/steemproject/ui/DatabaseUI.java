package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

public class DatabaseUI {
	
	JFrame mainWindow;
	JPanel optionsPanel;
	JPanel buttonPanel;
	JCheckBox option1check;
	JCheckBox option2check;
	JCheckBox option3check;
	JCheckBox option4check;
	JButton refreshButton;
	
	
	DatabaseUI() {
		mainWindow = new JFrame("Database UI");
		mainWindow.setSize(700,700);
		mainWindow.setLayout(null);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		optionsPanel = initCheckBoxesPanel();
		buttonPanel = initRefreshButton();
		//JTable table = new JTable(data, columnNames);
		
		mainWindow.add(optionsPanel);
		mainWindow.add(buttonPanel);
		
		setComponentLocations();
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(option1check.isSelected()) {
					System.out.printf("aa");
				}
				if(option2check.isSelected()) {
					System.out.printf("bb");
				}
				if(option3check.isSelected()) {
					System.out.printf("cc");
				}
				if(option4check.isSelected()) {
					System.out.printf("dd");
				}
			}
		});
	}
	
	private void setComponentLocations() {
		optionsPanel.setLocation(100, 25);
		buttonPanel.setLocation(225, 125);
	}
	
	private JPanel initRefreshButton() {
		JPanel aButtonPanel = new JPanel();
		aButtonPanel.setSize(250, 100);
		
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
	
	public static void main(String args[]) {
		DatabaseUI tablesUI = new DatabaseUI();
		tablesUI.show();
		
	}
}

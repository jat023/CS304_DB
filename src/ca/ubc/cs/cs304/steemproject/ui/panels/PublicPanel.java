package ca.ubc.cs.cs304.steemproject.ui.panels;

import javax.swing.*;
import javax.swing.border.LineBorder;

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.access.IPublicAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;

import java.awt.*;
import java.awt.event.*;

public class PublicPanel extends JPanel {
    
	private final IPublicAccessor iPublic;
	private final JTextField fGameNameField;
    private final JTextField fGameDeveloperField;
    private final JComboBox<Genre> fGameGenreField;
    private final JComboBox<GameSortByOption> fGameSortOptionField;
    private final JComboBox<SortDirection> fGameSortDirectionField;
	
	public PublicPanel(IPublicAccessor aPublicAccessor) {

		if (aPublicAccessor == null) {
			throw new IllegalArgumentException(
					"Public accessor cannot be null.");
		}

		iPublic = aPublicAccessor;
		setLayout(null);

		JLabel nameLabel = new JLabel("Game Title");
		nameLabel.setBounds(10, 10, 80, 25);
		this.add(nameLabel);

		fGameNameField = new JTextField("", 15);
		fGameNameField.setBounds(100, 10, 190, 25);
		this.add(fGameNameField);

		JLabel developerLabel = new JLabel("Developer");
		developerLabel.setBounds(10, 40, 80, 25);
		this.add(developerLabel);

		fGameDeveloperField = new JTextField("", 20);
		fGameDeveloperField.setBounds(100, 40, 190, 25);
		this.add(fGameDeveloperField);

		JLabel genreLabel = new JLabel("Genre");
		genreLabel.setBounds(10, 70, 80, 25);
		this.add(genreLabel);

		fGameGenreField = new JComboBox<Genre>(Genre.values());
		fGameGenreField.setBounds(100, 70, 190, 25);
		this.add(fGameGenreField);

		JLabel sortByLabel = new JLabel("Sort By");
		sortByLabel.setBounds(10, 100, 80, 25);
		this.add(sortByLabel);

		fGameSortOptionField = new JComboBox<GameSortByOption>(GameSortByOption.values());
		fGameSortOptionField.setBounds(100, 100, 100, 25);
		this.add(fGameSortOptionField);

		fGameSortDirectionField = new JComboBox<SortDirection>(SortDirection.values());
		fGameSortDirectionField.setBounds(210, 100, 80, 25);
		this.add(fGameSortDirectionField);
		
		JRadioButton discount = new JRadioButton("Disounted");
		discount.setBounds(10, 160, 150, 25);
		this.add(discount);
		
		JRadioButton owned = new JRadioButton("Owned");
		owned.setBounds(10, 190, 100, 25);
		this.add(owned);
		
		JLabel userLabel = new JLabel("User ID");
		userLabel.setBounds(10, 130, 110, 25);
		this.add(userLabel);
		
		JTextField userID = new JTextField(20);
		userID.setBounds(100, 130, 130, 25);
		this.add(userID);
		
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(10, 230, 280, 25);
		this.add(searchButton);
		
		JTextArea output = new JTextArea(10,10);
		output.setBounds(10, 270, 450, 300);
		this.add(output);
	}

	public static final void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new PublicPanel(Accessors.getPublicAccessor()));
		frame.setSize(500,650);
		frame.setVisible(true);
	}
}

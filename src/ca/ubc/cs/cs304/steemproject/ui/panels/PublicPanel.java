package ca.ubc.cs.cs304.steemproject.ui.panels;

import javax.swing.*;
import java.util.List;

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.access.IPublicAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

import java.awt.event.*;

@SuppressWarnings("serial")
public class PublicPanel extends JPanel {
    
	private final IPublicAccessor iPublic;
	private final JTextField fGameNameField;
    private final JTextField fGameDeveloperField;
    private final JComboBox<Genre> fGameGenreField;
    private final JComboBox<GameSortByOption> fGameSortOptionField;
    private final JComboBox<SortDirection> fGameSortDirectionField;
    private final JRadioButton discount = new JRadioButton("Discounted");
    private final JRadioButton owned = new JRadioButton("Owned");
    private final JTextField userID = new JTextField(25);
    private final JTextArea output = new JTextArea(10,10);
	
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
		
		discount.setBounds(10, 160, 150, 25);
		this.add(discount);
	
		owned.setBounds(10, 190, 100, 25);
		this.add(owned);
		
		JLabel userLabel = new JLabel("User ID");
		userLabel.setBounds(10, 130, 110, 25);
		this.add(userLabel);
		
		userID.setBounds(100, 130, 130, 25);
		this.add(userID);

		output.setBounds(10, 270, 450, 300);
		output.setLineWrap(true);
		output.setEditable(false);
		this.add(output);
		
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(10, 230, 280, 25);
		this.add(searchButton);
		
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String gameName = fGameNameField.getText();
				Genre gameGenre = (Genre)fGameGenreField.getSelectedItem();	
				String gameDeveloper = fGameDeveloperField.getText();
				SortDirection sortDirection = (SortDirection)fGameSortDirectionField.getSelectedItem();
				GameSortByOption sortField = (GameSortByOption)fGameSortOptionField.getSelectedItem();
				boolean discounted = discount.isSelected();
				boolean isOwned = owned.isSelected();
				String storeUserID = userID.getText();
								
				if (!isOwned && !discounted) {
					List<FinalizedGame> storeGeneralList = iPublic.listPurchasableGames(
						gameName, gameGenre, gameDeveloper, null, null, sortField, sortDirection, discounted);
				}
				
				else if (discounted) {
					List<FinalizedGame> storeGeneralList = iPublic.listPurchasableGames(
							gameName, gameGenre, gameDeveloper, null, null, sortField, sortDirection, discounted);
				}
				else if (isOwned) {
					try {
						List<Playtime> storeOwnedList = iPublic.listGamesOwned(storeUserID);
					} catch (UserNotExistsException e) {
						JOptionPane.showMessageDialog(null,
								"No game tester with this email exists.",
								"LOGIN FAILED",
								JOptionPane.INFORMATION_MESSAGE);
					}
					try {
						List<Playtime> storeOwnedList = iPublic.listGamesOwned(storeUserID);
					} catch (UserNotExistsException e) {
						JOptionPane.showMessageDialog(null,
								"No game tester with this email exists.",
								"LOGIN FAILED",
								JOptionPane.INFORMATION_MESSAGE);
					}
					try {
						List<Playtime> storeOwnedList = iPublic.listGamesOwned(
								storeUserID, gameName, gameGenre, gameDeveloper, sortField, sortDirection);
					} catch (UserNotExistsException e) {
						JOptionPane.showMessageDialog(null,
								"No game tester with this email exists.",
								"LOGIN FAILED",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			
		});
		

	}

	public static final void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new PublicPanel(Accessors.getPublicAccessor()));
		frame.setSize(500,650);
		frame.setVisible(true);
	}
}

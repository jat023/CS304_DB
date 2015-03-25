package ca.ubc.cs.cs304.steemproject.ui.panels;

import javax.swing.*;

import java.util.ArrayList;
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
    private final JTextField userIDField = new JTextField(25);
    private final JTextArea output = new JTextArea(10,10);
    private final ButtonGroup radioButtonGroup = new ButtonGroup();
	
	public PublicPanel(IPublicAccessor aPublicAccessor) {

		if (aPublicAccessor == null) {
			throw new IllegalArgumentException(
					"Public accessor cannot be null.");
		}

		iPublic = aPublicAccessor;
		setLayout(null);

//--------SET FIELDS, BUTTONS, LABELS, ETC with absolute positioning --------------------------------
//---------------------------------------------------------------------------------------------------
		JLabel nameLabel = new JLabel("Game Title");
		nameLabel.setBounds(10, 10, 80, 25);
		this.add(nameLabel);
		
				// the setBounds() API for each field or button is
					///					setBounds(int x-coord, int y-coord, int length, int height)
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
		fGameGenreField.addItem(null);
		fGameGenreField.setSelectedIndex(-1);
		this.add(fGameGenreField);

		JLabel sortByLabel = new JLabel("Sort By");
		sortByLabel.setBounds(10, 100, 80, 25);
		this.add(sortByLabel);

		fGameSortOptionField = new JComboBox<GameSortByOption>(GameSortByOption.values());
		fGameSortOptionField.setBounds(100, 100, 100, 25);
		fGameSortOptionField.addItem(null);
		fGameSortOptionField.setSelectedIndex(-1);
		this.add(fGameSortOptionField);

		fGameSortDirectionField = new JComboBox<SortDirection>(SortDirection.values());
		fGameSortDirectionField.setBounds(210, 100, 80, 25);
		fGameSortDirectionField.addItem(null);
		fGameSortDirectionField.setSelectedIndex(-1);
		this.add(fGameSortDirectionField);
		
		discount.setBounds(10, 160, 150, 25);
		this.add(discount);
	
		owned.setBounds(10, 190, 100, 25);
		this.add(owned);
		
		JLabel userLabel = new JLabel("User ID");
		userLabel.setBounds(10, 130, 110, 25);
		this.add(userLabel);
		
		userIDField.setBounds(100, 130, 130, 25);
		this.add(userIDField);

		output.setBounds(10, 270, 450, 300);
		output.setLineWrap(true);
		output.setEditable(false);
		this.add(output);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(300, 230, 70, 25);
		this.add(clearButton);
		
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(10, 230, 280, 25);
		this.add(searchButton);
		
		radioButtonGroup.add(discount);
		radioButtonGroup.add(owned);
		
//--------Add ActionListeners for events to the clear and search buttons --------------------------
//-------------------------------------------------------------------------------------------------
		
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.setText("");
				radioButtonGroup.clearSelection();
				fGameSortOptionField.setSelectedIndex(-1);
				fGameSortDirectionField.setSelectedIndex(-1);
				fGameGenreField.setSelectedIndex(-1);
				userIDField.setText("");
				fGameNameField.setText("");
				fGameDeveloperField.setText("");
			}
		});
		
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
					// Retrieve all the variables from the JTextFields created earlier
				String gameName = fGameNameField.getText().equals("") ? null : fGameNameField.getText();	
				String gameDeveloper = fGameDeveloperField.getText().equals("") ? null : fGameDeveloperField.getText();
				boolean discounted = discount.isSelected();
				boolean isOwned = owned.isSelected();
				String storeUserID = userIDField.getText().equals("") ? null : userIDField.getText();
                
				if (!isOwned && !discounted) {
					List<FinalizedGame> storeGeneralList = new ArrayList<FinalizedGame>();
					
					storeGeneralList = iPublic.listPurchasableGames(
							gameName,
							(Genre)fGameGenreField.getSelectedItem(),
							gameDeveloper, null, null,
							(GameSortByOption)fGameSortOptionField.getSelectedItem(),
							(SortDirection)fGameSortDirectionField.getSelectedItem(),
							discounted);
				
					if (storeGeneralList.size() == 0) {
						output.append("There are currently no games available for purchase.");
						output.append("\n");
					}
					else {
						output.append("Here are the games available for purchase:\n");
				
						for (int i = 0; i < storeGeneralList.size(); i++) {
							output.append(storeGeneralList.get(i).getName().toString() + "\n");
						}
						
						output.append("\n");
					}
				}
				
				if (discounted) {
					List<FinalizedGame> storeDiscountedList = new ArrayList<FinalizedGame>();
					
					storeDiscountedList = iPublic.listPurchasableGames(
							gameName,
							(Genre)fGameGenreField.getSelectedItem(),
							gameDeveloper, null, null,
							(GameSortByOption)fGameSortOptionField.getSelectedItem(),
							(SortDirection)fGameSortDirectionField.getSelectedItem(), discounted);
					
					if (storeDiscountedList.size() == 0) {
						output.append("There are currently no games on discount.");
						output.append("\n");
					}
					else {
						output.append("Here are the list of discounted games:");
						output.append("\n");
						
						for (int i = 0; i < storeDiscountedList.size(); i++) {
							output.append(storeDiscountedList.get(i).getName().toString());
						}
						
						output.append("\n");
					}
				}
				
				if (isOwned) {
					try {
						List<Playtime> storeOwnedList = new ArrayList<Playtime>();
						
						storeOwnedList = iPublic.listGamesOwned(storeUserID);
						
						output.append(storeUserID + ", here are the games in your library:\n");
						
						for (int i = 0; i < storeOwnedList.size(); i++) {
							output.append(storeOwnedList.get(i).getGame().getName().toString() + "\n");
						}
						
						output.append("\n");
						
					} catch (UserNotExistsException e) {
						JOptionPane.showMessageDialog(null,
								"No user with this email exists.",
								"ERROR",
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

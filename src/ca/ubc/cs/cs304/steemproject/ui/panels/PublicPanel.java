package ca.ubc.cs.cs304.steemproject.ui.panels;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.access.IPublicAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

import java.awt.Font;
import java.awt.event.*;

@SuppressWarnings("serial")
public class PublicPanel extends JPanel {
    
	private final IPublicAccessor iPublic;
	private final JTextField fGameNameField;
    private final JTextField fGameDeveloperField;
    private final JComboBox<Genre> fGameGenreField;
    private final JComboBox<GameSortByOption> fGameSortOptionField;
    private final JComboBox<SortDirection> fGameSortDirectionField;
    private final JRadioButton discount = new JRadioButton("Discounted games");
    private final JRadioButton owned = new JRadioButton("Owned by you");
    private final JRadioButton ownedByAll = new JRadioButton("Owned by all");
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
		JLabel searchLabel = new JLabel("SEARCH FOR AVAILABLE GAMES");
        searchLabel.setFont(new Font("Serif", Font.BOLD, 20));
        searchLabel.setHorizontalAlignment(JLabel.LEFT);
        searchLabel.setBounds(10, 10, 280, 25);
        this.add(searchLabel);
		
		JLabel nameLabel = new JLabel("Game Title");
		nameLabel.setBounds(10, 40, 80, 25);
		this.add(nameLabel);
		
		fGameNameField = new JTextField("", 15);
		fGameNameField.setBounds(100, 40, 190, 25);
		this.add(fGameNameField);

		JLabel developerLabel = new JLabel("Developer");
		developerLabel.setBounds(10, 70, 80, 25);
		this.add(developerLabel);

		fGameDeveloperField = new JTextField("", 20);
		fGameDeveloperField.setBounds(100, 70, 190, 25);
		this.add(fGameDeveloperField);

		JLabel genreLabel = new JLabel("Genre");
		genreLabel.setBounds(10, 100, 80, 25);
		this.add(genreLabel);

		fGameGenreField = new JComboBox<Genre>(Genre.values());
		fGameGenreField.setBounds(100, 100, 190, 25);
		fGameGenreField.addItem(null);
		fGameGenreField.setSelectedIndex(-1);
		this.add(fGameGenreField);

		JLabel sortByLabel = new JLabel("Sort By");
		sortByLabel.setBounds(10, 130, 80, 25);
		this.add(sortByLabel);

		fGameSortOptionField = new JComboBox<GameSortByOption>(GameSortByOption.values());
		fGameSortOptionField.setBounds(100, 130, 100, 25);
		fGameSortOptionField.addItem(null);
		fGameSortOptionField.setSelectedIndex(-1);
		this.add(fGameSortOptionField);

		fGameSortDirectionField = new JComboBox<SortDirection>(SortDirection.values());
		fGameSortDirectionField.setBounds(210, 130, 80, 25);
		fGameSortDirectionField.addItem(null);
		fGameSortDirectionField.setSelectedIndex(-1);
		this.add(fGameSortDirectionField);
		
		discount.setBounds(10, 160, 150, 25);
		this.add(discount);
	
		owned.setBounds(10, 190, 150, 25);
		this.add(owned);
		
		ownedByAll.setBounds(10, 220, 150, 25);
		this.add(ownedByAll);
		
		JLabel userLabel = new JLabel("User ID");
		userLabel.setBounds(160, 192, 60, 25);
		this.add(userLabel);
		
		userIDField.setBounds(220, 190, 130, 25);
		this.add(userIDField);

		output.setBounds(10, 330, 450, 300);
		output.setLineWrap(true);
		output.setEditable(false);
		this.add(output);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(300, 300, 70, 25);
		this.add(clearButton);
		
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(10, 300, 280, 25);
		this.add(searchButton);
		
		JButton mostPopular = new JButton("Most Popular");
		mostPopular.setBounds(330, 40, 120, 25);
		this.add(mostPopular);
		
		radioButtonGroup.add(discount);
		radioButtonGroup.add(owned);
		radioButtonGroup.add(ownedByAll);
		
//--------Add ActionListeners for events  --------------------------
//------------------------------------------------------------------
		
		mostPopular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Collection<FinalizedGame> mostPopularGame = iPublic.findMostPopularGame();
				
				for (Object obj : mostPopularGame) {
					output.append(mostPopularGame.toString() + "\n");
				}
			}	
		});
		
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
		
        final JTable table = new JTable();
        final JDialog dialog = new JDialog();
        dialog.setSize(720, 420);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        dialog.add(scrollPane);
		
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
					// Retrieve all the variables from the JTextFields created earlier
				String gameName = fGameNameField.getText().equals("") ? null : fGameNameField.getText();	
				String gameDeveloper = fGameDeveloperField.getText().equals("") ? null : fGameDeveloperField.getText();
				boolean discounted = discount.isSelected();
				boolean isOwned = owned.isSelected();
				boolean isOwnedByAll = ownedByAll.isSelected();
				String storeUserID = userIDField.getText().equals("") ? null : userIDField.getText();
                
				if (discounted) {
					List<FinalizedGame> storeDiscountedList = new ArrayList<FinalizedGame>();
					
					storeDiscountedList = iPublic.listPurchasableGames(
							gameName,
							(Genre)fGameGenreField.getSelectedItem(),
							gameDeveloper, null, null,
							(GameSortByOption)fGameSortOptionField.getSelectedItem(),
							(SortDirection)fGameSortDirectionField.getSelectedItem(), discounted);
					
	                table.setModel(new FeedbackTableModel(storeDiscountedList));
	                dialog.setVisible(true);
				}
				else if (isOwned) {
					try {
						List<Playtime> storeOwnedList = new ArrayList<Playtime>();
						
						storeOwnedList = iPublic.listGamesOwned(storeUserID);
						
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
				else if (isOwnedByAll) {
					Collection<FinalizedGame> ownedAll = iPublic.findGamesOwnedByAllCustomers();
					
					for (Object obj : ownedAll) {
						output.append(ownedAll.toString() + "\n");
					}
				}
				else {
					List<FinalizedGame> storeGeneralList = new ArrayList<FinalizedGame>();
					
					storeGeneralList = iPublic.listPurchasableGames(
							gameName,
							(Genre)fGameGenreField.getSelectedItem(),
							gameDeveloper, null, null,
							(GameSortByOption)fGameSortOptionField.getSelectedItem(),
							(SortDirection)fGameSortDirectionField.getSelectedItem(),
							discounted);
					
	                table.setModel(new FeedbackTableModel(storeGeneralList));
	                dialog.setVisible(true);
				}
			}
			
		});
	}
	

    // table class for displaying the games
    private static class FeedbackTableModel extends AbstractTableModel {

        private static final String[] COLUMN_NAMES = {"Game Title", "Description", "Developer",
        												"Genre", "Rating", "Full Price", "Sale Price", "Discount (%)"};
        private final List<FinalizedGame> fGameList;
        
        public FeedbackTableModel(List<FinalizedGame> aGameList) {
        	fGameList = aGameList;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public int getRowCount() {
            return fGameList.size();
        }
        
        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            switch (columnIndex) {
            case 0:
                return fGameList.get(rowIndex).getName();
            case 1:
                return fGameList.get(rowIndex).getDescription();
            case 2:
                return fGameList.get(rowIndex).getDeveloper();
            case 3:
                return fGameList.get(rowIndex).getGenre();
            case 4:
            	return fGameList.get(rowIndex).getRating();
            case 5:
            	return fGameList.get(rowIndex).getFullPrice();
            case 6:
            	return fGameList.get(rowIndex).getSalePrice();
            case 7:
            	return fGameList.get(rowIndex).getDiscountPercentage();
            default:
                throw new IllegalArgumentException("Column index higher than anticipated.");
            }
        }

    }
}

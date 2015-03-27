package ca.ubc.cs.cs304.steemproject.ui.panels;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private final JRadioButton discount = new JRadioButton("Discounted only");
    private final JRadioButton owned = new JRadioButton("Owned by user");
    private final JTextField userField = new JTextField(25);
    private final JTextArea output = new JTextArea(10,10);
    private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public PublicPanel(IPublicAccessor aPublicAccessor) {

		if (aPublicAccessor == null) {
			throw new IllegalArgumentException(
					"Public accessor cannot be null.");
		}

		iPublic = aPublicAccessor;
		setLayout(null);
		
		JLabel searchLabel = new JLabel("SEARCH FOR PURCHASABLE GAMES");
        searchLabel.setFont(new Font("Serif", Font.BOLD, 20));
        searchLabel.setHorizontalAlignment(JLabel.LEFT);
        searchLabel.setBounds(10, 10, 450, 25);
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
		
		discount.setBounds(10, 160, 170, 25);
		this.add(discount);
	
		owned.setBounds(10, 190, 150, 25);
		this.add(owned);
		
		JLabel userLabel = new JLabel("User Email/ID");
		userLabel.setBounds(160, 192, 120, 25);
		this.add(userLabel);
		
		userField.setBounds(260, 190, 130, 25);
		this.add(userField);

		output.setBounds(10, 330, 450, 200);
		output.setLineWrap(true);
		output.setEditable(false);
		this.add(output);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(300, 300, 100, 25);
		this.add(clearButton);
		
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(10, 300, 280, 25);
		this.add(searchButton);
		
			// aggregation query
		JButton mostPopular = new JButton("Most Popular");
		mostPopular.setBounds(330, 40, 150, 25);
		this.add(mostPopular);
		
			// Nested aggregation with group-by query
		JButton mostExpensive = new JButton("Most Expensive");
		mostExpensive.setBounds(330, 70, 150,25);
		this.add(mostExpensive);
		
		JButton leastExpensive = new JButton("Least Expensive");
		leastExpensive.setBounds(330, 100, 150,25);
		this.add(leastExpensive);
		
		JButton ownedByAll = new JButton("Owned By All");
		ownedByAll.setBounds(330, 130, 150,25);
        this.add(ownedByAll);
        
        buttonGroup.add(discount);
        buttonGroup.add(owned);
		
		
//--------Add ActionListeners for events  --------------------------
//------------------------------------------------------------------
		
		mostExpensive.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Pair<Genre,Float> game = iPublic.findMostExpensiveGenre();
				
				output.append("The most expensive genre is ");
				output.append(game.getKey().toString());
				output.append(" at an average price of ");
				output.append(game.getValue().toString());
				output.append(".\n");
			}	
		});
		
		leastExpensive.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Pair<Genre,Float> game = iPublic.findLeastExpensiveGenre();
				
				output.append("The least expensive genre is ");
				output.append(game.getKey().toString());
				output.append(" at an average price of ");
				output.append(game.getValue().toString());
				output.append(".\n");
			}	
		});
		
		mostPopular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			    JTable table = new JTable();
                JDialog dialog = new JDialog();
                dialog.setSize(720, 420);
                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                dialog.add(scrollPane);
                
				Collection<FinalizedGame> mostPopularGame = iPublic.findMostPopularGame();
			
				output.append("These games are most popular: \n");
				for (FinalizedGame obj : mostPopularGame) {
					output.append(obj.getName().toString() + "\n");
				}
				
				table.setModel(new DisplayGamesTableModel(new ArrayList<FinalizedGame>(mostPopularGame)));
                dialog.setVisible(true);
			}	
		});
		
		ownedByAll.addActionListener(new ActionListener() {
            
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		        
		        JTable table = new JTable();
		        JDialog dialog = new JDialog();
		        dialog.setSize(720, 420);
		        JScrollPane scrollPane = new JScrollPane(table);
		        table.setFillsViewportHeight(true);
		        dialog.add(scrollPane);
		        
		        Collection<FinalizedGame> ownedAll = iPublic.findGamesOwnedByAllCustomers();

		        output.append("These games are owned by all customers: \n");
		        for (FinalizedGame obj : ownedAll) {
		            output.append(obj.getName().toString() + "\n");
		        }
		        
		        table.setModel(new DisplayGamesTableModel(new ArrayList<FinalizedGame>(ownedAll)));
                dialog.setVisible(true);

		    }
        });
		
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.setText("");
				buttonGroup.clearSelection();
				fGameSortOptionField.setSelectedIndex(-1);
				fGameSortDirectionField.setSelectedIndex(-1);
				fGameGenreField.setSelectedIndex(-1);
				userField.setText("");
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
        
        final JTable ownedTable = new JTable();
        final JDialog ownedDialog = new JDialog();
        ownedDialog.setSize(720, 420);
        JScrollPane ownedScrollPane = new JScrollPane(ownedTable);
        ownedTable.setFillsViewportHeight(true);
        ownedDialog.add(ownedScrollPane);
		
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String gameName = fGameNameField.getText().equals("") ? null : fGameNameField.getText();	
				String gameDeveloper = fGameDeveloperField.getText().equals("") ? null : fGameDeveloperField.getText();
				boolean discounted = discount.isSelected();
				boolean isOwned = owned.isSelected();
				//String storeUserID = userIDField.getText().equals("") ? null : userIDField.getText();
				String storeUserEmail;
				int storeUserID;
				
				if (isOwned) {
				    
				    boolean useId;
				    
				    try {
				        storeUserID = Integer.parseInt(userField.getText());
				        useId = true;
				    } catch (NumberFormatException e) {
				        useId = false;
				    }
				    
					if (useId) {
						storeUserID = Integer.parseInt(userField.getText());
						
						try {
							List<Playtime> storeOwnedList = new ArrayList<Playtime>();
							
							storeOwnedList = iPublic.listGamesOwned(storeUserID);
							
			                ownedTable.setModel(new ownedGamesTableModel(storeOwnedList));
			                ownedDialog.setVisible(true);
							
						} catch (UserNotExistsException e) {
							JOptionPane.showMessageDialog(null,
									"No user with this ID exists.",
									"ERROR",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else {
						storeUserEmail = userField.getText().equals("") ? null : userField.getText();
						
						try {
							List<Playtime> storeOwnedList = new ArrayList<Playtime>();
							
							storeOwnedList = iPublic.listGamesOwned(storeUserEmail);
							
			                ownedTable.setModel(new ownedGamesTableModel(storeOwnedList));
			                ownedDialog.setVisible(true);
							
						} catch (UserNotExistsException e) {
							JOptionPane.showMessageDialog(null,
									"No user with this email exists.",
									"ERROR",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				
				else if (discounted) {
					List<FinalizedGame> storeDiscountedList = new ArrayList<FinalizedGame>();
					
					storeDiscountedList = iPublic.listPurchasableGames(
							gameName,
							(Genre)fGameGenreField.getSelectedItem(),
							gameDeveloper, null, null,
							(GameSortByOption)fGameSortOptionField.getSelectedItem(),
							(SortDirection)fGameSortDirectionField.getSelectedItem(), discounted);
					
	                table.setModel(new DisplayGamesTableModel(storeDiscountedList));
	                dialog.setVisible(true);
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
					
	                table.setModel(new DisplayGamesTableModel(storeGeneralList));
	                dialog.setVisible(true);
				}
			}
			
		});
	}
	

    // table class for displaying the all and discounted games
    private static class DisplayGamesTableModel extends AbstractTableModel {

        private static final String[] COLUMN_NAMES = {"Game Title", "Description", "Developer",
        												"Genre", "Rating", "Full Price", "Sale Price", "Discount (%)"};
        private final List<FinalizedGame> fGameList;
        
        public DisplayGamesTableModel(List<FinalizedGame> aGameList) {
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
    
    // table class for displaying the games owned by a certain given customers
    private static class ownedGamesTableModel extends AbstractTableModel {

        private static final String[] COLUMN_NAMES = {"Game Title", "Owned by", "Hours Played"};
        private final List<Playtime> fGameList;
        
        public ownedGamesTableModel(List<Playtime> aGameList) {
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
                return fGameList.get(rowIndex).getGame().getName();
            case 1:
                return fGameList.get(rowIndex).getUser().getEmail();
            case 2:
                return fGameList.get(rowIndex).getHoursSpent();
            default:
                throw new IllegalArgumentException("Column index higher than anticipated.");
            }
        }
    }
}

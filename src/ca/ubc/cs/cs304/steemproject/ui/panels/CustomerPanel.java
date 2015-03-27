package ca.ubc.cs.cs304.steemproject.ui.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import ca.ubc.cs.cs304.steemproject.access.ICustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.Retrieves;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.UserHasExistingCreditCards;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.ui.panels.datepicker.DateLabelFormatter;

@SuppressWarnings("serial")
public class CustomerPanel extends JPanel {

	private final ICustomerAccessor fCustomerAccessor;
    private final Customer fCustomer;
    
    private final JTextArea output = new JTextArea(10,10);
    private final JTextField fCreditCardNumberField = new JTextField(50);
    private final JTextField fCcvField = new JTextField(5);
    private final JTextField fAddressField = new JTextField(50);
    private final JDatePickerImpl fEarliestDatePicker;
    private final JDatePickerImpl fLatestDatePicker;
    
    private final JTextField purchaseGameField = new JTextField(30);
    private final JTextField purchaseCardField = new JTextField(30);
    
    public CustomerPanel(ICustomerAccessor aCustomerAccessor, Customer aCustomer) {
        
        if (aCustomerAccessor == null) {
            throw new IllegalArgumentException("Customer accessor cannot be null.");
        }
        if (aCustomer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        
        fCustomerAccessor = aCustomerAccessor;
        fCustomer = aCustomer;
        
        setLayout(null);
 
        	// MAIN TITLE
        JLabel titleLabel = new JLabel("Customer Account Options");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setBounds(10, 10, 330, 25);
        this.add(titleLabel);
        
        	// UPDATE CREDIT CARD INFORMATION
        JLabel creditCardLabel = new JLabel("Update Credit Card Information");
        creditCardLabel.setFont(new Font("Serif", Font.BOLD, 12));
        creditCardLabel.setHorizontalAlignment(JLabel.LEFT);
        creditCardLabel.setBounds(10, 40, 280, 25);
        this.add(creditCardLabel);
        
        JButton addCreditCardButton = new JButton("Add Credit Card");
        addCreditCardButton.setBounds(10,70,180,25);
        this.add(addCreditCardButton);
        
        
        JLabel numberLabel = new JLabel("Card num.");
        numberLabel.setBounds(10, 100, 130, 25);
        this.add(numberLabel);
        fCreditCardNumberField.setBounds(100,100,200,25);
        this.add(fCreditCardNumberField);
        
        JLabel cvvCreditCardLabel = new JLabel("CCV");
        cvvCreditCardLabel.setBounds(10, 130, 50, 25);
        this.add(cvvCreditCardLabel);
        
        fCcvField.setBounds(100, 130, 50, 25);
        this.add(fCcvField);
        
        JLabel addressWithCard = new JLabel("Address");
        addressWithCard.setBounds(10, 160, 100, 25);
        this.add(addressWithCard);
        
        fAddressField.setBounds(100, 160, 200, 25);
        this.add(fAddressField);
        
        JButton searchButton = new JButton("List Cards");
        searchButton.setBounds(20, 200, 280, 25);
        this.add(searchButton);
        
        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(320,200,100,25);
        this.add(clearButton);
       
        JButton deleteAccountButton = new JButton("Delete account");
        deleteAccountButton.setBounds(350,10,150,25);
        deleteAccountButton.setHorizontalAlignment(JButton.RIGHT);
        this.add(deleteAccountButton);
        
		output.setBounds(10, 240, 450, 100);
		this.add(output);
		output.setLineWrap(true);
		output.setEditable(false);
		
        	// SEE TRANSACTION HISTORY
        JLabel historyLabel = new JLabel("See transaction history:");
        historyLabel.setFont(new Font("Serif", Font.BOLD, 12));
        historyLabel.setHorizontalAlignment(JLabel.LEFT);
        historyLabel.setBounds(10, 360, 280, 25);
        this.add(historyLabel);
        
        JButton historyButton = new JButton("Transaction History");
        historyButton.setBounds(10, 390, 280, 25);
        this.add(historyButton); 
        
        	// MAKE PURCHASES
        JLabel purchaseLabel = new JLabel("Make a purchase (enter game name and credit card number):");
        purchaseLabel.setFont(new Font("Serif", Font.BOLD, 12));
        purchaseLabel.setHorizontalAlignment(JLabel.LEFT);
        purchaseLabel.setBounds(10, 490, 350, 25);
        this.add(purchaseLabel);
     
        JButton purchaseButton = new JButton("Purchase");
        purchaseButton.setBounds(10, 520, 80, 25);
        this.add(purchaseButton);
        
        JLabel purchaseGameName = new JLabel("Game name:");
        purchaseGameName.setBounds(100, 520, 120, 25);
        this.add(purchaseGameName);
        
        JLabel buyWithThisCreditCard = new JLabel("Credit Card:");
        buyWithThisCreditCard.setBounds(100, 550, 120, 25);
        this.add(buyWithThisCreditCard);
        
        purchaseGameField.setBounds(190, 520, 200, 25);
        this.add(purchaseGameField);
        purchaseCardField.setBounds(190, 550, 200, 25);
        this.add(purchaseCardField);
        
        // clear all field with the clear button
        clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
		        fCreditCardNumberField.setText("");
		        fCcvField.setText("");
		        fAddressField.setText("");
		        output.setText("");
			}
        });
        
		// purchase a game with a given credit card and game name
		purchaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
		
				try {
					fCustomerAccessor.purchaseGame(fCustomer,
							Retrieves.retrieveCreditCard(purchaseCardField.getText()),
							Retrieves.retrieveFinalizedGame(purchaseGameField.getText()));
					JOptionPane.showMessageDialog(null,
							"Congratulations, you've purchased a game!",
							"GAME PURCHASED",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (UserNotExistsException | GameNotExistException e) {
					JOptionPane.showMessageDialog(null,
							"Invalid entries",
							"FAILED TO PURCHASE GAME",
							JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null,
							"Invalid entries",
							"FAILED TO PURCHASE GAME",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// Deletes the current user account from the database
		deleteAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fCustomerAccessor.removeAccount(fCustomer);
					JOptionPane.showMessageDialog(null,
							"Current user deleted",
							"ACCOUNT REMOVED",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (UserNotExistsException e1) {
					JOptionPane.showMessageDialog(null,
							"No user exists",
							"FAILED TO REMOVE ACCOUNT",
							JOptionPane.ERROR_MESSAGE);
				} catch (UserHasExistingCreditCards e1) {
				    JOptionPane.showMessageDialog(null,
                            "User has existing credit cards.",
                            "FAILED TO REMOVE ACCOUNT",
                            JOptionPane.ERROR_MESSAGE);
                }
			}
		});
		
        final JTable creditCardTable = new JTable();
        final JDialog creditCardDialog = new JDialog();
        creditCardDialog.setSize(720, 420);
        JScrollPane creditCardScrollPane = new JScrollPane(creditCardTable);
        creditCardTable.setFillsViewportHeight(true);
        creditCardDialog.add(creditCardScrollPane);
        
		// Queries for the list of cards owned by the user
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<CreditCard> creditCards = new ArrayList<CreditCard>();
				
				try {
					creditCards = fCustomerAccessor.listCreditCards(fCustomer);
				} catch (UserNotExistsException e) {
					JOptionPane.showMessageDialog(null,
							"No user exists",
							"FAILED TO GET CREDIT CARDS",
							JOptionPane.ERROR_MESSAGE);
				}
			
				new DisplayCreditCard(fCustomerAccessor, creditCards, fCustomer);
				
			}
		});
		
		// Adds a card to the current user
		addCreditCardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String cardToBeAdded = fCreditCardNumberField.getText();
				String cvvOfCardToAdd = fCcvField.getText();
				String addressWithCard = fAddressField.getText();
				
				CreditCard addThisCard = new CreditCard(fCustomer, cardToBeAdded, cvvOfCardToAdd, addressWithCard);
				
				try {
					fCustomerAccessor.addNewCreditCard(addThisCard);
					JOptionPane.showMessageDialog(null,cardToBeAdded, 
							"Card added!", JOptionPane.INFORMATION_MESSAGE);
				} catch (UserNotExistsException e) {
					JOptionPane.showMessageDialog(null,
							"No user exists",
							"ADD CARD FAILED",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// labels and setup for the date pickers
		JLabel afterLabel = new JLabel("Earliest");
        afterLabel.setBounds(10, 420, 80, 25);
        this.add(afterLabel);

        JLabel beforeLabel = new JLabel("Latest");
        beforeLabel.setBounds(10, 450, 80, 25);
        this.add(beforeLabel);

        UtilDateModel afterModel = new UtilDateModel();
        Properties afterProperties = new Properties();
        afterProperties.put("text.today", "Today");
        afterProperties.put("text.month", "Month");
        afterProperties.put("text.year", "Year");
        JDatePanelImpl afterDatePanel = new JDatePanelImpl(afterModel, afterProperties);
        fEarliestDatePicker = new JDatePickerImpl(afterDatePanel, new DateLabelFormatter());
        fEarliestDatePicker.setBounds(100, 420, 190, 25);
        this.add(fEarliestDatePicker);

        UtilDateModel beforeModel = new UtilDateModel();
        Properties beforeProperties = new Properties();
        beforeProperties.put("text.today", "Today");
        beforeProperties.put("text.month", "Month");
        beforeProperties.put("text.year", "Year");
        JDatePanelImpl beforeDatePanel = new JDatePanelImpl(beforeModel, beforeProperties);
        fLatestDatePicker = new JDatePickerImpl(beforeDatePanel, new DateLabelFormatter());
        fLatestDatePicker.setBounds(100, 450, 190, 25);
        this.add(fLatestDatePicker);
        
        final JTable table = new JTable();
        final JDialog dialog = new JDialog();
        dialog.setSize(720, 420);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        dialog.add(scrollPane);
		
        // display history of transactions for the current customer
        // table class for displaying transaction history located below
		historyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Date earliestDate = (Date) fEarliestDatePicker.getModel().getValue();
                Date latestDate = (Date) fLatestDatePicker.getModel().getValue();

                if (earliestDate == null || latestDate == null) {
                    JOptionPane.showMessageDialog(null, "Dates must be selected.", "ACTION FAILED", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
					List<Transaction> retrieveTransactionHistory = fCustomerAccessor.history(
																			fCustomer, earliestDate, latestDate);
					
	                table.setModel(new FeedbackTableModel(retrieveTransactionHistory));
	                dialog.setVisible(true);
	                
				} catch (UserNotExistsException e) {
					JOptionPane.showMessageDialog(null,
							"No user exists",
							"FAILED TO GET TRANSACTION HISTORY",
							JOptionPane.INFORMATION_MESSAGE);
				}
                		
			}
		});
    }
    
    // table class for displaying the transaction history
    private static class FeedbackTableModel extends AbstractTableModel {

        private static final String[] COLUMN_NAMES = {"Customer Email", "Game Title", "Credit Card", "Date Purchased"};
        private final List<Transaction> fTransactionHistory;

        public FeedbackTableModel(List<Transaction> aTransactionHistory) {
        	fTransactionHistory = aTransactionHistory;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public int getRowCount() {
            return fTransactionHistory.size();
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
                return fTransactionHistory.get(rowIndex).getBuyer().getEmail();
            case 1:
                return fTransactionHistory.get(rowIndex).getGame().getName();
            case 2:
                return fTransactionHistory.get(rowIndex).getCreditCard().getCardNumber();
            case 3:
                return fTransactionHistory.get(rowIndex).getDateOfPurchase();
            default:
                throw new IllegalArgumentException("Column index higher than anticipated.");
            }
        }
    }
    
    private class DisplayCreditCard extends JDialog {

        private final ICustomerAccessor fCustomerAccessor;
        private final Customer fCustomer;
        private final JTable fTable;
        
        public DisplayCreditCard(ICustomerAccessor aCustomerAccessor, List<CreditCard> aListOfCreditCards, Customer thisCustomer) {
            
            this.fCustomerAccessor = aCustomerAccessor;
            this.fCustomer = thisCustomer;

            // Table which displays the list of games.
            fTable = new JTable();
            fTable.setModel(new CreditCardTableModel(aListOfCreditCards));
            fTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(fTable);
            fTable.setFillsViewportHeight(true);
            scrollPane.setBounds(10, 10, 700, 200);

            // Field for updating CCV
            JLabel ccvLabel = new JLabel("new CCV:");
            ccvLabel.setBounds(10, 220, 70, 25);
            final JTextField ccvField = new JTextField("");
            ccvField.setBounds(90, 220, 40, 25);

            // Button for updating CCV
            JButton updateButton = new JButton("Update");
            updateButton.setBounds(140, 220, 130, 25);
            
            updateButton.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    int row = fTable.getSelectedRow();
                    
                    if (row < 0 || row >= fTable.getModel().getRowCount()) {
                        JOptionPane.showMessageDialog(null, "No card is selected.", "SUBMISSION FAILED", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if( ccvField.getText().matches("[0-9]+") && ccvField.getText().length() == 3 ) {
                		fCustomerAccessor.updateCCV((String) fTable.getModel().getValueAt(row, 2), ccvField.getText());
                	}
                    else {
                    	JOptionPane.showMessageDialog(null, "CCV must be a 3 digit number", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    ccvField.setText("");
                    updateTable();
                }
            });

            // Button for deleting credit card
            JButton deleteButton = new JButton("Delete Credit Card");
            deleteButton.setBounds(10, 360, 170, 25);

            deleteButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {

                    int row = fTable.getSelectedRow();

                    if (row < 0 || row >= fTable.getModel().getRowCount()) {
                        JOptionPane.showMessageDialog(null, "No card is selected.", "SUBMISSION FAILED", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    CreditCard targetCard = new CreditCard(
                            fCustomer, 
                            (String) fTable.getModel().getValueAt(row, 2), 
                            (String) fTable.getModel().getValueAt(row, 3), 
                            (String) fTable.getModel().getValueAt(row, 4));

                    try {
                        fCustomerAccessor.deleteCreditCard(targetCard);
                    } catch (UserNotExistsException e) {
                        throw new IllegalStateException(e);
                    }
                    JOptionPane.showMessageDialog(null, "Successfully deleted credit card.", "SUBMISSION SUCCESS", JOptionPane.INFORMATION_MESSAGE); 
                    
                    updateTable();
                }
            });            

            // Put them all together
            this.setLayout(null);
            this.add(scrollPane);
            this.add(ccvLabel);
            this.add(ccvField);
            this.add(updateButton);
            this.add(deleteButton);
            this.setSize(720, 420);
            this.setVisible(true);
        }
        
        private void updateTable() {
            try {
                fTable.setModel(new CreditCardTableModel(fCustomerAccessor.listCreditCards(fCustomer)));
            } catch (UserNotExistsException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
 // table class for displaying the credit cards of customer
    private static class CreditCardTableModel extends AbstractTableModel {

        private static final String[] COLUMN_NAMES = {"Customer Email", "Customer ID", "Credit Card", "CVV", "Address"};
        private final List<CreditCard> fCreditCardRecord;

        public CreditCardTableModel(List<CreditCard> aCreditCardRecord) {
        	fCreditCardRecord = aCreditCardRecord;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public int getRowCount() {
            return fCreditCardRecord.size();
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
                return fCreditCardRecord.get(rowIndex).getCardOwner().getEmail();
            case 1:
            	return fCreditCardRecord.get(rowIndex).getCardOwner().getUserId();
            case 2:
                return fCreditCardRecord.get(rowIndex).getCardNumber();
            case 3:
                return fCreditCardRecord.get(rowIndex).getCcv();
            case 4:
                return fCreditCardRecord.get(rowIndex).getAddress();
            default:
                throw new IllegalArgumentException("Column index higher than anticipated.");
            }
        }
    }
}

package ca.ubc.cs.cs304.steemproject.ui.panels;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.access.ICustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.Retrieves;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.Transaction;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.ui.panels.datepicker.DateLabelFormatter;

@SuppressWarnings("serial")
public class CustomerPanel extends JPanel {

	private final ICustomerAccessor fCustomerAccessor;
    private final Customer fCustomer;
    
    private final JTextArea output = new JTextArea(10,10);
    private final JTextField deleteCreditCardField = new JTextField(50);
    private final JTextField addCreditCardField = new JTextField(50);
    private final JTextField cvvField = new JTextField(5);
    private final JTextField addressWithCardField = new JTextField(50);
    private final JDatePickerImpl fEarliestDatePicker;
    private final JDatePickerImpl fLatestDatePicker;
    
    private final JTextField purchaseGameField = new JTextField(30);
    private final JTextField purchaseCardField = new JTextField(30);
    private final JTextField purchaseCvvField = new JTextField(10);
    
    public CustomerPanel(ICustomerAccessor aCustomerAccessor, Customer aCustomer) {
        
        if (aCustomerAccessor == null) {
            throw new IllegalArgumentException("Customer accessor cannot be null.");
        }
        if (aCustomer == null) {
            throw new IllegalArgumentException("Customerer cannot be null.");
        }
        
        fCustomerAccessor = aCustomerAccessor;
        fCustomer = aCustomer;
        
        setLayout(null);
 
        	// MAIN TITLE
        JLabel titleLabel = new JLabel("Customer Account Options");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setBounds(10, 10, 280, 25);
        this.add(titleLabel);
        
        	// UPDATE CREDIT CARD INFORMATION
        JLabel creditCardLabel = new JLabel("Update Credit Card Information");
        creditCardLabel.setFont(new Font("Serif", Font.BOLD, 12));
        creditCardLabel.setHorizontalAlignment(JLabel.LEFT);
        creditCardLabel.setBounds(10, 40, 280, 25);
        this.add(creditCardLabel);
        
        JButton addCreditCardButton = new JButton("Add Credit Card");
        addCreditCardButton.setBounds(10,70,140,25);
        this.add(addCreditCardButton);
        
        addCreditCardField.setBounds(150,70,200,25);
        this.add(addCreditCardField);
        
        JButton deleteCreditCardButton = new JButton("Delete Credit Card");
        deleteCreditCardButton.setBounds(10,100,140,25);
        this.add(deleteCreditCardButton);
        
        deleteCreditCardField.setBounds(150, 100, 200, 25);
        this.add(deleteCreditCardField);
        
        JLabel cvvCreditCardField = new JLabel("CVV");
        cvvCreditCardField.setBounds(380, 100, 50, 25);
        this.add(cvvCreditCardField);
        
        cvvField.setBounds(420, 100, 50, 25);
        this.add(cvvField);
        
        JButton updateCVV = new JButton("Update CVV");
        updateCVV.setBounds(10, 130, 140, 25);
        this.add(updateCVV);
        
        JLabel addressWithCard = new JLabel("Address");
        addressWithCard.setBounds(20, 160, 100, 25);
        this.add(addressWithCard);
        
        addressWithCardField.setBounds(150, 160, 200, 25);
        this.add(addressWithCardField);
        
        JButton searchButton = new JButton("List Cards");
        searchButton.setBounds(100, 200, 280, 25);
        this.add(searchButton);
       
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
        purchaseGameName.setBounds(100, 520, 80, 25);
        this.add(purchaseGameName);
        
        JLabel buyWithThisCreditCard = new JLabel("Credit Card:");
        buyWithThisCreditCard.setBounds(100, 550, 80, 25);
        this.add(buyWithThisCreditCard);
        
        JLabel buyWithThisCVV = new JLabel("CVV:");
        buyWithThisCVV.setBounds(100, 580, 80, 25);
        this.add(buyWithThisCVV);
        
        purchaseGameField.setBounds(190, 520, 200, 25);
        this.add(purchaseGameField);
        purchaseCardField.setBounds(190, 550, 200, 25);
        this.add(purchaseCardField);
        purchaseCvvField.setBounds(190, 580, 100, 25);
        this.add(purchaseCvvField);

		// Purchase a game with a given credit card and game name
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
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null,
							"Invalid entries",
							"FAILED TO PURCHASE GAME",
							JOptionPane.INFORMATION_MESSAGE);
				}
			
			}
		});
		
		//Updates the CVV of the current user's credit card
		updateCVV.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String changeThisCardCVV = addCreditCardField.getText();
				String newCVV = cvvField.getText();
				
				if (newCVV == "" || changeThisCardCVV == "") {
					JOptionPane.showMessageDialog(null,
							"Invalid entries",
							"FAILED TO UPDATE CVV",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					fCustomerAccessor.updateCCV(changeThisCardCVV, newCVV);
				}
			}
		});
		
		// Deletes the current user account from the database
		deleteAccountButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fCustomerAccessor.removeAccount(fCustomer);
				} catch (UserNotExistsException e1) {
					JOptionPane.showMessageDialog(null,
							"No user exists",
							"FAILED TO REMOVE ACCOUNT",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
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
							JOptionPane.INFORMATION_MESSAGE);
				}
				
				for (int i = 0; i < creditCards.size(); i++) {
					output.append(creditCards.get(i).getCardNumber());
					output.append(" ");
				}
				
			}
		});
		
		// Adds a card to the current user
		addCreditCardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String cardToBeAdded = addCreditCardField.getText();
				String cvvOfCardToAdd = cvvField.getText();
				String addressWithCard = addressWithCardField.getText();
				
				CreditCard addThisCard = new CreditCard(fCustomer, cardToBeAdded, cvvOfCardToAdd, addressWithCard);
				
				try {
					fCustomerAccessor.addNewCreditCard(addThisCard);
					JOptionPane.showMessageDialog(null,cardToBeAdded, 
							"Card added!", JOptionPane.INFORMATION_MESSAGE);
				} catch (UserNotExistsException e) {
					JOptionPane.showMessageDialog(null,
							"No user exists",
							"ADD CARD FAILED",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		// Deletes the given card of the user from the database
		deleteCreditCardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String cardToBeDeleted = deleteCreditCardField.getText();
				String cvvOfCardToDelete = cvvField.getText();
				String addressWithCard = addressWithCardField.getText();
				
				CreditCard deleteThisCard = new CreditCard(fCustomer, cardToBeDeleted, cvvOfCardToDelete, addressWithCard);
				
				try {
					fCustomerAccessor.deleteCreditCard(deleteThisCard);
					JOptionPane.showMessageDialog(null,cardToBeDeleted, 
							"Card deleted!", JOptionPane.INFORMATION_MESSAGE);
				} catch (UserNotExistsException e) {
					JOptionPane.showMessageDialog(null,
							"No user exists",
							"DELETE CARD FAILED",
							JOptionPane.INFORMATION_MESSAGE);
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
    /*
    public static final void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new CustomerPanel(Accessors.getCustomerAccessor(),  new Customer(1, "customer1@gmail.com", "apple123")));
        frame.setSize(500,700);
        frame.setVisible(true);
    }*/
}

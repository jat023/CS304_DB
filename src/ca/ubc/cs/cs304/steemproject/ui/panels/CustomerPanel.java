package ca.ubc.cs.cs304.steemproject.ui.panels;

import java.awt.event.*;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.access.ICustomerAccessor;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

@SuppressWarnings("serial")
public class CustomerPanel extends JPanel {

	private final ICustomerAccessor fCustomerAccessor;
    private final Customer fCustomer;
    private final JTextArea output = new JTextArea(10,10);
    private final JTextField deleteCreditCardField = new JTextField(50);
    private final JTextField addCreditCardField = new JTextField(50);
    private final JTextField cvvField = new JTextField(5);
    private final JTextField addressWithCardField = new JTextField(50);
    
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
 
        JButton addCreditCardButton = new JButton("Add");
        addCreditCardButton.setBounds(10,10,80,25);
        this.add(addCreditCardButton);
        
        addCreditCardField.setBounds(90,10,200,25);
        this.add(addCreditCardField);
        
        JButton deleteCreditCardButton = new JButton("Delete");
        deleteCreditCardButton.setBounds(10,40,80,25);
        this.add(deleteCreditCardButton);
        
        deleteCreditCardField.setBounds(90,40, 200, 25);
        this.add(deleteCreditCardField);
        
        JLabel cvvCreditCardField = new JLabel("CVV");
        cvvCreditCardField.setBounds(315, 40, 50, 25);
        this.add(cvvCreditCardField);
        
        cvvField.setBounds(355, 40, 50, 25);
        this.add(cvvField);
        
        JLabel addressWithCard = new JLabel("Address");
        addressWithCard.setBounds(20, 75, 100, 25);
        this.add(addressWithCard);
        
        addressWithCardField.setBounds(90, 75, 200, 25);
        this.add(addressWithCardField);
        
        JButton purchaseButton = new JButton("Purchase");
        purchaseButton.setBounds(10, 110, 80, 25);
        this.add(purchaseButton);
        
        JButton historyButton = new JButton("Transaction History");
        historyButton.setBounds(10,140, 280, 25);
        this.add(historyButton);
        
        JButton searchButton = new JButton("List Cards");
        searchButton.setBounds(10, 170, 280, 25);
        this.add(searchButton);
       
		output.setBounds(10, 200, 450, 300);
		this.add(output);
		output.setLineWrap(true);
		output.setEditable(false);
		
		
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
					output.append("\n");
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
		
		purchaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		
		historyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		
	
    }
    
    public static final void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new CustomerPanel(Accessors.getCustomerAccessor(),  new Customer(1, "customer1@gmail.com", "apple123")));
        frame.setSize(500,500);
        frame.setVisible(true);
    }
}

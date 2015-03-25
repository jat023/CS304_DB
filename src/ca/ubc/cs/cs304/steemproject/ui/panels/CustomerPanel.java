package ca.ubc.cs.cs304.steemproject.ui.panels;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.access.ICustomerAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;

public class CustomerPanel extends JPanel {

	private final ICustomerAccessor fCustomerAccessor;
    private final Customer fCustomer;
    private final JTextArea output = new JTextArea(10,10);
    
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
        
        JTextField addCreditCardField = new JTextField(50);
        addCreditCardField.setBounds(90,10,200,25);
        this.add(addCreditCardField);
        
        JButton deleteCreditCardButton = new JButton("Delete");
        deleteCreditCardButton.setBounds(10,40,80,25);
        this.add(deleteCreditCardButton);
        
        JTextField deleteCreditCardField = new JTextField(50);
        deleteCreditCardField.setBounds(90,40, 200, 25);
        this.add(deleteCreditCardField);
        
        JButton purchaseButton = new JButton("Purchase");
        purchaseButton.setBounds(10, 70, 80, 25);
        this.add(purchaseButton);
        
        JButton historyButton = new JButton("Transaction History");
        historyButton.setBounds(10,100, 280, 25);
        this.add(historyButton);
        
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(10, 130, 280, 25);
        this.add(searchButton);
       
		output.setBounds(10, 160, 450, 300);
		this.add(output);
		
		
		
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		
		addCreditCardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		
		deleteCreditCardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
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

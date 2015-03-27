package ca.ubc.cs.cs304.steemproject.ui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import ca.ubc.cs.cs304.steemproject.access.ILoginAccessor;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.exception.PasswordIncorrectException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {

    private final ILoginAccessor fLoginAccessor;
    private final JComboBox<UserType> fTypeField;
    private final JTextField fEmailField;
    private final JPasswordField fPasswordField;

    private LoginStatus loginStatus;
    private Customer loggedInCustomer;
    private GameTester loggedInGameTester;
    
    private JButton logInStatusButton;

    public LoginPanel(ILoginAccessor aLoginAccessor) {

        if (aLoginAccessor == null) {
            throw new IllegalArgumentException("Login accessor cannot be null.");
        }
        fLoginAccessor = aLoginAccessor;

        loginStatus = LoginStatus.NOTLOGGEDIN;

        this.setLayout(null);

        JLabel userLabel = new JLabel("Email");
        userLabel.setBounds(10, 10, 80, 25);
        this.add(userLabel);

        fEmailField = new JTextField("", 20);
        fEmailField.setBounds(100, 10, 160, 25);
        this.add(fEmailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 40, 80, 25);
        this.add(passwordLabel);

        fPasswordField = new JPasswordField("", 20);
        fPasswordField.setBounds(100, 40, 160, 25);
        this.add(fPasswordField);
        
        logInStatusButton = new JButton();

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String email = fEmailField.getText();
				String password = String.valueOf(fPasswordField.getPassword());
				UserType type = (UserType) fTypeField.getSelectedItem();

				switch (type) {
				case REGULAR_USER:
					try {
						fLoginAccessor.loginCustomer(email, password);
						loggedInCustomer = fLoginAccessor.lookupCustomer(email);
						loginStatus = LoginStatus.CUSTOMER;
						logInStatusButton.doClick();
						JOptionPane.showMessageDialog(null, "Welcome " + email, "LOGIN SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					} catch (UserNotExistsException e) {
						JOptionPane.showMessageDialog(null, "No user with given this email exists.", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE);
					} catch (PasswordIncorrectException e) {
						JOptionPane.showMessageDialog(null,"Incorrect password.", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE);
					}
					break;
				case GAME_TESTER:
					try {
						fLoginAccessor.loginGametester(email, password);
						loggedInCustomer = fLoginAccessor.lookupCustomer(email);
						loginStatus = LoginStatus.GAMETESTER;
						logInStatusButton.doClick();
						JOptionPane.showMessageDialog(null, "Welcome " + email,"LOGIN SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					} catch (UserNotExistsException e) {
						JOptionPane.showMessageDialog(null, "No game tester with this email exists.", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE);
					} catch (PasswordIncorrectException e) {
						JOptionPane.showMessageDialog(null, "Incorrect password.", "LOGIN FAILED", JOptionPane.ERROR_MESSAGE);
					}
					break;
				default:
					throw new IllegalStateException("User type is " + type);
				}

				fEmailField.setText("");
				fPasswordField.setText("");
			}
		});

        this.add(loginButton);

        fTypeField = new JComboBox<UserType>(UserType.values());
        fTypeField.setBounds(100, 80, 160, 25);
        this.add(fTypeField);

        this.setSize(270, 120);
        this.setBorder(new LineBorder(Color.ORANGE));
    }

    /**
     * resets the login status.
     */
    public void logout() {
        loginStatus = LoginStatus.NOTLOGGEDIN;
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public Customer getCustomer() {
        if (loginStatus.equals(LoginStatus.CUSTOMER)) {
            return loggedInCustomer;
        } else {
            throw new IllegalStateException("No customer has logged in.");
        }
    }

    public GameTester getGameTester() {
        if (loginStatus.equals(LoginStatus.GAMETESTER)) {
            return loggedInGameTester;
        } else {
            throw new IllegalStateException("No game tester has logged in.");
        }
    }
    
    public JButton getLoginStatusButton() {
    	return logInStatusButton;
    }

    public enum LoginStatus {
        NOTLOGGEDIN,
        CUSTOMER,
        GAMETESTER;
    }

    private enum UserType {
        REGULAR_USER,
        GAME_TESTER;
    }
}

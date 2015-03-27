package ca.ubc.cs.cs304.steemproject.ui.panels;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
/**
 * A panel with a single 'logout' button.
 */
public class LogoutPanel extends JPanel {
    
    private final JButton fLogoutButton;
    
    public LogoutPanel() {
        
        fLogoutButton = new JButton("Log Out");
        fLogoutButton.setBounds(10, 10, 80, 25);
        this.add(fLogoutButton);
        
    }
    
    public JButton getLogoutButton() {
        return fLogoutButton;
    }

}

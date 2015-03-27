package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.ui.panels.CustomerPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.GameTesterPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.LoginPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.LoginPanel.LoginStatus;
import ca.ubc.cs.cs304.steemproject.ui.panels.LogoutPanel;
import ca.ubc.cs.cs304.steemproject.ui.panels.PublicPanel;


public class MainPanelUI {
		
	/*
	 * The main function for building the GUI
	 * 				that is called from main in RunSteem.java
	 */
	public void buildGUI() {
		JFrame mainWindow = new JFrame("Steem");
		final JTabbedPane tabbedPane = new JTabbedPane();
		
		mainWindow.setSize(550,700);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final LoginPanel theLoginPanel = new LoginPanel(Accessors.getLoginAccessor());
		final LogoutPanel theLogoutPanel = new LogoutPanel();
		tabbedPane.addTab("Public", new PublicPanel(Accessors.getPublicAccessor()));
		tabbedPane.addTab("Log In", theLoginPanel);
		
		mainWindow.add(tabbedPane);
		mainWindow.setVisible(true);
		
		theLogoutPanel.getLogoutButton().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try { 
                    tabbedPane.remove(2);
                } catch (IndexOutOfBoundsException e) {
                    // ignore
                }
                tabbedPane.remove(theLogoutPanel);
                theLoginPanel.logout();
                tabbedPane.addTab("Log In", theLoginPanel);
            }
        });
		
		theLoginPanel.getLoginStatusButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if( theLoginPanel.getLoginStatus().equals(LoginStatus.CUSTOMER) ) {
                    try { 
                        tabbedPane.remove(2);
                    } catch (IndexOutOfBoundsException e) {
                        // ignore
                    }
				    tabbedPane.remove(theLoginPanel);
				    tabbedPane.addTab("Log Out", theLogoutPanel);
					tabbedPane.addTab("Customer", new CustomerPanel(Accessors.getCustomerAccessor(), theLoginPanel.getCustomer() ));
				} else if( theLoginPanel.getLoginStatus().equals(LoginStatus.GAMETESTER) ) {
                    try { 
                        tabbedPane.remove(2);
                    } catch (IndexOutOfBoundsException e) {
                        // ignore
                    }
                    tabbedPane.remove(theLoginPanel);
                    tabbedPane.addTab("Log Out", theLogoutPanel);
					tabbedPane.addTab("Game Tester", new GameTesterPanel(Accessors.getGameTesterAccessor(), theLoginPanel.getGameTester() ));
				}
			}
		 });
		
		
	}
}
   

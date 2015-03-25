package ca.ubc.cs.cs304.steemproject.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.access.IGameTesterAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

@SuppressWarnings("serial")
public class GameTesterPanel extends JPanel {
    
    private final IGameTesterAccessor fGameTesterAccessor;
    private final GameTester fGameTester;
    
    private final JTextField fGameNameField;
    private final JTextField fGameDeveloperField;
    private final JComboBox<Genre> fGameGenreField;
    private final JComboBox<GameSortByOption> fGameSortOptionField;
    private final JComboBox<SortDirection> fGameSortDirectionField;
    private final JTextArea output = new JTextArea(10,10);
    
    public GameTesterPanel(IGameTesterAccessor aGameTesterAccessor, GameTester aGameTester) {
        
        if (aGameTesterAccessor == null) {
            throw new IllegalArgumentException("Game tester accessor cannot be null.");
        }
        if (aGameTester == null) {
            throw new IllegalArgumentException("Game tester cannot be null.");
        }
        fGameTesterAccessor = aGameTesterAccessor;
        fGameTester = aGameTester;
        
        setLayout(null);
        
        JLabel nameLabel = new JLabel("Game Title");
        nameLabel.setBounds(10, 10, 80, 25);
        this.add(nameLabel);

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
        this.add(fGameGenreField);
        
        JLabel sortByLabel = new JLabel("Sort By");
        sortByLabel.setBounds(10, 100, 80, 25);
        this.add(sortByLabel);

        fGameSortOptionField = new JComboBox<GameSortByOption>(GameSortByOption.values());
        fGameSortOptionField.setBounds(100, 100, 100, 25);
        this.add(fGameSortOptionField);

        fGameSortDirectionField = new JComboBox<SortDirection>(SortDirection.values());
        fGameSortDirectionField.setBounds(210, 100, 80, 25);
        this.add(fGameSortDirectionField);
        
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
    }
    
    public static final void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new GameTesterPanel(Accessors.getGameTesterAccessor(),  new GameTester(1, "gametester1@gmail.com", "Pass1")));
        frame.setSize(500,500);
        frame.setVisible(true);
    }

}

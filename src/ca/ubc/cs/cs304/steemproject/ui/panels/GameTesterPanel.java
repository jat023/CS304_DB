package ca.ubc.cs.cs304.steemproject.ui.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
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

import ca.ubc.cs.cs304.steemproject.access.Accessors;
import ca.ubc.cs.cs304.steemproject.access.IGameTesterAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.development.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.development.GameTesterFeedback;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.ui.panels.datepicker.DateLabelFormatter;

@SuppressWarnings("serial")
public class GameTesterPanel extends JPanel {

    private final IGameTesterAccessor fGameTesterAccessor;
    private final GameTester fGameTester;

    private final JTextField fGameNameField;
    private final JTextField fGameDeveloperField;
    private final JComboBox<Genre> fGameGenreField;
    private final JComboBox<GameSortByOption> fGameSortOptionField;
    private final JComboBox<SortDirection> fGameSortDirectionField;
    private final JDatePickerImpl fEarliestDatePicker;
    private final JDatePickerImpl fLatestDatePicker;

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

        JLabel searchLabel = new JLabel("SEARCH FOR AVAILABLE GAMES");
        searchLabel.setFont(new Font("Serif", Font.BOLD, 14));
        searchLabel.setHorizontalAlignment(JLabel.CENTER);
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
        fGameGenreField.addItem(null);
        fGameGenreField.setSelectedIndex(-1);
        fGameGenreField.setBounds(100, 100, 190, 25);
        this.add(fGameGenreField);

        JLabel sortByLabel = new JLabel("Sort By");
        sortByLabel.setBounds(10, 130, 80, 25);
        this.add(sortByLabel);

        fGameSortOptionField = new JComboBox<GameSortByOption>(GameSortByOption.values());
        fGameSortOptionField.addItem(null);
        fGameSortOptionField.setSelectedIndex(-1);
        fGameSortOptionField.setBounds(100, 130, 100, 25);
        this.add(fGameSortOptionField);

        fGameSortDirectionField = new JComboBox<SortDirection>(SortDirection.values());
        fGameSortDirectionField.addItem(null);
        fGameSortDirectionField.setSelectedIndex(-1);
        fGameSortDirectionField.setBounds(210, 130, 80, 25);
        this.add(fGameSortDirectionField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(10, 160, 280, 25);
        this.add(searchButton);

        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                String matchTitle = fGameNameField.getText().equals("") ? null : fGameNameField.getText();
                String matchDeveloper = fGameDeveloperField.getText().equals("") ? null : fGameDeveloperField.getText();

                List<GameInDevelopment> games = fGameTesterAccessor.listGamesInDevelopment(
                        matchTitle, 
                        (Genre) fGameGenreField.getSelectedItem(), 
                        matchDeveloper, 
                        (GameSortByOption) fGameSortOptionField.getSelectedItem(), 
                        (SortDirection) fGameSortDirectionField.getSelectedItem());

                new DisplayGameInDevelopment(fGameTesterAccessor, games, fGameTester);

            }

        });

        // Table dialog for showing game tester feedbacks.
        JLabel collectLabel = new JLabel("DISPLAY PREVIOUS TESTS");
        collectLabel.setFont(new Font("Serif", Font.BOLD, 14));
        collectLabel.setHorizontalAlignment(JLabel.CENTER);
        collectLabel.setBounds(300, 10, 280, 25);
        this.add(collectLabel);

        JLabel afterLabel = new JLabel("Earliest");
        afterLabel.setBounds(300, 40, 80, 25);
        this.add(afterLabel);

        JLabel beforeLabel = new JLabel("Latest");
        beforeLabel.setBounds(300, 70, 80, 25);
        this.add(beforeLabel);

        UtilDateModel afterModel = new UtilDateModel();
        Properties afterProperties = new Properties();
        afterProperties.put("text.today", "Today");
        afterProperties.put("text.month", "Month");
        afterProperties.put("text.year", "Year");
        JDatePanelImpl afterDatePanel = new JDatePanelImpl(afterModel, afterProperties);
        fEarliestDatePicker = new JDatePickerImpl(afterDatePanel, new DateLabelFormatter());
        fEarliestDatePicker.setBounds(390, 40, 190, 25);
        this.add(fEarliestDatePicker);

        UtilDateModel beforeModel = new UtilDateModel();
        Properties beforeProperties = new Properties();
        beforeProperties.put("text.today", "Today");
        beforeProperties.put("text.month", "Month");
        beforeProperties.put("text.year", "Year");
        JDatePanelImpl beforeDatePanel = new JDatePanelImpl(beforeModel, beforeProperties);
        fLatestDatePicker = new JDatePickerImpl(beforeDatePanel, new DateLabelFormatter());
        fLatestDatePicker.setBounds(390, 70, 190, 25);
        this.add(fLatestDatePicker);

        final JTable table = new JTable();
        final JDialog dialog = new JDialog();
        dialog.setSize(720, 420);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        dialog.add(scrollPane);

        JButton collectButton = new JButton("Display");
        collectButton.setBounds(300, 100, 290, 25);
        this.add(collectButton);

        collectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                Date earliestDate = (Date) fEarliestDatePicker.getModel().getValue();
                Date latestDate = (Date) fLatestDatePicker.getModel().getValue();

                if (earliestDate == null || latestDate == null) {
                    JOptionPane.showMessageDialog(null, "Dates must be selected.", "ACTION FAILED", JOptionPane.ERROR_MESSAGE);
                    return;
                } 

                List<GameTesterFeedback> feedbacks = fGameTesterAccessor.collectFeedback(earliestDate, latestDate);

                table.setModel(new FeedbackTableModel(feedbacks));
                dialog.setVisible(true);
            }
        });

    }

    private class DisplayGameInDevelopment extends JDialog {

        public DisplayGameInDevelopment(final IGameTesterAccessor aGameTestAccessor, final List<GameInDevelopment> aListOfGames, final GameTester aGameTester) {

            // Table which displays the list of games.
            final JTable table = new JTable();
            table.setModel(new GameInDevelopmentTableModel(aListOfGames));
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(table);
            table.setFillsViewportHeight(true);
            scrollPane.setBounds(10, 10, 700, 200);

            // Fields for giving feedback.
            JLabel ratingLabel = new JLabel("Rating out of 10:");
            ratingLabel.setBounds(10, 220, 130, 25);
            final JTextField ratingField = new JTextField("");
            ratingField.setBounds(140, 220, 40, 25);

            JLabel feedbackLabel = new JLabel("Feedback:");
            feedbackLabel.setBounds(10, 250, 80, 25);
            final JTextArea feedbackField = new JTextArea("Enter feedback...");
            feedbackField.setBounds(90, 250, 550, 100);

            // Button for submitting feedback
            JButton submitButton = new JButton("Submit Feedback");
            submitButton.setBounds(10, 360, 170, 25);

            submitButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {

                    int row = table.getSelectedRow();

                    if (row < 0 || row >= aListOfGames.size()) {
                        JOptionPane.showMessageDialog(null, "No game is selected.", "SUBMISSION FAILED", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    GameInDevelopment targetGame = aListOfGames.get(row);

                    float rating;
                    try {
                        rating = Float.valueOf(ratingField.getText());
                        if (rating < 0 || rating > 10) {
                            JOptionPane.showMessageDialog(null, "Rating must be a number between 0 and 10.", "SUBMISSION FAILED", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Rating must be a number between 0 and 10.", "SUBMISSION FAILED", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        aGameTestAccessor.provideFeedback(targetGame, aGameTester, rating, feedbackField.getText());
                    } catch (UserNotExistsException e) {
                        throw new IllegalStateException(e);
                    } catch (GameNotExistException e) {
                        throw new IllegalStateException(e);
                    }

                    ratingField.setText("");
                    feedbackField.setText("");
                    JOptionPane.showMessageDialog(null, "Successfully submitted feedback. Thank you!", "SUBMISSION SUCCESS", JOptionPane.INFORMATION_MESSAGE); 

                }
            });            

            // Put them all together
            this.setLayout(null);
            this.add(scrollPane);
            this.add(ratingLabel);
            this.add(ratingField);
            this.add(feedbackLabel);
            this.add(feedbackField);
            this.add(submitButton);

            this.setSize(720, 420);
            this.setVisible(true);
        }   
    }

    private static class GameInDevelopmentTableModel extends AbstractTableModel {

        private static final String[] COLUMN_NAMES = {"Game Title", "Description", "Genre", "Developer", "Current Version"};
        private final List<GameInDevelopment> fGames;

        public GameInDevelopmentTableModel(List<GameInDevelopment> aGames) {
            fGames = aGames;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public int getRowCount() {
            return fGames.size();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            switch (columnIndex) {
            case 0: 
                return fGames.get(rowIndex).getName();
            case 1:
                return fGames.get(rowIndex).getDescription();
            case 2:
                return fGames.get(rowIndex).getGenre();
            case 3:
                return fGames.get(rowIndex).getDeveloper();
            case 4:
                return fGames.get(rowIndex).getVersion();
            default:
                throw new IllegalArgumentException("Column index higher than anticipated.");
            }
        }
    }

    private static class FeedbackTableModel extends AbstractTableModel {

        private static final String[] COLUMN_NAMES = {"GameTester Email", "Game Title", "Rating", "DateTime", "Feedback"};
        private final List<GameTesterFeedback> fFeedbacks;

        public FeedbackTableModel(List<GameTesterFeedback> aFeedbacks) {
            fFeedbacks = aFeedbacks;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public int getRowCount() {
            return fFeedbacks.size();
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
                return fFeedbacks.get(rowIndex).getTester().getEmail();
            case 1:
                return fFeedbacks.get(rowIndex).getGame().getName();
            case 2:
                return fFeedbacks.get(rowIndex).getRating();
            case 3:
                return fFeedbacks.get(rowIndex).getDate();
            case 4:
                return fFeedbacks.get(rowIndex).getFeedback();
            default:
                throw new IllegalArgumentException("Column index higher than anticipated.");
            }
        }

    }
}

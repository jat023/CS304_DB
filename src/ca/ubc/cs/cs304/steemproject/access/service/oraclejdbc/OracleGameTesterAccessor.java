package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.access.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.access.service.IGameTesterAccessor;
import ca.ubc.cs.cs304.steemproject.access.service.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.service.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.development.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;

public class OracleGameTesterAccessor implements IGameTesterAccessor {

    private static final Logger log = Logger.getLogger(OracleGameTesterAccessor.class);
    
    private static final String insertTestsSQL = "INSERT INTO " + Tables.FEEDBACK_TABLENAME
            + "("
            + Tables.USER_ATTR_USERID+ ","
            + Tables.GAME_ATTR_NAME+ ","
            + Tables.FEEDBACK_ATTR_DATE+ ","
            + Tables.FEEDBACK_ATTR_RATING+ ","
            + Tables.FEEDBACK_ATTR_FEEDBACK+ ","
            + "(?,?,?,?,?)";
    
    private static OracleGameTesterAccessor fInstance;

    private OracleGameTesterAccessor() {}

    public static OracleGameTesterAccessor getInstance() {
        if (fInstance == null) {
            fInstance = new OracleGameTesterAccessor();
        } 
        return fInstance;
    }
    
    @Override
    public List<GameInDevelopment> listGamesInDevelopment() {
        return listGamesInDevelopment(null, null, null, null, null);
    }

    @Override
    public List<GameInDevelopment> listGamesInDevelopment(
            String matchName, String matchGenre, String matchDeveloper,
            GameSortByOption sortByOption, SortDirection sortDirection) {

        ResultSet results = GameQueriesHelper.queryGames(Tables.DEVELOPMENT_GAMETABLENAME, matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, null, null);
        
        List<GameInDevelopment> games = new ArrayList<GameInDevelopment>();
        
        try {
            while (results.next()) {

                games.add(new GameInDevelopment(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION), 
                        Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE)), 
                        results.getString(Tables.GAME_ATTR_DEVELOPER),
                        results.getString(Tables.DEVELOPMENT_GAME_ATTR_VERSION)));
                
            }
        } catch (Exception e) {
            log.error("Could not read results.", e);
            throw new RuntimeException("Could not read results.", e);
        }

        return null;
    }

    @Override
    public boolean provideFeedback(GameInDevelopment aGameInDevelopment, GameTester aGameTester, float aRating, String aFeedback) throws UserNotExistsException, GameNotExistException {
        
        if (!QueriesHelper.userExists(aGameTester.getUserId(), Tables.GAME_TESTER_TABLENAME)) {
            throw new UserNotExistsException();
        }
        
        if (!QueriesHelper.gameExists(aGameInDevelopment.getName(), Tables.DEVELOPMENT_GAMETABLENAME)) {
            throw new GameNotExistException();
        }
        
        try {
            
            PreparedStatement pstmt = SteemOracleDbConnector.getDefaultConnection().prepareStatement(insertTestsSQL);
            pstmt.setInt(1, aGameTester.getUserId());
            pstmt.setString(2, aGameInDevelopment.getName());
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setFloat(4, aRating);
            pstmt.setString(5, aFeedback);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            log.error("Could not insert feedback.", e);
            throw new RuntimeException("Could not insert feedback.", e);
        }
        
        return true;
    }
    
    

}

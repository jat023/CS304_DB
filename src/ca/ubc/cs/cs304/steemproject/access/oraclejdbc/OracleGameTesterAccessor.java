package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ca.ubc.cs.cs304.steemproject.access.IGameTesterAccessor;
import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.IUser;
import ca.ubc.cs.cs304.steemproject.base.development.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.development.GameTesterFeedback;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.InternalConnectionException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public class OracleGameTesterAccessor implements IGameTesterAccessor {

    private static final Logger log = Logger.getLogger(OracleGameTesterAccessor.class);

    private static OracleGameTesterAccessor fInstance;

    private final PreparedStatement fRetrieveTestsSQL;
    private final PreparedStatement fRetrieveGameSQL;

    private OracleGameTesterAccessor() {
        try {
            fRetrieveTestsSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "SELECT * FROM " +Tables.FEEDBACK_TABLENAME+ " WHERE " +Tables.FEEDBACK_ATTR_TIME+ " BETWEEN ? AND ? ORDER BY " + Tables.FEEDBACK_ATTR_TIME + " ASC");
            fRetrieveGameSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "SELECT * FROM " +Tables.DEVELOPMENT_GAMETABLENAME+ " WHERE " +Tables.GAME_ATTR_NAME+ "=?");
        } catch (SQLException e) {
            log.error("Failed to prepare statements.", e);
            throw new InternalConnectionException("Failed to prepare statements.", e);
        }
    }

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

        ResultSet results = GameQueriesHelper.queryGames(Tables.DEVELOPMENT_GAMETABLENAME, matchName, matchGenre, matchDeveloper, null, null, sortByOption, sortDirection, false, null);

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

        if (!GameQueriesHelper.gameExists(aGameInDevelopment.getName(), Tables.DEVELOPMENT_GAMETABLENAME)) {
            throw new GameNotExistException();
        }

        try {

            GameTesterFeedback feedback = new GameTesterFeedback(aGameInDevelopment, aGameTester, new Date(System.currentTimeMillis()), aRating, aFeedback);
            Inserts.insertFeedback(feedback);

        } catch (SQLException e) {
            log.error("Could not insert feedback.", e);
            throw new RuntimeException("Could not insert feedback.", e);
        }

        return true;
    }

    @Override
    public List<GameTesterFeedback> collectFeedback(Date afterThisDate, Date beforeThisDate) {

        if (afterThisDate == null || beforeThisDate == null) {
            log.error("Date parameters cannot be null.");
            throw new IllegalArgumentException("Date parameters cannot be null.");
        }

        ResultSet results;

        try {
            fRetrieveTestsSQL.setDate(1, new java.sql.Date(afterThisDate.getTime()));
            fRetrieveTestsSQL.setDate(2, new java.sql.Date(beforeThisDate.getTime()));
            results = fRetrieveTestsSQL.executeQuery();
        } catch (SQLException e) {
            log.error("Could not execute query.", e);
            throw new InternalConnectionException("Could not execute query.", e);
        }

        Collection<GameTesterFeedback> feedbacks = new ArrayList<GameTesterFeedback>();

        try {

            while (results.next()) {

                GameInDevelopment game = retrieveGameInDevelopment(results.getString(Tables.GAME_ATTR_NAME));
                
                IUser user = QueriesHelper.retrieveUser(results.getInt(Tables.USER_ATTR_USERID), Tables.GAME_TESTER_TABLENAME);
                
                GameTester tester = new GameTester(user.getUserId(), user.getEmail(), user.getPassword());
                
                feedbacks.add(new GameTesterFeedback(
                        game, 
                        tester,
                        results.getTimestamp(Tables.FEEDBACK_ATTR_TIME), 
                        results.getFloat(Tables.FEEDBACK_ATTR_RATING), 
                        results.getString(Tables.FEEDBACK_ATTR_FEEDBACK)));
            }

        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException("Could not read results.", e);
        } catch (GameNotExistException e) {
            log.error("Game not found.", e);
            throw new RuntimeException(e);
        } catch (UserNotExistsException e) {
            log.error("Found test submitted by non-existant tester.");
        } 

        return null;
    }

    private GameInDevelopment retrieveGameInDevelopment(String nameOfGame) throws GameNotExistException {
        ResultSet results;
        
        try {
            fRetrieveGameSQL.setString(1, nameOfGame);
            results = fRetrieveGameSQL.executeQuery();
        } catch (SQLException e) {
            log.error("Could not execute query.", e);
            throw new InternalConnectionException("Could not execute query.", e);
        }
        
        try {
            if (results.next()) {
                
                GameInDevelopment game = new GameInDevelopment(
                        results.getString(Tables.GAME_ATTR_NAME), 
                        results.getString(Tables.GAME_ATTR_DESCRIPTION),
                        Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE)), 
                        results.getString(Tables.GAME_ATTR_DEVELOPER),
                        results.getString(Tables.DEVELOPMENT_GAME_ATTR_VERSION));
                
                if (results.next()) {
                    throw new RuntimeException("Result returned more than one game with the supplied name.");
                }
                
                return game;
                
            } else {
                throw new GameNotExistException(nameOfGame);
            }
        } catch (SQLException e) {
            log.error("Could not read results.", e);
            throw new RuntimeException("Could not read results.", e);
        }
    }

}

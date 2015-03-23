package ca.ubc.cs.cs304.steemproject.access;

import java.util.Date;
import java.util.List;

import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.development.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.base.development.GameTester;
import ca.ubc.cs.cs304.steemproject.base.development.GameTesterFeedback;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public interface IGameTesterAccessor {

    /**
     * Lists all games in development with the matching parameters. 
     * @return
     */
    public List<GameInDevelopment> listGamesInDevelopment();

    /**
     * Lists all games in development with the matching parameters. 
     * All matching parameter can be null, in which case no matching is enforced.
     * Optional sorting options are available.
     * @param matchName
     * @param matchGenre
     * @param matchPublisher
     * @param sortByOption
     * @param sortDirection
     * @return
     */
    public List<GameInDevelopment> listGamesInDevelopment(
            String matchName, String matchGenre, String matchPublisher, 
            GameSortByOption sortByOption, SortDirection sortDirection);    

    /**
     * Provide feedback for a certain game.
     * @return true if feedback was stored successfully, false otherwise.
     */
    public boolean provideFeedback(GameInDevelopment aGameInDevelopment, GameTester aGameTester, float aRating, String aFeedback) throws UserNotExistsException, GameNotExistException;

    /**
     * Query for list of ratings and feedbacks of a specific game between within a certain time
     * interval (inclusive), submitted by any game tester, sorted in chronological order.
     */
    public List<GameTesterFeedback> collectFeedback(Date afterThisDate, Date beforeThisDate);
    
}

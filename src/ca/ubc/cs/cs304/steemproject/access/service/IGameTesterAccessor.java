package ca.ubc.cs.cs304.steemproject.access.service;

import java.util.List;

import ca.ubc.cs.cs304.steemproject.access.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.access.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.access.game.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.access.service.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.service.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.access.user.GameTester;

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

}

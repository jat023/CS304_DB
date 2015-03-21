package ca.ubc.cs.cs304.steemproject.access.service;

import java.util.Collection;
import java.util.Map;

import ca.ubc.cs.cs304.steemproject.access.exception.UserNotExistsException;
import ca.ubc.cs.cs304.steemproject.access.service.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.service.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;

public interface IPublicAccessor {
    
    /**
     * Lists all purchasable games.
     * @return
     */
    public Collection<FinalizedGame> listPurchasableGames();

    /**
     * Lists all purchasable games with the matching parameters. 
     * All matching parameter can be null, in which case no matching is enforced.
     * Optional sorting options are available.
     * Can additionally choose to list only games that currently have a discounted price.
     * @param matchName
     * @param matchGenre
     * @param matchDeveloper
     * @param matchLowestPrice
     * @param matchHighestPrice
     * @param sortByOption
     * @param sortDirection
     * @param listOnlyDiscountedGames
     * @return
     */
    public Collection<FinalizedGame> listPurchasableGames(
            String matchName, String matchGenre, String matchDeveloper, 
            Float matchLowestPrice, Float matchHighestPrice, 
            GameSortByOption sortByOption, SortDirection sortDirection, 
            boolean listOnlyDiscountedGames);

    /**
     * Lists all games belonging to the user with the supplied ID.
     * @param gameOwnerId
     * @return
     * @throws UserNotExistsException
     */
    public Map<FinalizedGame, Float> listGamesOwned(int gameOwnerId) throws UserNotExistsException;

    /**
     * Lists all games belonging to the user with the supplied email.
     * @param gameOwnerEmail
     * @return
     * @throws UserNotExistsException
     */
    public Map<FinalizedGame, Float> listGamesOwned(String gameOwnerEmail) throws UserNotExistsException;

    /**
     * Lists all games with the matching parameters belonging to the user with the supplied ID. 
     * Optional sorting options are available.
     * @param gameOwnerId
     * @param matchName
     * @param matchGenre
     * @param matchDeveloper
     * @param sortByOption
     * @param sortDirection
     * @return
     * @throws UserNotExistsException
     */
    public Map<FinalizedGame, Float> listGamesOwned(
            int gameOwnerId, 
            String matchName, String matchGenre, String matchDeveloper,
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException;

    /**
     * Lists all games with the matching parameters belonging to the user with the supplied email. 
     * Optional sorting options are available.
     * @param gameOwnerEmail
     * @param matchName
     * @param matchGenre
     * @param matchDeveloper
     * @param sortByOption
     * @param sortDirection
     * @return
     * @throws UserNotExistsException
     */
    public Map<FinalizedGame, Float> listGamesOwned(
            String gameOwnerEmail, 
            String matchName, String matchGenre, String matchDeveloper, 
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException;

}
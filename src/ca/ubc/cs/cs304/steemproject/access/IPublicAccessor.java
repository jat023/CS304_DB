package ca.ubc.cs.cs304.steemproject.access;

import java.util.List;

import ca.ubc.cs.cs304.steemproject.access.options.GameSortByOption;
import ca.ubc.cs.cs304.steemproject.access.options.SortDirection;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.base.released.Playtime;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public interface IPublicAccessor {
    
    /**
     * Lists all purchasable games.
     * @return
     */
    public List<FinalizedGame> listPurchasableGames();

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
    public List<FinalizedGame> listPurchasableGames(
            String matchName, Genre matchGenre, String matchDeveloper, 
            Float matchLowestPrice, Float matchHighestPrice, 
            GameSortByOption sortByOption, SortDirection sortDirection, 
            boolean listOnlyDiscountedGames);

    /**
     * Lists all games belonging to the user with the supplied ID.
     * @param gameOwnerId
     * @return
     * @throws UserNotExistsException
     */
    public List<Playtime> listGamesOwned(int gameOwnerId) throws UserNotExistsException;

    /**
     * Lists all games belonging to the user with the supplied email.
     * @param gameOwnerEmail
     * @return
     * @throws UserNotExistsException
     */
    public List<Playtime> listGamesOwned(String gameOwnerEmail) throws UserNotExistsException;

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
    public List<Playtime> listGamesOwned(
            int gameOwnerId, 
            String matchName, Genre matchGenre, String matchDeveloper,
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
    public List<Playtime> listGamesOwned(
            String gameOwnerEmail, 
            String matchName, Genre matchGenre, String matchDeveloper, 
            GameSortByOption sortByOption, SortDirection sortDirection) throws UserNotExistsException;

}
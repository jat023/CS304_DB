package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;
import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.IUser;
import ca.ubc.cs.cs304.steemproject.base.development.GameInDevelopment;
import ca.ubc.cs.cs304.steemproject.base.released.CreditCard;
import ca.ubc.cs.cs304.steemproject.base.released.Customer;
import ca.ubc.cs.cs304.steemproject.base.released.FinalizedGame;
import ca.ubc.cs.cs304.steemproject.exception.GameNotExistException;
import ca.ubc.cs.cs304.steemproject.exception.UserNotExistsException;

public final class Retrieves {

    private static PreparedStatement fRetrieveFinalizedGameSQL;
    private static PreparedStatement fRetrieveGameInDevelopmentSQL;
    private static PreparedStatement fRetrieveCreditCardSQL;

    public static GameInDevelopment retrieveGameInDevelopment(String nameOfGame) throws GameNotExistException, SQLException {

        if (fRetrieveGameInDevelopmentSQL==null) {
            fRetrieveGameInDevelopmentSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "SELECT * FROM " +Tables.DEVELOPMENT_GAMETABLENAME+ " WHERE " +Tables.GAME_ATTR_NAME+ "=?");
        }

        fRetrieveGameInDevelopmentSQL.setString(1, nameOfGame);
        ResultSet results = fRetrieveGameInDevelopmentSQL.executeQuery();

        if (results.next()) {

            return new GameInDevelopment(
                    results.getString(Tables.GAME_ATTR_NAME), 
                    results.getString(Tables.GAME_ATTR_DESCRIPTION),
                    Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE)), 
                    results.getString(Tables.GAME_ATTR_DEVELOPER),
                    results.getString(Tables.DEVELOPMENT_GAME_ATTR_VERSION));

        } else {
            throw new GameNotExistException(nameOfGame);
        }
    }

    public static CreditCard retrieveCreditCard(String aCardNumber) throws SQLException {

        if (fRetrieveCreditCardSQL == null) {
            fRetrieveCreditCardSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "SELECT * FROM " +Tables.CREDIT_CARD_TABLENAME+ " WHERE " +Tables.CREDIT_CARD_ATTR_CARDNUM+ "=?");
        }

        fRetrieveCreditCardSQL.setString(1, aCardNumber);
        ResultSet results = fRetrieveCreditCardSQL.executeQuery();

        if (results.next()) {

            IUser user;
            try {
                user = QueriesHelper.retrieveUser(results.getInt(Tables.USER_ATTR_USERID), Tables.CUSTOMER_TABLENAME);
            } catch (UserNotExistsException e) {
                throw new IllegalStateException("Won't happen.");
            }

            return new CreditCard(
                    new Customer(user.getUserId(), user.getEmail(), user.getPassword()), 
                    results.getString(Tables.CREDIT_CARD_ATTR_CARDNUM), 
                    results.getString(Tables.CREDIT_CARD_ATTR_CCV), 
                    results.getString(Tables.CREDIT_CARD_ATTR_ADDRESS));
        } else {
            throw new IllegalArgumentException("Card number does not exist in database.");
        }

    }

    public static FinalizedGame retrieveFinalizedGame(String nameOfGame) throws GameNotExistException, SQLException {

        if (fRetrieveFinalizedGameSQL==null) {
            fRetrieveFinalizedGameSQL = SteemOracleDbConnector.getDefaultConnection().prepareStatement(
                    "SELECT * FROM " +Tables.FINALIZED_GAME_TABLENAME+ " WHERE " +Tables.GAME_ATTR_NAME+ "=?");
        }

        String query = "SELECT * FROM " +Tables.FINALIZED_GAME_TABLENAME+ " WHERE " + Tables.GAME_ATTR_NAME+ "='" +nameOfGame+ "'";
        ResultSet results = SteemOracleDbConnector.getDefaultConnection().createStatement().executeQuery(query);

        if (results.next()) {

            return new FinalizedGame(
                    results.getString(Tables.GAME_ATTR_NAME), 
                    results.getString(Tables.GAME_ATTR_DESCRIPTION),
                    Genre.valueOf(results.getString(Tables.GAME_ATTR_GENRE)), 
                    results.getString(Tables.GAME_ATTR_DEVELOPER), 
                    results.getFloat(Tables.FINALIZED_GAME_ATTR_RATING), 
                    results.getFloat(Tables.FINALIZED_GAME_ATTR_FULLPRICE), 
                    results.getInt(Tables.FINALIZED_GAME_ATTR_ONSPECIAL)==1, 
                    results.getFloat(Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC));


        } else {
            throw new GameNotExistException(nameOfGame);
        }

    }
} 

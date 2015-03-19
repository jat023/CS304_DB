package ca.ubc.cs.cs304.steemproject.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ca.ubc.cs.cs304.steemproject.db.connection.SteemDbConnector;
import ca.ubc.cs.cs304.steemproject.game.PurchasableGame;

public class Tables {

    public static final String CUSTOMER_TABLENAME = "Customer";
    public static final String GAME_TESTER_TABLENAME = "GameTester";

    public static final String USER_ATTR_USERID = "userId";
    public static final String USER_ATTR_EMAIL = "email";
    public static final String USER_ATTR_PASSWORD = "password";

    public static final String PURCHASABLE_GAME_TABLENAME = "FinalizedGame";
    public static final String PURCHASABLE_GAME_ATTR_RATING = "rating";
    public static final String PURCHASABLE_GAME_ATTR_FULLPRICE = "price";
    public static final String PURCHASABLE_GAME_ATTR_ONSPECIAL = "onSpecial";
    public static final String PURCHASABLE_GAME_ATTR_DISCOUNTPERC = "discountPrice";

    public static final String DEVELOPMENT_GAMETABLENAME = "GameInDevelopment";
    public static final String DEVELOPMENT_GAME_ATTR_VERSION = "version";

    public static final String GAME_ATTR_NAME = "gname";
    public static final String GAME_ATTR_DESCRIPTION = "description";
    public static final String GAME_ATTR_GENRE = "genre";
    public static final String GAME_ATTR_DEVELOPER = "developer";

    public static final String CREDIT_CARD_TABLENAME = "CreditCard";
    public static final String CREDIT_CARD_ATTR_CARDNUM = "cardNum";
    public static final String CREDIT_CARD_ATTR_CCV = "ccv";
    public static final String CREDIT_CARD_ATTR_ADDRESS = "address";

    public static final String OWNS_GAME_TABLENAME = "OwnsGame";
    public static final String OWNS_GAME_ATTR_HOURS = "hours";

    public static final String TRANSACTION_TABLENAME = "Transaction";
    public static final String TRANSACTION_DATETIME = "dateTime";

    public static final String TEST_TABLENAME = "Test";
    public static final String TEST_ATTR_DATETIME = "dateTime";
    public static final String TEST_ATTR_RATING = "rating";

    public static void addNewPurchasableGame(PurchasableGame aPurchasableGame) throws SQLException {
        String insertFinalizedGameSQL = "INSERT INTO " + Tables.PURCHASABLE_GAME_TABLENAME
                + "("
                + GAME_ATTR_NAME+ ","
                + GAME_ATTR_DESCRIPTION+ ","
                + GAME_ATTR_GENRE+ ","
                + GAME_ATTR_DEVELOPER+ ","
                + PURCHASABLE_GAME_ATTR_RATING+ ","
                + PURCHASABLE_GAME_ATTR_FULLPRICE+ ","
                + PURCHASABLE_GAME_ATTR_ONSPECIAL+ ","
                + PURCHASABLE_GAME_ATTR_DISCOUNTPERC
                + ") VALUES "
                + "(?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = SteemDbConnector.getDefaultConnection().prepareStatement(insertFinalizedGameSQL);
        preparedStatement.setString(1, aPurchasableGame.getName());
        preparedStatement.setString(2, aPurchasableGame.getDescription());
        preparedStatement.setString(3, aPurchasableGame.getGenre());
        preparedStatement.setString(4, aPurchasableGame.getPublisher());
        preparedStatement.setFloat(5, aPurchasableGame.getRating());
        preparedStatement.setFloat(6, aPurchasableGame.getFullPrice());
        preparedStatement.setInt(7, aPurchasableGame.isOnSpecial() ? 1 : 0);
        preparedStatement.setFloat(8, aPurchasableGame.getDiscountPercentage());
        preparedStatement.executeUpdate();
    }
}

package ca.ubc.cs.cs304.steemproject.access.service.oraclejdbc;

final class Tables {

    public static final String CUSTOMER_TABLENAME = "Customer";
    public static final String GAME_TESTER_TABLENAME = "GameTester";

    public static final String USER_ATTR_USERID = "userId";
    public static final String USER_ATTR_EMAIL = "email";
    public static final String USER_ATTR_PASSWORD = "password";

    public static final String FINALIZED_GAME_TABLENAME = "FinalizedGame";
    public static final String FINALIZED_GAME_ATTR_RATING = "rating";
    public static final String FINALIZED_GAME_ATTR_FULLPRICE = "price";
    public static final String FINALIZED_GAME_ATTR_ONSPECIAL = "onSpecial";
    public static final String FINALIZED_GAME_ATTR_DISCOUNTPERC = "discountPrice";

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
    public static final String TRANSACTION_ATTR_TIME = "transactionDate";

    public static final String FEEDBACK_TABLENAME = "Feedback";
    public static final String FEEDBACK_ATTR_TIME = "feedbackDate";
    public static final String FEEDBACK_ATTR_RATING = "rating";
    public static final String FEEDBACK_ATTR_FEEDBACK = "feedback";

}

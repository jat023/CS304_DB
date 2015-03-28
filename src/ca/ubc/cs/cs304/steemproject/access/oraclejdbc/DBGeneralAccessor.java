package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;

public class DBGeneralAccessor {
	private DBcustomers allCustomers;
	private DBtesters allTesters;
	private DBcards allCards;
	private DBgames allGames;
	
	private Connection fConnection;
	private Statement statement;
	
	public DBGeneralAccessor() throws SQLException {
		fConnection = SteemOracleDbConnector.getDefaultConnection();
		statement = fConnection.createStatement();
		
		allCustomers = new DBcustomers(statement);
		allTesters = new DBtesters(statement);
		allCards = new DBcards(statement);
		allGames = new DBgames(statement);
	}
	
	public DBcustomers getCustomers() {
		return allCustomers;
	}
	public DBtesters getTesters() {
		return allTesters;
	}
	public DBcards getCards() {
		return allCards;
	}
	public DBgames getGames() {
		return allGames;
	}
	
	
	public class DBcustomers extends DBQueriesHelper{
		private Statement statement;
		
		private List<Integer> customerIDs;
		private List<String> customerEmails;
		private List<String> customerPasswords;
		
		DBcustomers(Statement aStatement) {
			statement = aStatement;		
			customerIDs = new ArrayList<Integer>();
			customerEmails = new ArrayList<String>();
			customerPasswords = new ArrayList<String>();
		}
		
		public List<Integer> getIDs() throws SQLException {
			ResultSet results = SelectQuery(Tables.USER_ATTR_USERID, Tables.CUSTOMER_TABLENAME, statement);
			addIntegersToList(results, customerIDs, Tables.USER_ATTR_USERID );
			return customerIDs;
		}
		public List<String> getEmails() throws SQLException {
			ResultSet results = SelectQuery(Tables.USER_ATTR_EMAIL, Tables.CUSTOMER_TABLENAME, statement);
			addStringsToList(results, customerEmails, Tables.USER_ATTR_EMAIL );
			return customerEmails;
		}
		public List<String> getPasswords() throws SQLException {
			ResultSet results = SelectQuery(Tables.USER_ATTR_PASSWORD, Tables.CUSTOMER_TABLENAME, statement);
			addStringsToList(results, customerPasswords, Tables.USER_ATTR_PASSWORD );
			return customerPasswords;
		}
	}
	
	
	public class DBtesters extends DBQueriesHelper{
		private Statement statement;
		
		private List<Integer> testerIDs;
		private List<String> testerEmails;
		private List<String> testerPasswords;
		
		DBtesters(Statement aStatement) {
			statement = aStatement;		
			testerIDs = new ArrayList<Integer>();
			testerEmails = new ArrayList<String>();
			testerPasswords = new ArrayList<String>();
		}
		
		public List<Integer> getIDs() throws SQLException {
			ResultSet results = SelectQuery(Tables.USER_ATTR_USERID, Tables.GAME_TESTER_TABLENAME, statement);
			addIntegersToList(results, testerIDs, Tables.USER_ATTR_USERID );
			return testerIDs;
		}
		public List<String> getEmails() throws SQLException {
			ResultSet results = SelectQuery(Tables.USER_ATTR_EMAIL, Tables.GAME_TESTER_TABLENAME, statement);
			addStringsToList(results, testerEmails, Tables.USER_ATTR_EMAIL );
			return testerEmails;
		}
		public List<String> getPasswords() throws SQLException {
			ResultSet results = SelectQuery(Tables.USER_ATTR_PASSWORD, Tables.GAME_TESTER_TABLENAME, statement);
			addStringsToList(results, testerPasswords, Tables.USER_ATTR_PASSWORD );
			return testerPasswords;
		}
	}
	
	public class DBcards extends DBQueriesHelper{
		private Statement statement;
		
		private List<Integer> userIDs;
		private List<String> CardNumbers;
		private List<String> CCVs;
		private List<String> Addresses;
		
		DBcards(Statement aStatement) {
			statement = aStatement;		
			userIDs = new ArrayList<Integer>();
			CardNumbers = new ArrayList<String>();
			CCVs = new ArrayList<String>();
			Addresses = new ArrayList<String>();
		}
		
		public List<Integer> getIDs() throws SQLException {
			ResultSet results = SelectQuery(Tables.USER_ATTR_USERID, Tables.CREDIT_CARD_TABLENAME, statement);
			addIntegersToList(results, userIDs, Tables.USER_ATTR_USERID );
			return userIDs;
		}
		public List<String> getCardNumbers() throws SQLException {
			ResultSet results = SelectQuery(Tables.CREDIT_CARD_ATTR_CARDNUM, Tables.CREDIT_CARD_TABLENAME, statement);
			addStringsToList(results, CardNumbers, Tables.CREDIT_CARD_ATTR_CARDNUM );
			return CardNumbers;
		}
		public List<String> getCCVs() throws SQLException {
			ResultSet results = SelectQuery(Tables.CREDIT_CARD_ATTR_CCV, Tables.CREDIT_CARD_TABLENAME, statement);
			addStringsToList(results, CCVs, Tables.CREDIT_CARD_ATTR_CCV );
			return CCVs;
		}
		public List<String> getAddresses() throws SQLException {
			ResultSet results = SelectQuery(Tables.CREDIT_CARD_ATTR_ADDRESS, Tables.CREDIT_CARD_TABLENAME, statement);
			addStringsToList(results, Addresses, Tables.CREDIT_CARD_ATTR_ADDRESS );
			return Addresses;
		}
	}
	
	public class DBgames extends DBQueriesHelper{
		private Statement statement;
		
		private List<String> gameNames;
		private List<String> descriptions;
		private List<String> genres;
		private List<String> developers;
		private List<Float> ratings;
		private List<Float> prices;
		private List<Boolean> onSpecials;
		private List<Float> discounts;
		
		DBgames(Statement aStatement) {
			statement = aStatement;		
			gameNames = new ArrayList<String>();
			descriptions = new ArrayList<String>();
			genres = new ArrayList<String>();
			developers = new ArrayList<String>();
			ratings = new ArrayList<Float>();
			prices = new ArrayList<Float>();
			onSpecials = new ArrayList<Boolean>();
			discounts = new ArrayList<Float>();
		}
		
		public List<String> getNames() throws SQLException {
			ResultSet results = SelectQuery(Tables.GAME_ATTR_NAME, Tables.FINALIZED_GAME_TABLENAME, statement);
			addStringsToList(results, gameNames, Tables.GAME_ATTR_NAME );
			return gameNames;
		}
		public List<String> getDescriptions() throws SQLException {
			ResultSet results = SelectQuery(Tables.GAME_ATTR_DESCRIPTION, Tables.FINALIZED_GAME_TABLENAME, statement);
			addStringsToList(results, descriptions, Tables.GAME_ATTR_DESCRIPTION );
			return descriptions;
		}
		public List<String> getGenres() throws SQLException {
			ResultSet results = SelectQuery(Tables.GAME_ATTR_GENRE, Tables.FINALIZED_GAME_TABLENAME, statement);
			addStringsToList(results, genres, Tables.GAME_ATTR_GENRE );
			return genres;
		}
		public List<String> getDevelopers() throws SQLException {
			ResultSet results = SelectQuery(Tables.GAME_ATTR_DEVELOPER, Tables.FINALIZED_GAME_TABLENAME, statement);
			addStringsToList(results, developers, Tables.GAME_ATTR_DEVELOPER );
			return developers;
		}
		public List<Float> getRatings() throws SQLException {
			ResultSet results = SelectQuery(Tables.FINALIZED_GAME_ATTR_RATING, Tables.FINALIZED_GAME_TABLENAME, statement);
			addFloatsToList(results, ratings, Tables.FINALIZED_GAME_ATTR_RATING );
			return ratings;
		}
		public List<Float> getPrices() throws SQLException {
			ResultSet results = SelectQuery(Tables.FINALIZED_GAME_ATTR_FULLPRICE, Tables.FINALIZED_GAME_TABLENAME, statement);
			addFloatsToList(results, prices, Tables.FINALIZED_GAME_ATTR_FULLPRICE );
			return prices;
		}
		public List<Boolean> getOnSpecials() throws SQLException {
			ResultSet results = SelectQuery(Tables.FINALIZED_GAME_ATTR_ONSPECIAL, Tables.FINALIZED_GAME_TABLENAME, statement);
			addBooleansToList(results, onSpecials, Tables.FINALIZED_GAME_ATTR_ONSPECIAL );
			return onSpecials;
		}
		public List<Float> getDiscounts() throws SQLException {
			ResultSet results = SelectQuery(Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC, Tables.FINALIZED_GAME_TABLENAME, statement);
			addFloatsToList(results, discounts, Tables.FINALIZED_GAME_ATTR_DISCOUNTPERC );
			return discounts;
		}
	}
}

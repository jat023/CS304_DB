package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs.cs304.steemproject.access.oraclejdbc.connection.SteemOracleDbConnector;


public class DBCustomerAccessor {
	private static Connection fConnection;
	private Statement statement;
	
	public DBCustomerAccessor() throws SQLException {
		fConnection = SteemOracleDbConnector.getDefaultConnection();
		statement = fConnection.createStatement();
	}
	
	public List<Integer> getIDs() throws SQLException {
		List<Integer> customerIDs = new ArrayList<Integer>();
		String query = "SELECT "+ Tables.USER_ATTR_USERID + " FROM " + Tables.CUSTOMER_TABLENAME;
		ResultSet results = statement.executeQuery(query);
		
		int i = 0;
		while( results.next() ) {
			customerIDs.add(i, results.getInt(Tables.USER_ATTR_USERID));
			i++;
		}
		
		return customerIDs;
	}
	
	public List<String> getEmails() throws SQLException {
		List<String> customerEmails = new ArrayList<String>();
		String query = "SELECT "+ Tables.USER_ATTR_EMAIL + " FROM " + Tables.CUSTOMER_TABLENAME;
		ResultSet results = statement.executeQuery(query);
		
		int i = 0;
		while( results.next() ) {
			customerEmails.add(i, results.getString(Tables.USER_ATTR_EMAIL));
			i++;
		}
		
		return customerEmails;
	}
	
	public List<String> getPasswords() throws SQLException {
		List<String> customerPasswords = new ArrayList<String>();
		String query = "SELECT "+ Tables.USER_ATTR_PASSWORD + " FROM " + Tables.CUSTOMER_TABLENAME;
		ResultSet results = statement.executeQuery(query);
		
		int i = 0;
		while( results.next() ) {
			customerPasswords.add(i, results.getString(Tables.USER_ATTR_PASSWORD));
			i++;
		}
		
		return customerPasswords;
	}
}

package ca.ubc.cs.cs304.steemproject.access.oraclejdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBQueriesHelper {
	public ResultSet SelectQuery(String select, String from, Statement statement) throws SQLException {
		String query = "SELECT "+ select + " FROM " + from;
		ResultSet results = statement.executeQuery(query);
		return results;
	}
	
	public void addIntegersToList(ResultSet aResults, List<Integer> targetList, String input) throws SQLException {
		int i = 0;
		while( aResults.next() ) {
			targetList.add(i, aResults.getInt(input));
			i++;
		}
	}
	
	public void addStringsToList(ResultSet aResults, List<String> targetList, String input) throws SQLException {
		int i = 0;
		while( aResults.next() ) {
			targetList.add(i, aResults.getString(input));
			i++;
		}
	}
	
	public void addFloatsToList(ResultSet aResults, List<Float> targetList, String input) throws SQLException {
		int i = 0;
		while( aResults.next() ) {
			targetList.add(i, aResults.getFloat(input));
			i++;
		}
	}
	
	public void addBooleansToList(ResultSet aResults, List<Boolean> targetList, String input) throws SQLException {
		int i = 0;
		while( aResults.next() ) {
			targetList.add(i, aResults.getBoolean(input));
			i++;
		}
	}
}

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class oracleDB {
	public static void main(String[] argv) throws SQLException {
		
		System.out.println("-------- Oracle JDBC Connection Testing ------");
		 
		//DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
 
		try  {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Oracle JDBC Driver Registered!");
		}
		catch(ClassNotFoundException ex) {
			System.out.println("Unable to load driver class!\n");
			System.exit(1);
		}
		
		
 
		Connection connection = null;
 
		try {
 
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_j5m8", "a10798122");
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
 
		}
 
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		
		
	}
}
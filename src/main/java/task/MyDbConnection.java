package task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDbConnection {
	private static final Logger LOGGER = Logger.getLogger(MyDbConnection.class.getName());

	public static Connection con = null;

	public static Connection getCon() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/skandi", "root", "");
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, "ClassNotFoundException", e);
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "SQLException", e);
		}
		return con;
	}
}

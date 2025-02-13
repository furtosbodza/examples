package task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NumberModel {

	private static final Logger LOGGER = Logger.getLogger(NumberModel.class.getName());

	public void saveNyeroszamok(Number data) {
		Connection con = MyDbConnection.getCon();
		try {
			int i = 1;
			PreparedStatement stmt = con.prepareStatement("insert into nyeroszamok (sz1, sz2, sz3, sz4, sz5, sz6) values (?,?,?,?,?,?)");
			stmt.setInt(i, data.getSz1());
			stmt.setInt(++i, data.getSz2());
			stmt.setInt(++i, data.getSz3());
			stmt.setInt(++i, data.getSz4());
			stmt.setInt(++i, data.getSz5());
			stmt.setInt(++i, data.getSz6());

			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Hiba a Tips tábla beszúrás során!", e);
		}
	}

}

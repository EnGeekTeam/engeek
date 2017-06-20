package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.utils.Utils;

@Service
public class UserDeckDAOImpl implements UserDeckDAO {

	@Autowired
	private DataSource dataSource;

	@Override
	public void save(String deckId, int userId) throws Exception {
		if (!isExists(deckId, userId)) {
			Connection connection = null;
			String sql = "insert into userdeck(createdAt,interactedTime,status,decks_id,user_id) values (?,?,?,?,?)";
			try {
				connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, Utils.getDate());
				ps.setString(2, Utils.getDate());
				ps.setInt(3, 0);
				ps.setString(4, deckId);
				ps.setInt(5, userId);
				ps.execute();
				ps.close();
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			} finally {
				if (connection != null)
					connection.close();
			}
		}

	}

	@Override
	public boolean isExists(String deckId, int userId) throws Exception {
		Connection connection = null;
		String sql = String.format("select count(*) as count from userdeck where user_id=%d and decks_id='%s'", userId,
				deckId);

		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt("count");
				if (count > 0) {
					st.close();
					return true;
				}
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
		return false;
	}

}

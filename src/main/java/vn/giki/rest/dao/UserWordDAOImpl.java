package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.utils.Utils;

@Service
public class UserWordDAOImpl implements UserWordDAO {

	@Autowired
	private DataSource dataSource;

	@Override
	public boolean isExists(int userId, String wordId) throws SQLException {
		Connection connection = null;
		String sql = String.format("select count(*) as count from userword where user_id = %d and word_id = '%s'",
				userId, wordId);

		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt("count");
				if (count > 0) {
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

	@Override
	public void insertUserWord(int userId, String wordId) throws SQLException {
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			String sql = String.format(
					"insert into userword(user_id,word_id,ranking,interactedTime,createdAt) values (?,?,?,?,?)");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, wordId);
			ps.setInt(3, 0);
			ps.setString(4, Utils.getDateTime());
			ps.setString(5, Utils.getDateTime());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public void update(int userId, String wordId, boolean isWrong) throws SQLException {
		Connection connection = null;
		int value = (isWrong) ? 1 : 0;
		String sql = String.format(
				"update userword SET totalNumberofReview=(totalNumberofReview+1), totalNumberOfWrong=(totalNumberOfWrong+%d) WHERE user_id = %d and word_id = '%s'",
				value, userId, wordId);
		
		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

	}

}

package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.User;

@Service
public class FriendDAOImpl implements FriendDAO {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDAO userDAO;

	@Override
	public void save(int userId, int userFriend) throws Exception {
		if (!isExists(userId, userFriend)) {

			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				String sql = "insert into friend(user_id,friend_id) values (?,?)";
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setInt(1, userId);
				ps.setInt(2, userFriend);
				ps.executeUpdate();
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
	public boolean isExists(int userId, int userFriendId) throws Exception {
		Connection connection = null;
		String sql = String.format("select count(*) as count from friend where user_id=%d and friend_id=%d", userId,
				userFriendId);
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

	@Override
	public void remove(int userId, int userFriend) throws Exception {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String sql = String.format("delete from friend where user_id=%d and friend_id=%d", userId, userFriend);
			Statement statement = connection.createStatement();
			statement.execute(sql);
			statement.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public List<User> saveListByUserIdPlatform(int userId, List<String> listIdPlatform, String platform)
			throws Exception {
		int friendId;
		List<Integer> listId = new ArrayList<>();
		for (String idPlatform : listIdPlatform) {
			friendId = userDAO.getIdUser(idPlatform, platform);
			save(userId, friendId);
			listId.add(friendId);
		}

		return getListByListId(listId);

	}

	@Override
	public void removeListUserIdPlatform(int userId, List<Integer> listFriendId) throws Exception {
		for (Integer friendId : listFriendId) {
			remove(userId, friendId);
		}

	}

	@Override
	public int getSizeListFriend(int userId) throws Exception {
		Connection connection = null;
		String sql = String.format("select count(*) as count from friend where user_id=%d", userId);

		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt("count");
				st.close();
				return count;
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
		return 0;
	}

	@Override
	public List<User> getListFriend(int userId, int size, int page) throws Exception {
		Connection connection = null;
		List<User> result = new ArrayList<>();
		page = page - 1;
		int start = page * size, end = page * size + size;
		String sql = String.format(
				"select u.id, googleId,facebookId,name, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user u INNER JOIN friend f on u.id=f.friend_id where f.user_id=%d order by total desc limit %d, %d",
				userId, start, end);

		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			System.out.println(sql);
			User user;
			while (rs.next()) {
				user = new User();
				user.setName(rs.getString("name"));
				user.setId(rs.getInt("id"));
				user.setGoogleId(rs.getString("googleId"));
				user.setFacebookId(rs.getString("facebookId"));
				user.setAvatarUrl(rs.getString("avatarUrl"));
				user.setScoreGame1(rs.getInt("game1_max_score"));
				user.setScoreGame2(rs.getInt("game2_max_score"));
				user.setScoreGame3(rs.getInt("game3_max_score"));
				user.setScoreTotal(rs.getInt("total"));
				result.add(user);
			}
			rs.close();
			statement.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

		return result;
	}

	public List<User> getListByListId(List<Integer> listId) throws SQLException {
		Connection connection = null;
		List<User> result = new ArrayList<>();

		StringBuffer stringBuffer = new StringBuffer();
		for (Integer tmp : listId) {
			stringBuffer.append(tmp);
			stringBuffer.append(",");
		}
		String value = stringBuffer.toString().substring(0, stringBuffer.lastIndexOf(","));
		String sql = String.format(
				"select u.id, googleId,facebookId,name, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user u WHERE id in (%s)",
				value);

		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			User user;
			while (rs.next()) {
				user = new User();
				user.setName(rs.getString("name"));
				user.setId(rs.getInt("id"));
				user.setGoogleId(rs.getString("googleId"));
				user.setFacebookId(rs.getString("facebookId"));
				user.setAvatarUrl(rs.getString("avatarUrl"));
				user.setScoreGame1(rs.getInt("game1_max_score"));
				user.setScoreGame2(rs.getInt("game2_max_score"));
				user.setScoreGame3(rs.getInt("game3_max_score"));
				user.setScoreTotal(rs.getInt("total"));
				result.add(user);
			}

			rs.close();
			st.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

	}

}

package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.User;

@Service
public class FriendDAOImpl implements FriendDAO {

	@Autowired
	private Connection connection;

	@Autowired
	private UserDAO userDAO;

	@Override
	public void save(int userId, int userFriend) throws Exception {
		if (!isExists(userId, userFriend)) {
			String sql = "insert into friend(user_id,friend_id) values (?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, userFriend);
			ps.executeUpdate();
			ps.close();
		}
	}

	@Override
	public boolean isExists(int userId, int userFriendId) throws Exception {
		String sql = String.format("select count(*) as count from friend where user_id=%d and friend_id=%d", userId,
				userFriendId);
		Statement st = connection.createStatement();

		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			int count = rs.getInt("count");
			if (count > 0) {
				st.close();
				return true;
			}
		}
		st.close();
		return false;
	}

	@Override
	public void remove(int userId, int userFriend) throws Exception {
		String sql = String.format("delete from friend where user_id=%d and friend_id=%d", userId, userFriend);
		Statement statement = connection.createStatement();
		statement.execute(sql);
		statement.close();
	}

	@Override
	public void saveListByUserIdPlatform(int userId, List<String> listIdPlatform, String platform) throws Exception {
		int friendId;
		for (String idPlatform : listIdPlatform) {
			friendId = userDAO.getIdUser(idPlatform, platform);
			save(userId, friendId);
		}

	}

	@Override
	public void removeListUserIdPlatform(int userId, List<String> listIdPlatform, String platform) throws Exception {
		int friendId;
		for (String idPlatform : listIdPlatform) {
			friendId = userDAO.getIdUser(idPlatform, platform);
			remove(userId, friendId);
		}

	}

	@Override
	public int getSizeListFriend(int userId) throws Exception {
		String sql = String.format("select count(*) as count from friend where user_id=%d", userId);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			int count = rs.getInt("count");
			st.close();
			return count;
		}
		st.close();
		return 0;
	}

	@Override
	public List<User> getListFriend(int userId, int size, int page) throws Exception {
		List<User> result = new ArrayList<>();

		page = page - 1;
		int start = page * size, end = page * size + size;

		Statement statement = connection.createStatement();

		String sql = String.format(
				"select u.id, googleId,facebookId,name, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user u INNER JOIN friend f on u.id=f.friend_id where f.user_id=%d order by total desc limit %d, %d",
				userId, start, end);

		ResultSet rs = statement.executeQuery(sql);

		System.out.println(sql);
		User user;
		while (rs.next()) {
			user = new User();
			user.setName(rs.getString("name"));
			user.setId(rs.getInt("id"));
			user.setGoogleId(rs.getString("googleId"));
			user.setAvatarUrl(rs.getString("avatarUrl"));
			user.setScoreGame1(rs.getInt("game1_max_score"));
			user.setScoreGame2(rs.getInt("game2_max_score"));
			user.setScoreGame3(rs.getInt("game3_max_score"));
			user.setScoreTotal(rs.getInt("total"));
			result.add(user);
		}
		rs.close();

		statement.close();

		return result;
	}

}

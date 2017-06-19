package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.Deck;
import vn.giki.rest.entity.Package;
import vn.giki.rest.entity.User;
import vn.giki.rest.utils.Constant;
import vn.giki.rest.utils.Utils;

@Service
public class UserDAOImpl implements UserDAO {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserPackageDAO userPackDao;

	@Autowired
	private DeckDAO deckDAO;

	@Autowired
	private UserDeckDAO userDeckDao;

	@Autowired
	private PackageDAO packageDAO;
	

	@Override
	public int insertUser(Map<String, Object> u) throws Exception {
		Connection connection = null;
		String sql = "insert into user(email,facebookId,googleId,token,tokenClient,created,name,gender,avatarUrl,hint) values (?,?,?,?,?,?,?,?,?,?)";

		try {
			connection = dataSource.getConnection();

			PreparedStatement st = connection.prepareStatement(sql);
			st.setString(1, (String) u.get("email"));
			st.setString(2, (String) u.get("facebookId"));
			st.setString(3, (String) u.get("googleId"));
			st.setString(4, (String) u.get("token"));
			st.setString(5, (String) u.get("tokenClient"));
			st.setString(6, (String) u.get("created"));
			st.setString(7, (String) u.get("name"));
			st.setString(8, (String) u.get("gender"));
			st.setString(9, (String) u.get("avatarUrl"));
			st.setInt(10, (int) u.get("hint"));
			st.executeUpdate();
			st.close();

			// create client token
			String getId = String.format("select id from user where email='%s'", (String) u.get("email"));
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(getId);
			int userId = 0;
			if (rs.next()) {
				userId = rs.getInt("id");
				userPackDao.save(Constant.PACKAGE.ID_FREE, userId);

				for (Deck d : deckDAO.getListByIdPac(Constant.PACKAGE.ID_FREE)) {
					if (!d.isPremium()) {
						userDeckDao.save(d.getId(), userId);
					}
				}

				return userId;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

		return 0;

	}

	@Override
	public int countPage(String value) throws SQLException {
		Connection connection = null;
		String sql = String.format("SELECT count(*) as count FROM user where googleId in (%s)", value);
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt("count");
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
	public boolean isExistsUser(int userId) throws SQLException {
		Connection connection = null;
		String sql = String.format("select count(*) as count from user where id = %d", userId);

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
	public List<User> getListFriends(String value, int size, int page) throws SQLException {
		Connection connection = null;
		List<User> result = new ArrayList<>();
		page = page - 1;
		int start = page * size, end = page * size + size;

		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			String sql = String.format(
					"select id, googleId, facebookId,name, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user as u where googleId in (%s) order by total desc limit %d, %d",
					value, start, end);
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

	@Override
	public void updatePurches(int userId, long paymentTime, long paymentExpire, int paymentStatus, String type)
			throws Exception {
		Connection connection = null;
		String sql = String.format(
				"update user set paymentTime='%s', paymentStatus=%d, expiredDate='%s', type='%s' where id=%d",
				Utils.getDate(paymentTime), paymentStatus, Utils.getDate(paymentExpire), type, userId);

		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			statement.execute(sql);
			statement.close();

			System.out.println(paymentStatus + "stt");

			if (paymentStatus == Constant.USER.STATE_PAYMENT_PAID) {
				List<Package> packages = packageDAO.getAll();
				for (Package p : packages) {
					for (Deck d : deckDAO.getListByIdPac(p.getId())) {

						userDeckDao.save(d.getId(), userId);
					}
				}

			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	@Override
	public User getScore(int userId) throws Exception {
		Connection connection = null;
		User user = new User();
		String sql = String.format(
				"select id, name,hint, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user as u where u.id = %d",
				userId);

		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				user.setId(userId);
				user.setName(rs.getString("name"));
				user.setHint(rs.getInt("hint"));
				user.setScoreGame1(rs.getInt("game1_max_score"));
				user.setScoreGame2(rs.getInt("game2_max_score"));
				user.setScoreGame3(rs.getInt("game3_max_score"));
				user.setScoreTotal(rs.getInt("total"));
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
		return user;
	}

	@Override
	public int getIdUser(String userIdPlatform, String platform) throws SQLException {
		Connection connection = null;
		String sql = "";
		if (platform.equals(Constant.PLATFORM.GOOGLE)) {
			sql = String.format("select id from user where googleId='%s'", userIdPlatform);
		} else if (platform.equals(Constant.PLATFORM.FACEBOOK)) {
			sql = String.format("select id from user where facebookId='%s'", userIdPlatform);
		}
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int id = rs.getInt("id");
				rs.close();
				st.close();
				return id;
			} else {
				rs.close();
				st.close();
				throw new SQLException("Id user not found");
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public void updateUser(String sql) throws Exception {
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			st.execute(sql);
			st.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	@Override
	public void updateClientToken(int userId, String token) throws Exception {
		Connection connection = null;
		String sql = String.format("update user set tokenClient='%s' where id=%d", token, userId);
		System.out.println(sql);
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			st.execute(sql);
			st.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

	}

	@Override
	public boolean checkClientToken(int userId, String token) throws Exception {
		Connection connection = null;
		String sql = String.format("select tokenClient from user where id=%d", userId);
		System.out.println(sql);
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				String tokenUser = rs.getString("tokenClient");
				if (tokenUser.equals(token)) {
					return true;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
		return false;
	}

}

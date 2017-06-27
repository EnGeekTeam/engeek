package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.bouncycastle.math.ec.custom.sec.SecT113Field;
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
		String sql = "insert into user(email,facebookId,googleId,token,tokenClient,created,name,gender,avatarUrl,hint,uniquecode) values (?,?,?,?,?,?,?,?,?,?,?)";

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
			st.setString(11, (String) u.get("uniquecode"));
			st.executeUpdate();
			st.close();

			// create client token
			String facebbookId = (String) u.get("facebookId");
			String googleId = (String) u.get("googleId");
			
			String sqlGetId;
			if (facebbookId.isEmpty()){
				sqlGetId = String.format("select id from user where googleId='%s'", googleId);
			} else  if (googleId.isEmpty()){
				sqlGetId = String.format("select id from user where facebookId='%s'", facebbookId);
			} else {
				sqlGetId = String.format("select id from user where email='%s'", (String) u.get("email"));
			}
			
			System.out.println(sqlGetId);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sqlGetId);
			int userId = 0;
			if (rs.next()) {
				userId = rs.getInt("id");
				userPackDao.save(Constant.PACKAGE.ID_FREE, userId);
				System.out.println("insert: ID" + userId);
				for (Deck d : deckDAO.getListByIdPac(Constant.PACKAGE.ID_FREE)) {
					if (!d.isPremium()) {
						System.out.println("--");
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
	public void updatePurches(int userId, long paymentTime, long paymentExpire, int paymentStatus, String type, boolean isUpGold)
			throws Exception {
		Connection connection = null;
		long time = System.currentTimeMillis();
		String sql;
		if (isUpGold){
			sql= String.format(
					"update user set paymentTime='%s', paymentStatus=%d, expiredDate='%s', type='%s', hint=hint+%d, lastUpHint=%d where id=%d",
					Utils.getDateTime(paymentTime), paymentStatus, Utils.getDateTime(paymentExpire), type,Constant.USER.HINT_GOLD,time, userId);
		} else {
			sql= String.format(
					"update user set paymentTime='%s', paymentStatus=%d, expiredDate='%s', type='%s', lastUpHint=%d where id=%d",
					Utils.getDateTime(paymentTime), paymentStatus, Utils.getDateTime(paymentExpire), type,time, userId);
		}
		

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
				"select id, name,googleId,facebookId,hint, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user as u where u.id = %d",
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
	
	public User getUser(int idUser) throws SQLException{
		Connection connection =null;
		String sql = String.format("select * from user where id=%d", idUser);
		User user=null;
		try {
			connection = dataSource.getConnection();
			
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()){
				user= new User();
				user.setId(rs.getInt("id"));
				user.setFacebookId(rs.getString("facebookId"));
				user.setGoogleId(rs.getString("googleId"));
				user.setHint(rs.getInt("hint"));
				user.setLastUpHInt(rs.getLong("lastUpHint"));
				user.setPaymentStatus(rs.getInt("paymentStatus"));
				
				rs.close();
				st.close();
				return user;
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection!=null)connection.close();
		}
		return user;
	}

	@Override
	public void checkUpHint(int userId) throws SQLException{
		User user = getUser(userId);
		long time = System.currentTimeMillis();
		
		if (user.getPaymentStatus()==Constant.USER.STATE_PAYMENT_PAID){
			long lastUpHint = user.getLastUpHInt();
			long space = (time-lastUpHint);
			long cost = (long)Constant.USER.DATE_UP_HINT*(long)86400000;
			System.out.println("cost first: " +cost);
			long tmp=0;
				int	i=0;
			while(true){
				tmp=space-cost;
				if (tmp>=0){
					i++;
					space = space-cost;
					lastUpHint+=cost;
				} else {
					break;
				}
			}
			
			if (i>0){
				System.out.println("update 30 day");
				Connection connection=null;
				int hintUp = Constant.USER.HINT_GOLD*i;
				String sql = String.format("update user set hint=hint+%d, lastUpHint=%d where id=%d", hintUp, lastUpHint, userId);
				try {
					connection = dataSource.getConnection();
					Statement st = connection.createStatement();
					st.execute(sql);
					st.close();
					
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				} finally {
					if (connection!=null)connection.close();
				}
				
			}
			
		}
		
		
		
		
	}
	/*@Override
	public boolean isAvailableForBonus(String uniqueCode, int friendID) throws SQLException {
		Connection connection = null;
		String sql = String.format("select count(*) as count from user where user.uniquecode = %s", uniqueCode);
		String sql2 = String.format("select count(*) as count from user where user.id = %d", friendID);
		try{
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int countCode = 0;
			int countUser = 0;
			if (rs.next()){
				countCode = rs.getInt("count");
			}
			rs = st.executeQuery(sql2);
			if(rs.next()){
				countUser = rs.getInt("count");
			}
			if(countCode >0 && countUser > 0)
				return true;
			rs.close();
			st.close();
		}catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}finally{
			if(connection != null)
				connection.close();
		}
		return false;
	}*/

	@Override
	public void updateUserExpiredDate(int friendID, String uniqueCode) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = null;
		String sql = String.format("select count(*) as count from user where user.uniquecode = '%s'", uniqueCode);
		String sqlplus = String.format("select count(*) as count from user where user.id = %d", friendID);
		String sql1 = String.format("Select UNIX_TIMESTAMP(user.expiredDate) as date, user.status from user where user.id = %d", friendID);
		String sql2 = String.format("Select UNIX_TIMESTAMP(user.expiredDate) as date from user where user.uniquecode = '%s'", uniqueCode);
		String sql3 = String.format("update user set user.expiredDate = date_add(user.expiredDate,INTERVAL +7 DAY), status = true where user.id = %d", friendID);
		String sql4 = String.format("update user set user.expiredDate = date_add(NOW(),INTERVAL +7 DAY), status = true where user.id = %d", friendID);
		String sql5 = String.format("update user set user.expiredDate = date_add(user.expiredDate,INTERVAL +7 DAY), invitedFriend = invitedFriend + 1 where user.uniquecode = '%s'", uniqueCode);
		String sql6 = String.format("update user set user.expiredDate = date_add(NOW(),INTERVAL +7 DAY), invitedFriend = invitedFriend + 1 where user.uniquecode = '%s'", uniqueCode);
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
	//		ResultSet rs = st.executeQuery(sql1);
			ResultSet rs = st.executeQuery(sql);
			int countCode = 0;
			int countUser = 0;
			if (rs.next()){
				countCode = rs.getInt("count");
			}
			System.out.println(countCode);
			rs = st.executeQuery(sqlplus);
			if(rs.next()){
				countUser = rs.getInt("count");
			}
			System.out.println(countUser);
			if(countCode >0 && countUser > 0){
				rs = st.executeQuery(sql1);
				long timeMillis = System.currentTimeMillis();
				long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
				while(rs.next()){
					long time = rs.getLong("date");
					boolean status = rs.getBoolean("status");
					System.out.println(status);
					System.out.println(time);
					if((!rs.wasNull()) && (timeSeconds < time)){
						if(status==false)
							st.execute(sql3);
						else
							throw new Exception("Already inserted code");
					}
					else
						st.execute(sql4);
				}
				rs = st.executeQuery(sql2);
				while(rs.next()){
					long time = rs.getLong("date");
					if((!rs.wasNull()) && timeSeconds < time){
						st.execute(sql5);
					}
					else
						st.execute(sql6);
				}
			}
			
		} catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}finally{
			if(connection != null)
				connection.close();
		}
		
	}
}

package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.Deck;
import vn.giki.rest.entity.User;
import vn.giki.rest.utils.Constant;
import vn.giki.rest.utils.Utils;

@Service
public class UserDAOImpl implements UserDAO {

	@Autowired
	private Connection connection;

	@Autowired
	private UserPackageDAO userPackDao;

	@Autowired
	private DeckDAO deckDAO;

	@Autowired
	private UserDeckDAO userDeckDao;

	@Override
	public int insertUser(Map<String, Object> u) throws Exception {
		String sql = "insert into user(email,facebookId,googleId,token,tokenClient,created,name,gender,avatarUrl,hint) values (?,?,?,?,?,?,?,?,?,?)";
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
			String sqlUpdateTokenClient = String.format("update user set tokenClient='%s' where id=%d",
					Utils.encodeJWT(String.valueOf(userId)), userId);
			Statement stt = connection.createStatement();
			stt.execute(sqlUpdateTokenClient);
			rs.close();

			userPackDao.save(Constant.PACKAGE.ID_FREE, userId);

			for (Deck d : deckDAO.getListByIdPac(Constant.PACKAGE.ID_FREE)) {
				if (!d.isPremium()) {
					userDeckDao.save(d.getId(), userId);
				}
			}

			return userId;
		}

		return 0;

	}

	@Override
	public int countPage(int size) throws SQLException {
		String sql = String.format("SELECT round(count(*)/%d) as count FROM user", size);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			int count = rs.getInt("count");
			return count;
		}
		return 0;

	}

	@Override
	public boolean isExistsUser(int userId) throws SQLException {
		String sql = String.format("select count(*) as count from user where id = %d", userId);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			int count = rs.getInt("count");
			if (count > 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<User> getListFriends(List<String> listId, int fOrg) throws SQLException{
		List<User> result = new ArrayList<>();
		
		String sql;
		User user;
		Statement statement = connection.createStatement();
		ResultSet rs;
		String ifQuery = (fOrg==1)?"facebookId":"googleId";
		
		for (String item : listId){
			sql = String.format("select id, name, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user as u where %s='%s'", ifQuery,item);
			rs = statement.executeQuery(sql);
			if (rs.next()){
				user = new User();
				user.setName(rs.getString("name"));
				user.setAvatarUrl(rs.getString("avatarUrl"));
				user.setScoreGame1(rs.getInt("game1_max_score"));
				user.setScoreGame2(rs.getInt("game2_max_score"));
				user.setScoreGame3(rs.getInt("game3_max_score"));
				user.setScoreTotal(rs.getInt("total"));
				result.add(user);
			}
			rs.close();
			
		}
		
		statement.close();
		
		return result;
		
	}
	
	@Override
	public void updatePurches(int userId, long paymentTime, long paymentExpire, int paymentStatus) throws SQLException{
		String sql = String.format("update user set paymentTime='%s', paymentStatus=%d, expiredDate='%s' where id=%d", 
				Utils.getDate(paymentTime),
				paymentStatus,
				Utils.getDate(paymentExpire),
				userId);
		
		Statement statement = connection.createStatement();
		statement.execute(sql);
		statement.close();
		
	}

}

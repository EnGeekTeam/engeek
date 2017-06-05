package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.Deck;
import vn.giki.rest.utils.Constant;
import vn.giki.rest.utils.Utils;

@Service
public class UserDAOImpl implements UserDAO{
	
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
		int userId=0;
		if (rs.next()){
			userId = rs.getInt("id");
			String sqlUpdateTokenClient = String.format("update user set tokenClient='%s' where id=%d", Utils.encodeJWT(String.valueOf(userId)), userId);
			Statement stt = connection.createStatement();
			stt.execute(sqlUpdateTokenClient);
			rs.close();
			
			userPackDao.save(Constant.PACKAGE.ID_FREE, userId);
			
			for  (Deck d : deckDAO.getListByIdPac(Constant.PACKAGE.ID_FREE)){
				if (!d.isPremium()){
					userDeckDao.save(d.getId(), userId);
				}
			}
			
			return userId;
		}
		
		return 0;
		
	}
	
	@Override
	public int countPage(int size) throws SQLException{
		String sql = String.format("SELECT round(count(*)/%d) as count FROM user", size);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()){
			int count = rs.getInt("count");
			return count;
		}
		return 0;
		
	}
	
}

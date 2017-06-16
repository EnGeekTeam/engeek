package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.utils.Utils;

@Service
public class UserDeckDAOImpl implements UserDeckDAO {

	@Autowired
	private Connection connection;

	@Override
	public void save(String deckId, int userId) throws Exception {
		if (!isExists(deckId, userId)) {

			String sql = "insert into userdeck(createdAt,interactedTime,status,decks_id,user_id) values (?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, Utils.getDate());
			ps.setString(2, Utils.getDate());
			ps.setInt(3, 0);
			ps.setString(4, deckId);
			ps.setInt(5, userId);
			ps.execute();
			ps.close();
		}

	}

	@Override
	public boolean isExists(String deckId, int userId) throws Exception {
		String sql = String.format("select count(*) as count from userdeck where user_id=%d and decks_id='%s'", userId,
				deckId);
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

}

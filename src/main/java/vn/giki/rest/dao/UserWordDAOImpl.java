package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.utils.Utils;

@Service
public class UserWordDAOImpl implements UserWordDAO{

	@Autowired
	private Connection connection;
	
	@Override
	public boolean isExists(int userId, String wordId) throws SQLException {
		String sql = String.format("select count(*) as count from userword where user_id = %d and word_id = '%s'", userId, wordId);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()){
			int count = rs.getInt("count");
			if (count>0){
				return true;
			}
		}
		return false;
	}

	@Override
	public void insertUserWord(int userId, String wordId) throws SQLException{
		String sql = String.format("insert into userword(user_id,word_id,ranking,interactedTime,createdAt) values (?,?,?,?,?)");
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, userId);
		ps.setString(2, wordId);
		ps.setInt(3, 0);
		ps.setString(4, Utils.getDate());
		ps.setString(5, Utils.getDate());
		ps.executeUpdate();
		ps.close();
	}

	@Override
	public void update(int userId, String wordId, boolean isWrong) throws SQLException {
		int value = (isWrong)?1:0;
		String sql = String.format("update userword SET totalNumberofReview=(totalNumberofReview+1), totalNumberOfWrong=(totalNumberOfWrong+%d) WHERE user_id = %d and word_id = '%s'", value,userId, wordId);
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);
		statement.close();
		
	}
	
	

}

package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameDAOImpl implements GameDAO {
	@Autowired
	private Connection connection;

	@Override
	public boolean isReadyForGame1(int userID) throws SQLException {
		// TODO Auto-generated method stub
		String sql = String.format("SELECT count(*) FROM word, userword WHERE word.id = userword.word_id and userword.user_id = %d and word.antonym is not null", userID);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()){
			int c = rs.getInt("count");
			if (c>0)
				return true;
		}
		return false;
	}

	@Override
	public boolean isReadyForGame2(int userID) throws SQLException {
		// TODO Auto-generated method stub
		String sql = String.format("SELECT count(*) as count FROM word, userword WHERE word.id = userword.word_id and userword.user_id = %d and word.sysnonym is not null", userID);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()){
			int c = rs.getInt("count");
			if (c>0)
				return true;
		}
		return false;
	}

	@Override
	public boolean isReadyForGame3(int userID) throws SQLException {
		// TODO Auto-generated method stub
		String sql = String.format("SELECT count(*) FROM userword WHERE userword.user_id = %d", userID);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()){
			int c = rs.getInt("count");
			if (c>0)
				return true;
		}
		return false;
	}

	@Override
	public List<String> game2list(int userID) throws SQLException {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		if(isReadyForGame2(userID)){
			String sql = String.format("SELECT userword.word_id FROM userword, word WHERE userword.word_id = word.id AND userword.user_id = %d and word.sysnonym is not null", userID);
			Statement st = connection.createStatement();
			ResultSet set = st.executeQuery(sql);
			while (set.next()){
				list.add(set.getString("word_id"));
			}
		}
		return list;
	}
	@Override
	public HashMap<String, Object> game2choice(String wordID) throws SQLException {
		// TODO Auto-generated method stub
		HashMap<String, Object> choices = new HashMap<>();
		String sql = String.format("SELECT word.sysnonym FROM word WHERE word.id != '%s' ORDER by RAND() LIMIT 3", wordID);
		Statement st = connection.createStatement();
		ResultSet set = st.executeQuery(sql);
		int i = 0;
		while (set.next()){
			choices.put("word"+i, set.getString("sysnonym"));
			System.out.println(set.getString("sysnonym"));
			i++;
		}
		String sql2 = String.format("SELECT word.sysnonym FROM word WHERE word.id = '%s'",  wordID);
		Statement st2 = connection.createStatement();
		ResultSet set2 = st2.executeQuery(sql2);
		while(set2.next()){
			choices.put("word", wordID);
			choices.put("sysnonym", set2.getString("sysnonym"));
		}
		for (String s: choices.keySet()){
			System.out.println(s + ":"+ choices.get(s));
		}
		return choices;
	}

	@Override
	public Map<String, Object> game2data(List<String> listword) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> result = new TreeMap<>();
		List<HashMap<String, Object>> list = new ArrayList<>();
		for (String word: listword){
			HashMap<String, Object> hashchoice = game2choice(word);
			list.add(hashchoice);
		}
		result.put("data", list.size()==0?"[]":list);
		return result;
	}
}

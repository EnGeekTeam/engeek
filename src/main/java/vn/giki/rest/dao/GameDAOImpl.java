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

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.Game1;
import vn.giki.rest.entity.Game2;
import vn.giki.rest.utils.SQLTemplate;

@Service
public class GameDAOImpl implements GameDAO {
	@Autowired
	private DataSource dataSource;

	@Override
	public boolean isReadyForGame1(int userID) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		String sql = String.format("SELECT count(*) as count FROM word, userword WHERE word.id = userword.word_id and userword.user_id = %d and word.antonym is not null", userID);
		try{
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				int c = rs.getInt("count");
				if (c>0)
					return true;
			}
			rs.close();
			st.close();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(connection!=null)
				connection.close();
		}
		
		return false;
	}

	@Override
	public boolean isReadyForGame2(int userID) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		String sql = String.format("SELECT count(*) as count FROM word, userword WHERE word.id = userword.word_id and userword.user_id = %d and word.sysnonym is not null", userID);
		try{
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				int c = rs.getInt("count");
				if (c>0)
					return true;
			}
			rs.close();
			st.close();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(connection!=null)
				connection.close();
		}
		return false;
	}

	@Override
	public boolean isReadyForGame3(int userID) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		String sql = String.format("SELECT count(*) as count FROM userword WHERE userword.user_id = %d", userID);
		try{
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				int c = rs.getInt("count");
				if (c>0)
					return true;
			}
			rs.close();
			st.close();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(connection!=null)
				connection.close();
		}
		
		return false;
	}

	@Override
	public List<String> game2list(int userID) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<String> list = new ArrayList<>();
		if(isReadyForGame2(userID)){
			String sql = String.format("SELECT userword.word_id FROM userword, word WHERE userword.word_id = word.id AND userword.user_id = %d and word.sysnonym is not null", userID);
			try{
				connection = dataSource.getConnection();
				Statement st = connection.createStatement();
				ResultSet set = st.executeQuery(sql);
				while (set.next()){
					list.add(set.getString("word_id"));
				}
				set.close();
				st.close();
			}catch(Exception e){
				throw new Exception(e.getMessage());
			}finally{
				if(connection!=null)
					connection.close();
			}
			
		}
		return list;
	}
	@Override
	public HashMap<String, Object> game2choice(String wordID, int userId) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		HashMap<String, Object> choices = new HashMap<>();
		String sql = String.format("SELECT word.sysnonym FROM word WHERE word.id != '%s' and word.sysnonym is not null ORDER by RAND() LIMIT 3", wordID);
		String sql2 = String.format("SELECT word.sysnonym, UNIX_TIMESTAMP(userword.interactedTime) as lastReview FROM word, userword WHERE userword.word_id = word.id and word.id = '%s' and userword.user_id= %d", wordID, userId);
		try{
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet set = st.executeQuery(sql);
			int i = 0;
			while (set.next()){
				choices.put("word"+i, set.getString("sysnonym"));
				System.out.println(set.getString("sysnonym"));
				i++;
			}
			set = st.executeQuery(sql2);
			if(set.next()){
				choices.put("synonym", set.getString("sysnonym"));
				choices.put("lastReview", set.getLong("lastReview"));
			}
			
			set.close();
			st.close();
			/*String sql2 = String.format("SELECT word.sysnonym FROM word WHERE word.id = '%s'",  wordID);
			Statement st2 = connection.createStatement();
			ResultSet set2 = st2.executeQuery(sql2);
			while(set2.next()){
				choices.put("word", wordID);
				choices.put("sysnonym", set2.getString("sysnonym"));
			}
			set2.close();
			st2.close();*/
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(connection!=null)
				connection.close();
		}
		
		for (String s: choices.keySet()){
			System.out.println(s + ":"+ choices.get(s));
		}
		return choices;
	}

	@Override
	public List<Game2> game2data(List<String> listword, int userId) throws Exception {
		// TODO Auto-generated method stub
		List<Game2> listgame2data = new ArrayList<>();
		for (String w : listword){
			String word = w;
			HashMap<String, Object> choice = game2choice(w, userId);
			String choice1 = choice.get("word0").toString();
			String choice2 = choice.get("word1").toString();
			String choice3 = choice.get("word2").toString();
			String synonym = choice.get("synonym").toString();
			long lastReview = (long)choice.get("lastReview");
			listgame2data.add(new Game2(word,synonym, choice1, choice2, choice3,lastReview));
		}
		return listgame2data;
	}

	@Override
	public List<Game1> game1data(int userId) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<Game1> game1 = new ArrayList<>();
		try{
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(String.format(SQLTemplate.LIST_WORD_GAME1,userId));
			while (rs.next()){
				int numberFalse = rs.getInt("totalNumberOfWrong");
				int numberReview = rs.getInt("totalNumberOfReview");
				String antonym = rs.getString("antonym");
				String word = rs.getString("word_id");
				long lastReview = rs.getLong("time");
				game1.add(new Game1(numberFalse, numberReview, antonym, word, lastReview));
			}
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(connection!=null)
				connection.close();
		}
		return game1;
	}

	/*@Override
	public Map<String, Object> game2data(List<String> listword) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> result = new TreeMap<>();
		List<HashMap<String, Object>> list = new ArrayList<>();
		for (String word: listword){
			HashMap<String, Object> hashchoice = game2choice(word);
			list.add(hashchoice);
		}
		result.put("data", list.size()==0?"[]":list);
		return result;
	}*/
	
	
}

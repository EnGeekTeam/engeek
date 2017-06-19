package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.Deck;

@Service
public class DeckDAOImpl implements DeckDAO {
	
	@Autowired
	private DataSource dataSource;

	@Override
	public List<Deck> getListByIdPac(String packageId) throws Exception {
		Connection connection = null;
		List<Deck> result = new ArrayList<>();
		String sql = String.format("select * from deck where package_id = '%s'", packageId);

		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			Deck deck;
			while (rs.next()) {
				deck = new Deck();
				deck.setId(rs.getString("id"));
				deck.setMeaning(rs.getString("meaning"));
				deck.setMemo(rs.getString("memo"));
				deck.setName(rs.getString("name"));
				deck.setPicturePath(rs.getString("picturePath"));
				deck.setPremium(rs.getBoolean("isPremium"));
				deck.setPackageId(rs.getString("package_id"));
				result.add(deck);
			}
			
			rs.close();
			statement.close();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if  (connection!=null){
				connection.close();
			}
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getInfoById(String id) throws Exception {
		Connection connection= null;
		HashMap<String, Object> result = new HashMap<>();
		String sql = String.format("SELECT id,name,memo,meaning,picturePath FROM deck WHERE id='%s'", id);
		
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				result.put("id", rs.getString("id"));
				result.put("name", rs.getString("name"));
				result.put("memo", rs.getString("memo"));
				result.put("meaning", rs.getString("meaning"));
				result.put("picturePath", "picturePath");
			}
			rs.close();
			st.close();
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if (connection!=null){
				connection.close();
			}
		}

		return result;
	}

	@Override
	public boolean isExists(String deckId) throws Exception {
		Connection connection=null;
		String sql = String.format("select count(*) as count from deck where id = '%s'", deckId);
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
			throw new Exception(e.getMessage());
		} finally {
			if (connection!=null){
				connection.close();
			}
		}
		return false;
	}

}

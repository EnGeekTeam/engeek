package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.Deck;

@Service
public class DeckDAOImpl implements DeckDAO{
	@Autowired
	private Connection connection;

	@Override
	public List<Deck> getListByIdPac(String packageId) throws SQLException {
		List<Deck> result = new ArrayList<>();
		String sql = String.format("select * from deck where package_id = '%s'", packageId);
		
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		Deck deck;
		while (rs.next()){
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
		
		statement.close();
		return result;
	}
	
	@Override
	public HashMap<String, Object> getInfoById(String id) throws SQLException{
		HashMap<String, Object> result = new HashMap<>();
		
		String sql = String.format("SELECT id,name,memo,meaning,picturePath FROM `deck` WHERE id='%s'", id);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()){
			result.put("id", rs.getString("id"));
			result.put("name", rs.getString("name"));
			result.put("memo", rs.getString("memo"));
			result.put("meaning", rs.getString("meaning"));
			result.put("picturePath", "picturePath");
		}
		
		return result;
	}
	
	

}

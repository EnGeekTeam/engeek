package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgressDAOImpl implements ProgressDAO{
	
	@Autowired
	private DataSource datasource;
	
	
	@Override
	public Map<String, Object> allTimeData(int userID, List<Hashtable<String,Object>> badges) throws Exception {
		Connection connection = null;
		Map<String, Object> alldata = new TreeMap<>();
		String sql1 = String.format("select user.total_time, user.total_score from user where user.id=%d", userID);
		try{
			connection = datasource.getConnection();
			Statement st1 = connection.createStatement();
			ResultSet set1 = st1.executeQuery(sql1);
			while(set1.next()){
				alldata.put("Timespent", set1.getString("total_time"));
				alldata.put("Gikiscore", set1.getString("total_score"));
			}
			set1.close();
			st1.close();
			String sql2 = String.format("select count(userword.user_id) as count from userword group by userword.user_id having userword.user_id=%d", userID);
			Statement st2 = connection.createStatement();
			ResultSet set2 = st2.executeQuery(sql2);
			while(set2.next()){
				alldata.put("Newword", set2.getString("count"));
			}
			set2.close();
			st2.close();
			alldata.put("listBadge", badges.size()==0?"[]":badges);
		}catch (Exception e){
			throw new Exception(e.getMessage());
		} finally{
			if(connection!=null)
				connection.close();
		}
		
		return alldata;
	}


	@Override
	public List<Hashtable<String, Object>> allBadges(int userID) throws Exception {
		Connection connection = null;
		List<Hashtable<String, Object>> listbadge = new ArrayList<>();
		String sql = String.format("select badge.type, badge.picture from badge, userbadge where userbadge.badge_id = badge.id and userbadge.user_id=%d", userID);
		try{
			connection = datasource.getConnection();
			Statement st = connection.createStatement();
			ResultSet set = st.executeQuery(sql);
			while (set.next()){
				Hashtable<String, Object> abadge = new Hashtable<>();
				abadge.put("badgeName", set.getString("type"));
				abadge.put("picture", set.getString("picture"));
				listbadge.add(abadge);
			}
			set.close();
			st.close();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(connection !=null)
				connection.close();
		}
		
		return listbadge;
	}
	

	

}

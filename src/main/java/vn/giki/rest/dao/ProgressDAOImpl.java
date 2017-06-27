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

import vn.giki.rest.entity.Badge;
import vn.giki.rest.entity.BadgeInfo;
import vn.giki.rest.entity.ProgressInfo;

@Service
public class ProgressDAOImpl implements ProgressDAO{
	
	@Autowired
	private DataSource datasource;
	
	
	/*@Override
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
	}*/


	@Override
	public List<Badge> allBadges(int userID) throws Exception {
		Connection connection = null;
		List<Badge> listbadge = new ArrayList<>();
		String sql = String.format("select badge.type, badge.name, badge.picture from badge, userbadge where userbadge.badge_id = badge.id and userbadge.user_id=%d", userID);
		try{
			connection = datasource.getConnection();
			Statement st = connection.createStatement();
			ResultSet set = st.executeQuery(sql);
			System.out.println(sql);
			while (set.next()){
				String type = set.getString("type");
				String name = set.getString("name");
				String picture = set.getString("picture");
				listbadge.add(new Badge(type, name, picture));
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


	@Override
	public ProgressInfo allTimeData(int userID) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		String sql1 = String.format("select user.total_time, user.total_score from user where user.id=%d", userID);
		ProgressInfo info = new ProgressInfo();
		try{
			connection = datasource.getConnection();
			Statement st1 = connection.createStatement();
			ResultSet set1 = st1.executeQuery(sql1);
			int total_score = 0;
			while(set1.next()){
				info.setTotalTime((long)set1.getLong("total_time"));
				total_score = set1.getInt("total_score");
				info.setTotalScore(total_score);
				
			}
			set1.close();
			st1.close();
			String sql2 = String.format("select count(userword.user_id) as count from userword group by userword.user_id having userword.user_id=%d", userID);
			Statement st2 = connection.createStatement();
			ResultSet set2 = st2.executeQuery(sql2);
			int total_word = 0;
			while(set2.next()){
				total_word = set2.getInt("count");
				info.setNewWord(total_word);
			}
			set2.close();
			st2.close();
			if(total_word>=50 && total_score >= 50000 )
				info.setNameLevel("Enthusiast");
			else if(total_word>=100 && total_score >= 100000 )
				info.setNameLevel("Talented");
			else if(total_word>=250 && total_score >= 250000 )
				info.setNameLevel("Eloquent");
			else if(total_word>=500 && total_score >= 500000 )
				info.setNameLevel("Silver-tongued");
			else if(total_word>=1000 && total_score >= 1000000 )
				info.setNameLevel("Persuasive");
			else if(total_word>=1500 && total_score >= 1500000 )
				info.setNameLevel("Powerful");
			else if(total_word>=2000 && total_score >= 2000000 )
				info.setNameLevel("Dinosaur");
			else if(total_word>=3000 && total_score >= 3000000 )
				info.setNameLevel("Unicorn");
			else
				info.setNameLevel("Newbie");
		}catch (Exception e){
			throw new Exception(e.getMessage());
		} finally{
			if(connection!=null)
				connection.close();
		}
		return info;
	}


	@Override
	public BadgeInfo allTimeBadge(int userID) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		String sql1 = String.format("select user.total_time, user.current_day_streak, user.invitedFriends from user where user.id = %d", userID);
		String sql2 = String.format("select count(userword.user_id) as count from userword group by userword.user_id having userword.user_id=%d", userID);
		BadgeInfo info = new BadgeInfo();
		try {
			connection = datasource.getConnection();
			Statement st = connection.createStatement();
			ResultSet set = st.executeQuery(sql1);
			
			if (set.next()){
				info.setCurrentStreak(set.getInt("current_day_streak"));
				info.setInvitedFriend(set.getInt("invitedFriends"));
				info.setTotalTime(set.getLong("total_time"));
			}
			set = st.executeQuery(sql2);
			if(set.next()){
				info.setLearnedWord(set.getInt("count"));
			}
			set.close();
			st.close();
		}catch (Exception e){
			throw new Exception(e.getMessage());
		} finally{
			if(connection!=null)
				connection.close();
		}
		return info;		
	}


	@Override
	public List<Badge> bestBadge(int userID) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<Badge> maxBadge = new ArrayList<>();
		String sql = String.format("select max(badge.get_level) as max, badge.type from badge, userbadge where (badge.id = userbadge.badge_id and userbadge.user_id=%d) group by badge.type having (badge.type = 'word' or badge.type = 'hour' or badge.type = 'day' or badge.type = 'friend')", userID);
		try{
			connection = datasource.getConnection();
			Statement st = connection.createStatement();
			ResultSet set = st.executeQuery(sql);
			while(set.next()){
				String type = set.getString("type");
				int max = set.getInt("max");
				maxBadge.add(new Badge(type, max));
			}
			set.close();
			st.close();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(connection !=null)
				connection.close();
		}
		
		return maxBadge;
	}


	@Override
	public void saveBadge(int userId, int get_level, String type) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		String sql1 = String.format("select badge.id from badge where badge.get_level = %d and badge.type = '%s'", get_level, type);
		try{
			connection = datasource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql1);
			int badgeID = 0;
			if(rs.next()){
				badgeID = rs.getInt("id");
				String sql2 = String.format("insert into userbadge set user_id = %d, badge_id = %d, createdAt = NOW()", userId, badgeID);
				st.executeQuery(sql2);
				
			}
			rs.close();
			st.close();
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}finally{
			if(connection !=null)
				connection.close();
		}
		
	}
	

	

}

package vn.giki.rest.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import vn.giki.rest.dao.ProgressDAO;
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.entity.Badge;
import vn.giki.rest.entity.BadgeInfo;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users/{userId}/progress")
@Api(tags = {"User APIs"})
public class Progress {
	
	@Autowired
	private ProgressDAO progressDao;
	
	@Autowired 
	private UserDAO userDao;
	@Autowired
	private DataSource dataSource;
	@PostMapping("/savetime")
	public Map<String, Object> saveTimeSpend(@PathVariable Integer userId, @RequestHeader String hash, @RequestParam(required=true) String sessionTime) throws SQLException{
		Response res = new Response();
		Connection connection = null;
		try{
			connection = dataSource.getConnection();
			if (!userDao.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			String sql = String.format(SQLTemplate.UPDATE_TOTAL_TIME, Long.parseLong(sessionTime), userId);
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			return res.execute(sql, connection).renderResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}finally{
			if(connection!=null)
				connection.close();
		}
	}
	
	@GetMapping("/alltimepro")
	public Map<String, Object> allTimeData (@PathVariable Integer userId, @RequestHeader String hash){
		Response res = new Response();
		try{
			if (!userDao.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			List<Map<String, Object>> badges = new ArrayList<>();
			HashMap<String, Object> tmp;
			for(Badge b : progressDao.allBadges(userId)){
				tmp= new HashMap<>();
				tmp.put("type_badge", b.getType());
				tmp.put("name_badge", b.getName());
				tmp.put("pic_url", b.getPicURL());
				badges.add(tmp);
			}
			res.setResult(badges);
			HashMap<String, Object> dataprogress = new HashMap<>();
			dataprogress.put("total_time", progressDao.allTimeData(userId).getTotalTime());
			dataprogress.put("total_score", progressDao.allTimeData(userId).getTotalScore());
			dataprogress.put("new_word", progressDao.allTimeData(userId).getNewWord());
			dataprogress.put("name_level", progressDao.allTimeData(userId).getNameLevel());
			dataprogress.put("max_score", progressDao.allTimeData(userId).maxScore);
			return res.renderResponsePlus(dataprogress, "progressInfo");
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
	}
	
	@GetMapping("/vocab")
	public Map<String, Object> vocab (@PathVariable Integer userId, @RequestHeader String hash) throws SQLException{
		Response res = new Response();
		Connection connection = null;
		try{
			connection = dataSource.getConnection();
			if (!userDao.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			String sql = String.format(SQLTemplate.LIST_LEARNED_WORD, userId);
			return res.execute(sql, connection).renderArrayResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}finally{
			if(connection!=null)
				connection.close();
		}
	}
	
	@GetMapping("/maxBadgeData")
	public Map<String, Object> maxBadge (@PathVariable Integer userId, @RequestHeader String hash){
		Response res = new Response();
		try{
			if(!userDao.isExistsUser(userId))
				throw new ResourceNotFoundException();
			List<Map<String, Object>> badges = new ArrayList<>();
			HashMap<String, Object> tmp;
			for(Badge b : progressDao.bestBadge(userId)){
				tmp= new HashMap<>();
				tmp.put("type", b.getType());
				tmp.put("get_level", b.getGet_level());
				badges.add(tmp);
			}
			res.setResult(badges);
			HashMap<String, Object> badgeInfo = new HashMap<>();
			badgeInfo.put("current_day_streak", progressDao.allTimeBadge(userId).getCurrentStreak());
			badgeInfo.put("total_hour",(double) progressDao.allTimeBadge(userId).getTotalTime()/3600);
			badgeInfo.put("total_word", progressDao.allTimeBadge(userId).getLearnedWord());
			badgeInfo.put("invited_friends", progressDao.allTimeBadge(userId).getInvitedFriend());
			return res.renderResponsePlus(badgeInfo, "currentBadgeInfo");
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
	}
	@PostMapping("/saveBadge")
	public Map<String, Object> maxBadge (@PathVariable Integer userId, @ApiParam(value = "Type of badge: friend,day,word", required = true) @RequestParam String type, @RequestParam(required=true) Integer get_level, @RequestHeader String hash){
		Response res = new Response();
		try{
			if(!userDao.isExistsUser(userId))
				throw new ResourceNotFoundException();
			progressDao.saveBadge(userId, get_level, type);
			return res.renderResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
		
	}
}

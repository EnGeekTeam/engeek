package vn.giki.rest.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import vn.giki.rest.dao.ProgressDAO;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users/{userId}/progress")
@Api(tags = {"User APIs"})
public class Progress {
	private Connection connection;
	
	@Autowired
	public void setConnection (Connection conn){
		this.connection = conn;
	}
	
	@Autowired
	private ProgressDAO progressDao;
	
	@PostMapping("/savetime")
	public Map<String, Object> saveTimeSpend(@PathVariable Integer userId, @RequestHeader String hash, @RequestParam(required=true) Integer sessionTime){
		Response res = new Response();
		try{
			List<Map<String, Object>> temp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, userId), connection)
					.getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			String sql = String.format(SQLTemplate.UPDATE_TOTAL_TIME, sessionTime, userId);
			return res.execute(sql, connection).renderResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
	}
	
	@GetMapping("/alltimepro")
	public Map<String, Object> allTimeData (@PathVariable Integer userId, @RequestHeader String hash) throws SQLException{
		Response res = new Response();
		Map<String, Object> data = new TreeMap<>();
		data = progressDao.allTimeData(userId, progressDao.allBadges(userId));
		try{
			List<Map<String, Object>> temp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, userId), connection)
					.getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			return data;
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
	}
	
	@GetMapping("/vocab")
	public Map<String, Object> vocab (@PathVariable Integer userId, @RequestHeader String hash){
		Response res = new Response();
		try{
			List<Map<String, Object>> temp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, userId), connection)
					.getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			String sql = String.format(SQLTemplate.LIST_LEARNED_WORD, userId);
			return res.execute(sql, connection).renderArrayResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
	}

}
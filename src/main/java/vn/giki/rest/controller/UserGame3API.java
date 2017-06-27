package vn.giki.rest.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import vn.giki.rest.dao.GameDAO;
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users/{userID}/game3")
@Api(tags = {"User APIs"})
public class UserGame3API {
	
	@Autowired
	private DataSource dataSource;
	@Autowired
	private GameDAO gameDao;
	@Autowired
	private UserDAO userDao;
	
	@PostMapping("/save")
	public Map<String, Object> addGame3 (@PathVariable Integer userID, @RequestParam(required = true)String wordId, @RequestParam (required=true)Boolean isCorrected, @RequestParam (required = true) Integer score, @RequestHeader String hash) throws SQLException{
		Response res = new Response();
		Connection connection = null;
		try{
			connection = dataSource.getConnection();
			if (!userDao.isExistsUser(userID)) {
				throw new ResourceNotFoundException();
			}
			String query = "user_id="+userID+", word_id='"+wordId+"', isCorrected="+ isCorrected+", score="+score+", timeReview = NOW()";
			String sql = String.format(SQLTemplate.INSERT_GAME3, query);
			return res.execute(sql, connection).renderResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}finally{
			if(connection!=null)
				connection.close();
		}
	}
	@GetMapping("/data")
	public Map<String, Object> dataGame3 (@PathVariable Integer userID, @RequestHeader String hash) throws SQLException{
		Response res = new Response();
		Connection connection = null;
		try{
			connection = dataSource.getConnection();
			if (!userDao.isExistsUser(userID)) {
				throw new ResourceNotFoundException();
			}
			String sql = String.format(SQLTemplate.GET_DATA_GAME3, userID);
			return res.execute(sql, connection).renderArrayResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}finally{
			if(connection!=null)
				connection.close();
		}
	}
	
}

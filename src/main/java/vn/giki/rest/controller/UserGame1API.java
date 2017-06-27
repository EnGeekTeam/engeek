package vn.giki.rest.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vn.giki.rest.dao.GameDAO;
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.entity.Game1;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.GameException;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("users/{userId}/game1")
@Api(tags = { "User APIs" })
public class UserGame1API {
		
	@Autowired
	private GameDAO gameDao;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private UserDAO userDao;

	@PostMapping("/save")
	public Map<String, Object> addGame1(@PathVariable Integer userId, @RequestHeader String hash, @RequestParam(required = true) Integer score,
			@RequestParam(required = true) long timePlay, @RequestParam(required = true) String word1,
			@RequestParam(required = true) String word2, @RequestParam(required = true) String word3,
			@RequestParam(required = true) String word4) throws SQLException {
		Response res = new Response();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (!userDao.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			if (!gameDao.isReadyForGame1(userId))
				throw new GameException();

			String query = "user_id = " + userId + ", score = " + score + ", timePlay = " + timePlay + ", word1_id = '"
					+ word1 + "', word2_id = '" + word2 + "', word3_id = '" + word3 + "', word4_id='" + word4 + "', createdAt = NOW()";
			String sql = String.format(SQLTemplate.INSERT_GAME1, query);
			return res.execute(sql, connection).renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderArrayResponse();
		} finally{
			if(connection!=null)
				connection.close();
		}
	}

	@GetMapping("/words")
	public Map<String, Object> wordGame1(@PathVariable Integer userId, @RequestHeader String hash) throws SQLException {
		Response res = new Response();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (!userDao.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			if (!gameDao.isReadyForGame1(userId))
				throw new GameException();
			
			List<Map<String, Object>> gameList = new ArrayList<>();
			HashMap<String, Object> tmp;
			for(Game1 game : gameDao.game1data(userId)){
				tmp = new HashMap<>();
				tmp.put("number_of _false", game.getNumberFalse());
				tmp.put("number_of_review", game.getNumberReview());
				tmp.put("antonym", game.getAntonym());
				tmp.put("word", game.getWord());
				tmp.put("lastReview", game.getLastReview());
				gameList.add(tmp);
			}
			res.setResult(gameList);
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(String.format("Select min(usergame1.timePlay) as min from usergame1 where usergame1.user_id = %d", userId));
			HashMap<String, Object> bestPerform = new HashMap<>();
			if(rs.next()){
				bestPerform.put("best_performance", rs.getLong("min"));
			}
			rs.close();
			st.close();
			return res.renderResponsePlus(bestPerform, "performance");

		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}finally{
			if(connection!=null)
				connection.close();
		}
	}
	
}

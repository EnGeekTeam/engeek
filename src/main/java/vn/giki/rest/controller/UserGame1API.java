package vn.giki.rest.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.GameException;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("users/{userId}/game1")
@Api(tags = { "User APIs" })
public class UserGame1API {
	private Connection connection;
	
	@Autowired
	private GameDAO gameDao;

	@Autowired
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	@PostMapping("/save")
	public Map<String, Object> addGame1(@PathVariable Integer userId, @RequestHeader String hash, @RequestParam(required = true) Integer score,
			@RequestParam(required = true) Integer timeRemain, @RequestParam(required = true) String word1,
			@RequestParam(required = true) String word2, @RequestParam(required = true) String word3,
			@RequestParam(required = true) String word4) {
		Response res = new Response();
		try {
			List<Map<String, Object>> temp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, userId), connection)
					.getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			if (!gameDao.isReadyForGame1(userId))
				throw new GameException();

			String query = "user_id = " + userId + ", score = " + score + ", timeRemain = " + timeRemain + ", word1_id = '"
					+ word1 + "', word2_id = '" + word2 + "', word3_id = '" + word3 + "', word4_id='" + word4 + "'";
			String sql = String.format(SQLTemplate.INSERT_GAME1, query);
			return res.execute(sql, connection).renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderArrayResponse();
		}
	}

	@GetMapping("/words")
	public Map<String, Object> wordGame1(@PathVariable Integer userId, @RequestHeader String hash) {
		Response res = new Response();
		System.out.println(userId);
		try {
			List<Map<String, Object>> tp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, userId), connection)
					.getResult();
			if (tp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			if (!gameDao.isReadyForGame1(userId))
				throw new GameException();
			
			String sql = String.format(SQLTemplate.LIST_WORD_GAME1, userId);
			
			return res.execute(sql, connection).renderArrayResponse();

		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
	}
	
}

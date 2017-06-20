package vn.giki.rest.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
@RequestMapping("users/{UID}/game2")
@Api(tags = {"User APIs"})
public class UserGame2API {
	private Connection connection;
	
	@Autowired
	public void setConnection(Connection conn) {
		this.connection = conn;
	}
	
	@Autowired
	private GameDAO gameDAO;
	
	@PostMapping("/save")
	public Map<String, Object> addGame2 (@PathVariable Integer UID, @RequestHeader String hash, @RequestParam(required = true) String wordID, @RequestParam(required=true) Boolean isCorrected, @RequestParam(required = true)Integer score, @RequestParam(required=true)Integer timeRemain, @RequestParam(required = true) Date timeReview){
		Response res = new Response();
		try{
			List<Map<String, Object>> temp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, UID), connection)
					.getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			if (!gameDAO.isReadyForGame2(UID))
				throw new GameException();
			String query = "user_id = " + UID +", word_id='"+wordID+"', isCorrected="+isCorrected+", score="+score+", timeRemain="+timeRemain+", timeReview='"+timeReview+"'";
			String sql = String.format(SQLTemplate.INSERT_GAME2, query);
			return res.execute(sql, connection).renderResponse();
		}catch(Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
	}
	
	@GetMapping("/data")
	public Map<String, Object> wordsGame2 (@PathVariable Integer UID, @RequestHeader String hash){
		Map<String, Object> rs = new TreeMap<>();
		try {
			rs = gameDAO.game2data(gameDAO.game2list(UID));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
}

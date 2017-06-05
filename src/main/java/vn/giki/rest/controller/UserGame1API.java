package vn.giki.rest.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/game1/{userID}")
public class UserGame1API {
	private Connection connection;
	
	@Autowired
	public void setConnection (Connection conn){
		this.connection = conn;
	}
	
	@PutMapping()
	public Map<String, Object> addGame1 (@PathVariable Integer UID, @RequestParam (required = true) Integer score, @RequestParam (required = true) Integer timeRemain,
			@RequestParam (required =true) String word1,@RequestParam (required = false) String word2,
			@RequestParam (required =true) String word3,@RequestParam (required =true) String word4){
		Response res = new Response();
		try{
			List<Map<String, Object>> temp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, UID), connection)
					.getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			
			String query = "user_id = "+UID+", score = "+score+", timeRemain = "+timeRemain+ ", word1_id = '"+word1+"', word2_id = '"+word2+"', word3_id = '"+word3+"', word4_id='"+word4+"'";
			String sql = String.format(SQLTemplate.INSERT_GAME1, query);
			return res.execute(sql, connection).renderResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
	}
	
	/*@GetMapping("/words")
	public Map<String, Object> wordGame1 (@PathVariable Integer UID ){
		
	}*/
}

package vn.giki.rest.controller;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users/{userID}/game3")
@Api(tags = {"User APIs"})
public class UserGame3API {
	
	private Connection connection;
	@Autowired
	public void setConnection (Connection con){
		this.connection = con;
	}
	
	@PostMapping("/save")
	public Map<String, Object> addGame3 (@PathVariable Integer userID, @RequestParam(required = true)String wordId, @RequestParam (required=true)Boolean isCorrected, @RequestParam (required = true) Integer score, @RequestParam (required = true)Date timeReview){
		Response res = new Response();
		try{
			List<Map<String, Object>> temp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, userID), connection)
					.getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			String query = "user_id="+userID+", word_id='"+wordId+"', isCorrected="+ isCorrected+", score="+score+", timeReview='"+timeReview+"'";
			String sql = String.format(SQLTemplate.INSERT_GAME3, query);
			return res.execute(sql, connection).renderResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}
	}
	/*@GetMapping("/data")
	public Map<String, Object> dataGame3 (@PathVariable Integer userID){
		
	}*/
	
}

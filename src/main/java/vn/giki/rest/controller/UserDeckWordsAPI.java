package vn.giki.rest.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.giki.rest.dao.DeckDAO;
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users/{userId}/decks/words")
@Api(tags = { "User APIs" })
public class UserDeckWordsAPI {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private DeckDAO deckDAO;

	@Autowired
	private UserDAO userDAO;

	@ApiOperation(value = "Find all words of specified user and specified deck by ID", notes = "Returns a list of words of specified user and specified deck. Deck associated with deck's ID  OR user association with user's ID not exist, deck's ID OR user's ID is invalid will return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "deckId", value = "Deck's ID", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PostMapping
	public Map<String, Object> getUserPackDecks(@PathVariable Integer userId, @RequestParam String deckId,
			@RequestHeader String hash) throws SQLException, UnsupportedEncodingException {
		Response res = new Response();
		String deckIds = URLDecoder.decode(deckId, "UTF-8");
		System.out.println(deckIds);
		Connection connection=null;
		try {
			connection=dataSource.getConnection();
			if (!userDAO.isExistsUser(userId) || !deckDAO.isExists(deckIds)) {
				throw new ResourceNotFoundException();
			}

			String sql = String.format(SQLTemplate.GET_USER_DECK_WORDS, userId, deckIds);
			return res.execute(sql, connection).renderResponsePlus(deckDAO.getInfoById(deckIds), "root");
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}finally {
			if (connection!=null)connection.close();
		}
	}
}

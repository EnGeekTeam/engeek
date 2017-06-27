package vn.giki.rest.controller;

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
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.dao.UserWordDAO;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users/{userId}/words")
@Api(tags = { "User APIs" })
public class UserWordAPI {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserWordDAO userWordDAO;

	@Autowired
	private DataSource dataSource;

	@ApiOperation(value = "Find all words of specified user", notes = "Find and returns a list of word of specified user. User associated with ID not exist or user's ID is invalid will return API error and error message. Page < 0 or size < 1 will also return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "page", value = "Page (start at 0, default value is 0)", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "Number of items in page (start at 1, default value is 10)", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@GetMapping
	public Map<String, Object> getUserPackages(@PathVariable Integer userId,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size,
			@RequestHeader String hash) throws SQLException {
		Response res = new Response();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}

			int start = page * size, end = page * size + size;
			String sql = String.format(SQLTemplate.GET_USER_WORDS, userId, start, end);
			return res.execute(sql, connection).renderArrayResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderArrayResponse();
		} finally {
			if (connection != null)
				connection.close();

		}
	}

	@ApiOperation(value = "Add word of user", notes = "Find and returns a list of word of specified user. User associated with ID not exist or user's ID is invalid will return API error and error message. Page < 0 or size < 1 will also return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "wordId", value = "Word ID", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "isWrong", value = "Wrong answer. (1: true, 0: false)", required = true, dataType = "int", paramType = "query"),
		//	@ApiImplicitParam(name = "isLearn", value = "Call api save in learn or game process. (1: true, 0: false)", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PostMapping("/{wordId}/update")
	public Map<String, Object> addUserWord(@PathVariable Integer userId, @PathVariable String wordId,
			@RequestParam Integer isWrong, @RequestHeader String hash) {

		Response res = new Response();
		try {
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			if (userWordDAO.isExists(userId, wordId)) {
				boolean value = (isWrong == 1) ? true : false;
				userWordDAO.update(userId, wordId, value);
			} else {
				userWordDAO.insertUserWord(userId, wordId);
			}

			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderArrayResponse();
		}

	}
}

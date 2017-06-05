package vn.giki.rest.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users/{userId}/packages/{packageId}/decks")
@Api(tags = { "User APIs" })
public class UserPackDecksAPI {
	private Connection connection;

	@Autowired
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@ApiOperation(value = "Find all decks of specified user and specified package by ID", notes = "Returns a list of decks of specified user and specified package. User associated with User's ID OR Package associated with Package's ID not exist, package's ID OR user's ID is invalid will return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "packageId", value = "Package's ID", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header")})
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@GetMapping
	public Map<String, Object> getUserPackDecks(@PathVariable Integer userId, @PathVariable String packageId, @RequestHeader String hash) {
		Response res = new Response();
		try {
			List<Map<String, Object>> temp = res.execute(String.format(SQLTemplate.IS_USER_EXIST, userId), connection)
					.getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			temp = res.execute(String.format(SQLTemplate.IS_PACKAGE_EXIST, packageId), connection).getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			String sql = String.format(SQLTemplate.GET_USER_PACKAGE_DECKS, userId, packageId);
			return res.execute(sql, connection).renderArrayResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderArrayResponse();
		}
	}
}

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.dao.UserPackageDAO;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users/{userId}/packages")
@Api(tags = { "User APIs" })
public class UserPackAPI {

	@Autowired
	private UserPackageDAO userPackDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private DataSource dataSource;


	@ApiOperation(value = "Find all packages of specified user", notes = "Returns a list of package of specified user.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@GetMapping
	public Map<String, Object> getUserPackages(@PathVariable Integer userId, @RequestHeader String hash)
			throws SQLException {
		Response res = new Response();
		Connection connection =null; 
		
		try {
			connection=dataSource.getConnection();
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			String sql = String.format(SQLTemplate.GET_USER_PACKAGES, userId);
			return res.execute(sql, connection).renderArrayResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderArrayResponse();
		}finally {
			if (connection!=null)connection.close();
			
		}
	}

	@ApiOperation(value = "Unlock package", notes = "Unlock package of user", responseContainer = "Message")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "packageId", value = "Package ID", required = true, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PostMapping("/{packageId}/unlock")
	public Map<String, Object> unlockUserPackages(@PathVariable Integer userId, @PathVariable String packageId,
			@RequestHeader String hash) {
		Response res = new Response();
		try {
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}

			userPackDAO.save(packageId, userId);

			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderArrayResponse();
		}
	}

}

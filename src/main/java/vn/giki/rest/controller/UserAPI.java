package vn.giki.rest.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.giki.rest.utils.FacebookSignIn;
import vn.giki.rest.utils.GoogleSignIn;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.Utils;
import vn.giki.rest.utils.exception.CanNotUpdateWithEmptyParameterException;
import vn.giki.rest.utils.exception.ResourceNotFoundException;
import vn.giki.rest.utils.exception.TokenInvalidException;

@RestController
@RequestMapping("/users")
@Api(value = "/users", description = "Operations about users", basePath = "/users", tags = { "User APIs" })
public class UserAPI {
	private Connection connection;

	@Autowired
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@ApiOperation(value = "Find user's payment info of specified user", notes = "Returns an user's payment information who associated with user's ID. User associated with ID not exist, user's ID is invalid will return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header")
			})
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@GetMapping("/{userId}/payment")
	public Map<String, Object> getUserPayment(@PathVariable Integer userId, @RequestHeader String hash) {
		Response res = new Response();
		try {
			List<Map<String, Object>> temp = res
					.execute(String.format(SQLTemplate.GET_USER_PAYMENT, userId), connection).getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
		
	}

	@ApiOperation(value = "Find the high scores descending", notes = "Returns a list of user high scores. Result will include maxium score of each user's game and total score of all game. Page < 0 or size < 1 will return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "Page (start at 0, default value is 0)", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "Number of items in page (start at 1, default value is 10)", required = false, dataType = "string", paramType = "query") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@GetMapping("/high-scores")
	public Map<String, Object> getUserHighScore(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size) {
		Response res = new Response();
		try {
			int start = page * size, end = page * size + size;
			String sql = String.format(SQLTemplate.GET_USERS_HIGH_SCORES, start, end);
			return res.execute(sql, connection).renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
	}

	@ApiOperation(value = "Update user by ID", notes = "Update and returns an updated user's ID if success. User's ID not exist or invalid parameters will return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "email", value = "User's email", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "avatarUrl", value = "User's avatar url", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "expiredDate", value = "User's expired date", required = false, dataType = "date", paramType = "query"),
			@ApiImplicitParam(name = "gender", value = "User's gender", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "hint", value = "User's hint", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "invitedFriends", value = "User's invited friends", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "User's name", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "paymentStatus", value = "User's payment status", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "paymentTime", value = "User's payment time", required = false, dataType = "date", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "User's type", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header")})
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PutMapping("/{userId}")
	public Map<String, Object> updateUser(@PathVariable Integer userId, @RequestParam(required = false) String email,
			@RequestParam(required = false) String avatarUrl, @RequestParam(required = false) String expiredDate,
			@RequestParam(required = false) String gender, @RequestParam(required = false) Integer hint,
			@RequestParam(required = false) Integer invitedFriends, @RequestParam(required = false) String name,
			@RequestParam(required = false) Integer paymentStatus, @RequestParam(required = false) String paymentTime,
			@RequestParam(required = false) Integer type, 
			@RequestHeader(required = true) String hash) {
		Response res = new Response();
		try {
			List<Map<String, Object>> temp = res
					.execute(String.format(SQLTemplate.IS_USER_EXIST, userId), connection).getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
		
			StringBuilder params = new StringBuilder();
			if (email != null) {
				params.append("email='");
				params.append(email);
				params.append("',");
			}
			if (avatarUrl != null) {
				params.append("avatarUrl='");
				params.append(avatarUrl);
				params.append("',");
			}
			if (expiredDate != null) {
				params.append("expiredDate='");
				params.append(expiredDate);
				params.append("',");
			}
			if (gender != null) {
				params.append("gender='");
				params.append(gender);
				params.append("',");
			}
			if (name != null) {
				params.append("name='");
				params.append(name);
				params.append("',");
			}
			if (paymentTime != null) {
				params.append("paymentTime='");
				params.append(paymentTime);
				params.append("',");
			}
			if (hint != null) {
				params.append("hint=");
				params.append(hint);
				params.append(",");
			}
			if (invitedFriends != null) {
				params.append("invitedFriends=");
				params.append(invitedFriends);
				params.append(",");
			}
			if (paymentStatus != null) {
				params.append("paymentStatus=");
				params.append(paymentStatus);
				params.append(",");
			}
			if (type != null) {
				params.append("type=");
				params.append(type);
				params.append(",");
			}
			if (params.length() == 0) {
				throw new CanNotUpdateWithEmptyParameterException();
			} else {
				params.deleteCharAt(params.lastIndexOf(","));
			}
			String sql = String.format(SQLTemplate.UPDATE_USER, params, userId);
			return res.execute(sql, connection).renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
	}

	@ApiOperation(value = "Log in", notes = "Used to login to the system. If user is available, then proceed to login. If the user does not exits, then proceed to register the user to the system. The result returned includes user's information", responseContainer = "List")
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PostMapping("/info")
	public @ResponseBody Map<String, Object> login(
			@ApiParam(value = "Google Id", required = false) @RequestParam(name = "googleId", defaultValue = "") String googleId,
			@ApiParam(value = "Facebook Id", required = false) @RequestParam(name = "facebookId", defaultValue = "") String facebookId,
			@ApiParam(value = "Token Google or Facebook", required = true) @RequestParam(name = "token", defaultValue = "") String token)
			throws Exception {
		Response res = new Response();
		try {
			Map<String, Object> userTmp = null;
			List<Map<String, Object>> queryTmp;
			if (!googleId.equals("")) {
				System.out.println("TAG: google");
				queryTmp = res.execute(String.format("select * from user where googleId = %s", googleId), connection).getResult();
				Map<String, Object> uCheck = GoogleSignIn.checkToken(token, googleId);
				if (queryTmp.size() > 0) {
					userTmp = queryTmp.get(0);
					// login
					if (uCheck != null) {
						System.out.println("login");
						userTmp.remove("token");
						return res.renderResponse();
					} else {
						throw new TokenInvalidException();
					}

				} else {
					System.out.println(googleId);
					if (uCheck != null) {
						saveUser(uCheck);
						uCheck.remove("token");
						res.getResult().add(uCheck);
						return res.renderResponse();
					} else {
						throw new TokenInvalidException();
					}
				}
			} else {
				queryTmp = res.execute(String.format("select * from user where facebookId = %s", facebookId), connection)
						.getResult();
				Map<String, Object> uCheck = FacebookSignIn.checkToken(token, facebookId);
				if (queryTmp.size() > 0) {
					userTmp = queryTmp.get(0);
					// login
					if (uCheck != null) {
						System.out.println("login");
						userTmp.remove("token");
						return res.renderResponse();
					} else {
						throw new TokenInvalidException();
					}

				} else {
					System.out.println(googleId);
					if (uCheck != null) {
						saveUser(uCheck);
						uCheck.remove("token");
						res.getResult().add(uCheck);
						return res.renderResponse();
					} else {
						throw new TokenInvalidException();
					}
				}
			}
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
	}

	public void saveUser(Map<String, Object> u) throws SQLException, IllegalArgumentException, UnsupportedEncodingException {
		String sql = "insert into user(email,facebookId,googleId,token,tokenClient,created,name,gender,avatarUrl,hint) values (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement st = connection.prepareStatement(sql);
		st.setString(1, (String) u.get("email"));
		st.setString(2, (String) u.get("facebookId"));
		st.setString(3, (String) u.get("googleId"));
		st.setString(4, (String) u.get("token"));
		st.setString(5, (String) u.get("tokenClient"));
		st.setDate(6, new Date(new java.util.Date().getTime()));
		st.setString(7, (String) u.get("name"));
		st.setString(8, (String) u.get("gender"));
		st.setString(9, (String) u.get("avatarUrl"));
		st.setInt(10, (int) u.get("hint"));
		st.executeUpdate();
		st.close();
		
		
		String getId = String.format("select id from user where email='%s'", (String) u.get("email"));
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(getId);
		if (rs.next()){
			int id = rs.getInt("id");
			String sqlUpdateTokenClient = String.format("update user set tokenClient='%s' where id=%d", Utils.encodeJWT(String.valueOf(id)), id);
			Statement stt = connection.createStatement();
			stt.execute(sqlUpdateTokenClient);
		}
	}
}

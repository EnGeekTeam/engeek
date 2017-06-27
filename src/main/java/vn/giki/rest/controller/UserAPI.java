package vn.giki.rest.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import vn.giki.rest.dao.PackageDAO;
import vn.giki.rest.dao.PayDAO;
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.entity.Package;
import vn.giki.rest.entity.User;
import vn.giki.rest.utils.Constant;
import vn.giki.rest.utils.FacebookSignIn;
import vn.giki.rest.utils.GoogleSignIn;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.SQLTemplate;
import vn.giki.rest.utils.Utils;
import vn.giki.rest.utils.exception.CanNotUpdateWithEmptyParameterException;
import vn.giki.rest.utils.exception.ResourceNotFoundException;
import vn.giki.rest.utils.exception.TokenInvalidException;
import vn.giki.rest.utils.pourchase.PurchaseVerifierApple;
import vn.giki.rest.utils.pourchase.PurchaseVerifierGoogle;

@RestController
@RequestMapping("/users")
@Api(value = "/users", description = "Operations about users", basePath = "/users", tags = { "User APIs" })
public class UserAPI {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PackageDAO packageDAO;
	
	@Autowired
	private PayDAO payDAO;

	@ApiOperation(value = "Find user's payment info of specified user", notes = "Returns an user's payment information who associated with user's ID. User associated with ID not exist, user's ID is invalid will return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@GetMapping("/{userId}/payment")
	public Map<String, Object> getUserPayment(@PathVariable Integer userId, @RequestHeader String hash)
			throws SQLException {
		Response res = new Response();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			List<Map<String, Object>> temp = res
					.execute(String.format(SQLTemplate.GET_USER_PAYMENT, userId), connection).getResult();
			if (temp.size() == 0) {
				throw new ResourceNotFoundException();
			}
			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		} finally {
			if (connection != null)
				connection.close();
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
			@ApiImplicitParam(name = "nps", value = "User's nps", required = false, dataType = "int (1=10)", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "User's status", required = false, dataType = "int (0/1)", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PutMapping("/{userId}/update")
	public Map<String, Object> updateUser(@PathVariable Integer userId, @RequestParam(required = false) String email,
			@RequestParam(required = false) String avatarUrl, @RequestParam(required = false) String expiredDate,
			@RequestParam(required = false) String gender, @RequestParam(required = false) Integer hint,
			@RequestParam(required = false) Integer invitedFriends, @RequestParam(required = false) String name,
			@RequestParam(required = false) Integer paymentStatus, @RequestParam(required = false) String paymentTime,
			@RequestParam(required = false) Integer type, @RequestParam(required = false) Integer nps, @RequestParam(required = false) Integer status, @RequestHeader(required = true) String hash) {
		Response res = new Response();
		try {
			if (!userDAO.isExistsUser(userId)) {
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
			if (nps != null) {
				params.append("nps=");
				params.append(nps);
				params.append(",");
			}
			if (status != null) {
				params.append("status=");
				params.append(status);
				params.append(",");
			}
			if (params.length() == 0) {
				throw new CanNotUpdateWithEmptyParameterException();
			} else {
				params.deleteCharAt(params.lastIndexOf(","));
			}
			String sql = String.format(SQLTemplate.UPDATE_USER, params, userId);
			userDAO.updateUser(sql);
			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
	}

	@ApiOperation(value = "Update user purchase", notes = "Update and returns an updated user's ID if success. User's ID not exist or invalid parameters will return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "receiptData", value = "Purchase info", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PostMapping("/{userId}/purchases_ios")
	public Map<String, Object> updatePurchase(@PathVariable Integer userId,
			@RequestBody(required = true) String receiptData, @RequestHeader(required = true) String hash) {

		System.out.println(receiptData);
		JSONObject json = new JSONObject(receiptData);

		String dataReceipt = json.getString("receipt-data");

		System.out.println(receiptData);
		Response res = new Response();
		try {

			HashMap<String, Object> infoPurcha = PurchaseVerifierApple.getReceipt(dataReceipt);

			if (!infoPurcha.containsKey("product_id")) {
				userDAO.updatePurches(userId, 0, 0, Constant.USER.STATE_PAYMENT_UNPAID, "", true);
				throw new Exception("Payment expired!");
			} else if (((String)infoPurcha.get("product_id")).equals(Constant.PURCHASE_APPLE.PRODUCT_FOREVER)){
				userDAO.updatePurches(userId, Long.parseLong((String) infoPurcha.get("purchase_date_ms")),
						0,
						Constant.USER.STATE_PAYMENT_PAID, (String) infoPurcha.get("product_id"), true);
				return res.renderResponse();
			}

			if (infoPurcha.containsKey("cancellation_date")) {
				userDAO.updatePurches(0, Long.parseLong((String) infoPurcha.get("purchase_date_ms")), 0,
						Constant.USER.STATE_CLOSE, (String) infoPurcha.get("product_id"), true);
			} else {
				if (infoPurcha.containsKey("expires_date_ms")) {
					userDAO.updatePurches(userId, Long.parseLong((String) infoPurcha.get("purchase_date_ms")),
							Long.parseLong((String) infoPurcha.get("expires_date_ms")),
							Constant.USER.STATE_PAYMENT_PAID, (String) infoPurcha.get("product_id"), true);
				} else {
					userDAO.updatePurches(userId, Long.parseLong((String) infoPurcha.get("purchase_date_ms")), 0,
							Constant.USER.STATE_PAYMENT_PAID, (String) infoPurcha.get("product_id"), true);
				}
			}
			payDAO.save(userId, Constant.PLATFORM.IOS, "", (String)infoPurcha.get("product_id"), receiptData,Utils.getDate(Long.parseLong((String)infoPurcha.get("purchase_date_ms"))),Utils.getDate(Long.parseLong((String)infoPurcha.get("expires_date_ms"))));

			return res.renderResponse();

		} catch (Exception e) {
			e.printStackTrace();
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
		Connection connection = null;
		String createToken = Utils.createToken();
		try {
			connection = dataSource.getConnection();
			Map<String, Object> userTmp = null;
			List<Map<String, Object>> queryTmp;
			if (!googleId.equals("")) {
				System.out.println("TAG: google");
				queryTmp = res.execute(String.format("select * from user where googleId = '%s'", googleId), connection)
						.getResult();
				Map<String, Object> uCheck = GoogleSignIn.checkToken(token, googleId);
				if (queryTmp.size() > 0) {
					userTmp = queryTmp.get(0);
					// login
					if (uCheck != null) {
						System.out.println("login");
						userTmp.remove("token");
						userDAO.updateClientToken((int)userTmp.get("id"), createToken);
						userTmp.put("tokenClient", createToken);
						userDAO.checkUpHint((int)userTmp.get("id"));
						return res.renderResponse();
					} else {
						throw new TokenInvalidException();
					}

				} else {
					System.out.println(googleId);
					if (uCheck != null) {
						int userId = userDAO.insertUser(uCheck);
						uCheck.remove("token");
						uCheck.put("id", userId);
						res.getResult().add(uCheck);
						return res.renderResponse();
					} else {
						throw new TokenInvalidException();
					}
				}
			} else {
				queryTmp = res
						.execute(String.format("select * from user where facebookId = '%s'", facebookId), connection)
						.getResult();
				Map<String, Object> uCheck = FacebookSignIn.checkToken(token, facebookId);
				if (queryTmp.size() > 0) {
					userTmp = queryTmp.get(0);
					// login
					if (uCheck != null) {
						System.out.println("login");
						userTmp.remove("token");
						userDAO.updateClientToken((int)userTmp.get("id"), createToken);
						userTmp.put("tokenClient", createToken);
						userDAO.checkUpHint((int)userTmp.get("id"));
						return res.renderResponse();
					} else {
						throw new TokenInvalidException();
					}

				} else {
					System.out.println(googleId);
					if (uCheck != null) {
						int userId = userDAO.insertUser(uCheck);
						uCheck.remove("token");
						uCheck.put("id", userId);

						res.getResult().add(uCheck);
						return res.renderResponse();
					} else {
						throw new TokenInvalidException();
					}
				}
			}
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	@ApiOperation(value = "Update user purchase", notes = "Update and returns an updated user's ID if success. User's ID not exist or invalid parameters will return API error and error message.", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "packageName", value = "Package name", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "subcriptionId", value = "Product Id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "token", value = "Purchase Token", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PostMapping("/{userId}/purchases_android")
	public @ResponseBody Map<String, Object> purchaseAndroid(@PathVariable("userId") int userId,
			@RequestParam("packageName") String packageName, @RequestParam("subcriptionId") String subcriptionId,
			@RequestParam("token") String token, @RequestHeader("hash") String hash) throws Exception {

		Response res = new Response();
		try {
			String resData = PurchaseVerifierGoogle.getData(packageName, subcriptionId, token);

			System.out.println(resData);

			JSONObject jsonObj = new JSONObject(resData);
			long startTimeMillis = jsonObj.getLong("startTimeMillis");
			
			if (jsonObj.has("startTimeMillis") && subcriptionId.equals(Constant.PURCHASE_GOOGLE.PRODUCT_FOREVER)){
				userDAO.updatePurches(userId, startTimeMillis, 0, Constant.USER.STATE_PAYMENT_PAID,
						subcriptionId, true);
				return res.renderResponse();
			}

			long expiryTimeMillis = jsonObj.getLong("expiryTimeMillis");
			long timeTmp = System.currentTimeMillis();
			System.out.println(timeTmp);

			// TODO: increase gold,hint....
			if (expiryTimeMillis > timeTmp) {
				userDAO.updatePurches(userId, startTimeMillis, expiryTimeMillis, Constant.USER.STATE_PAYMENT_PAID,
						subcriptionId, true);
			} else {
				userDAO.updatePurches(userId, startTimeMillis, expiryTimeMillis, Constant.USER.STATE_PAYMENT_UNPAID,
						subcriptionId, true);
				throw new Exception("Payment expired!");
			}
			payDAO.save(userId, Constant.PLATFORM.ANDROID, packageName, subcriptionId, token,Utils.getDate(startTimeMillis),Utils.getDate(expiryTimeMillis));

			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}

	}

	@GetMapping("/{userId}/get_score")
	public @ResponseBody Map<String, Object> getScore(@PathVariable("userId") int userId,
			@RequestHeader("hash") String hash) {
		Response res = new Response();
		try {
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			System.out.println("----+++" + userId);

			User user = userDAO.getScore(userId);
			System.out.println(user.getName());

			List<Map<String, Object>> result = new ArrayList<>();
			HashMap<String, Object> tmp = new HashMap<>();
			tmp.put("id", user.getId());
			tmp.put("name", user.getName());
			tmp.put("hint", user.getHint());
			tmp.put("score_game_1", user.getScoreGame1());
			tmp.put("score_game_2", user.getScoreGame2());
			tmp.put("score_game_3", user.getScoreGame3());
			tmp.put("score_total", user.getScoreTotal());

			List<Package> scoreNext = packageDAO.getAll();
			System.out.println(scoreNext.size() + "++=");
			Package packageTmp = null;
			for (Package s : scoreNext) {
				System.out.println(s.getId());
				if (s.getOrders() > user.getScoreTotal()) {
					packageTmp = s;
					break;
				}
			}

			if (packageTmp == null && scoreNext.size() > 0) {
				packageTmp = scoreNext.get(1);
			}

			tmp.put("next_level", packageTmp);

			result.add(tmp);
			res.setResult(result);
			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
		
	}
	
	@PutMapping("/{userID}/getBonus/")
	public Map<String, Object> getBonus (@PathVariable (required = true) int userID, @RequestParam(required = true) String uniquecode, @RequestHeader (required = true) String hash){
		Response res = new Response();
		try{
			if(!userDAO.isExistsUser(userID))
				throw new ResourceNotFoundException();
			userDAO.updateUserExpiredDate(userID, uniquecode);
			return res.renderResponse();
		}catch (Exception e){
			return res.setThrowable(e).renderResponse();
		}
	}
	
}

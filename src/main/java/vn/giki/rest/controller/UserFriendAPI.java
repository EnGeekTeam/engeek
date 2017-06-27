package vn.giki.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.giki.rest.dao.FriendDAO;
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.entity.User;
import vn.giki.rest.utils.Response;
import vn.giki.rest.utils.Utils;
import vn.giki.rest.utils.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/users")
@Api(value = "/users", description = "Operations about users", basePath = "/users", tags = { "Friend APIs" })
public class UserFriendAPI {

	@Autowired
	private FriendDAO friendDAO;

	@Autowired
	private UserDAO userDAO;

	@ApiOperation(value = "Add list friend", notes = "Add list friend", responseContainer = "status")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "list", value = "List ID", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "platform", value = "Platform: 'google' or 'facebook'", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header")})
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PostMapping("/{userId}/add_friend")
	public Map<String, Object> addFriend(@PathVariable("userId") int userId, @RequestParam("list") String list,
			@RequestParam("platform") String platform, @RequestHeader("hash") String hash) {
		Response res = new Response();
		System.out.println("##");
		try {
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			// parse json
			JSONObject jsonObj = new JSONObject(list);
			JSONArray arrJson = jsonObj.getJSONArray("list");
			List<String> listData = new ArrayList<>();
			for (int i = 0; i < arrJson.length(); i++) {
				listData.add(arrJson.get(i).toString());
			}

			System.out.println(platform);

			List<Map<String, Object>> result = new ArrayList<>();
			HashMap<String, Object> tmp;
			List<User> listFriend= friendDAO.saveListByUserIdPlatform(userId, listData, platform);
			for (User u : listFriend) {
				tmp = new HashMap<>();
				tmp.put("id", u.getId());
				tmp.put("name", u.getName());
				tmp.put("googleId", u.getGoogleId());
				tmp.put("facebookId", u.getFacebookId());
				tmp.put("avatarUrl", u.getAvatarUrl());
				tmp.put("game1_max_score", u.getScoreGame1());
				tmp.put("game2_max_score", u.getScoreGame2());
				tmp.put("game3_max_score", u.getScoreGame3());
				tmp.put("total_score", u.getScoreTotal());
				result.add(tmp);
			}
			res.setResult(result);
			
			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
	}

	@ApiOperation(value = "Get list friends from list send", notes = "Get list friends from list send", responseContainer = "List")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "list", value = "List ID", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "Page (start at 1, default value is 1)", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "Number of items in page (start at 1, default value is 10)", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PostMapping("/{userId}/get_list_friends")
	public @ResponseBody Map<String, Object> getListFriends(@PathVariable("userId") int userId,
			@RequestParam("list") String list, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size, @RequestHeader("hash") String hash) {
		Response res = new Response();
		try {
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}

			JSONObject jsonObj = new JSONObject(list);
			JSONArray arrJson = jsonObj.getJSONArray("list");
			List<String> listData = new ArrayList<>();
			for (int i = 0; i < arrJson.length(); i++) {
				listData.add(arrJson.get(i).toString());
			}
			String value = Utils.getValue(listData);

			List<Map<String, Object>> result = new ArrayList<>();
			HashMap<String, Object> tmp;

			for (User u : userDAO.getListFriends(value, size, page)) {
				tmp = new HashMap<>();
				tmp.put("id", u.getId());
				tmp.put("name", u.getName());
				tmp.put("googleId", u.getGoogleId());
				tmp.put("facebookId", u.getFacebookId());
				tmp.put("avatarUrl", u.getAvatarUrl());
				tmp.put("game1_max_score", u.getScoreGame1());
				tmp.put("game2_max_score", u.getScoreGame2());
				tmp.put("game3_max_score", u.getScoreGame3());
				tmp.put("total_score", u.getScoreTotal());
				result.add(tmp);
			}
			res.setResult(result);

			HashMap<String, Object> tmpInfo = new HashMap<>();
			int count = userDAO.countPage(value);
			int total = (count % size != 0) ? (count / size + 1) : (count / size);
			tmpInfo.put("totalPage", total);
			tmpInfo.put("page", page);

			return res.renderResponsePlus(tmpInfo, "info");
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}

	}

	@ApiOperation(value = "Remove friend from list send", notes = "Remove friend from list send", responseContainer = "status")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "list", value = "List id giki", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@PutMapping("/{userId}/remove_friend")
	public Map<String, Object> removeFriend(@PathVariable("userId") int userId, @RequestParam("list") String list,
			@RequestHeader("hash") String hash) {
		Response res = new Response();
		try { 
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}
			// parse json
			JSONObject jsonObj = new JSONObject(list);
			JSONArray arrJson = jsonObj.getJSONArray("list");
			List<Integer> listData = new ArrayList<>();

			for (int i = 0; i < arrJson.length(); i++) {
				listData.add(Integer.parseInt(arrJson.get(i).toString()));
			}

			friendDAO.removeListUserIdPlatform(userId, listData);

			return res.renderResponse();
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
	}

	@ApiOperation(value = "Get list friend", notes = "Get list friend", responseContainer = "list friend")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "User's ID", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "page", value = "Page (start at 1, default value is 1)", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "Number of items in page (start at 1, default value is 10)", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "hash", value = "Hash key", required = true, dataType = "String", paramType = "header") })
	@ApiResponses({ @ApiResponse(code = 500, message = "Internal Error") })
	@GetMapping("/{userId}/leaderboard")
	public Map<String, Object> removeFriend(@PathVariable("userId") int userId,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size, @RequestHeader("hash") String hash) {
		Response res = new Response();
		try {
			if (!userDAO.isExistsUser(userId)) {
				throw new ResourceNotFoundException();
			}

			List<Map<String, Object>> result = new ArrayList<>();
			HashMap<String, Object> tmp;
			
			List<User> listFriend = friendDAO.getListFriend(userId, size, page);
			listFriend.add(userDAO.getScore(userId));

			for (User u : listFriend) {
				tmp = new HashMap<>();
				tmp.put("id", u.getId());
				tmp.put("name", u.getName());
				tmp.put("googleId", u.getGoogleId());
				tmp.put("facebookId", u.getFacebookId());
				tmp.put("avatarUrl", u.getAvatarUrl());
				tmp.put("game1_max_score", u.getScoreGame1());
				tmp.put("game2_max_score", u.getScoreGame2());
				tmp.put("game3_max_score", u.getScoreGame3());
				tmp.put("total_score", u.getScoreTotal());
				result.add(tmp);
			}
			res.setResult(result);

			HashMap<String, Object> tmpInfo = new HashMap<>();
			int count = friendDAO.getSizeListFriend(userId);
			int total = (count % size != 0) ? (count / size + 1) : (count / size);
			tmpInfo.put("totalPage", total);
			tmpInfo.put("page", page);

			return res.renderResponsePlus(tmpInfo, "info");
		} catch (Exception e) {
			return res.setThrowable(e).renderResponse();
		}
	}

}

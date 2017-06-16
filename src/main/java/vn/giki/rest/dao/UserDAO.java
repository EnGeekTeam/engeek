package vn.giki.rest.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import vn.giki.rest.entity.User;

public interface UserDAO {

	int insertUser(Map<String, Object> u) throws Exception;

	int countPage(String value) throws Exception;

	boolean isExistsUser(int userId) throws SQLException;

	List<User> getListFriends(String value, int size, int page) throws SQLException;

	void updatePurches(int userId, long paymentTime, long paymentExpire, int paymentStatus, String type)
			throws Exception;

	User getScore(int userId) throws Exception;

	String getPlatform(int userId) throws SQLException;

	int getIdUser(String userIdPlatform, String platform) throws SQLException;
}

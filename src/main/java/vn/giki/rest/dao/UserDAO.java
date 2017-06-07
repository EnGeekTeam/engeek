package vn.giki.rest.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import vn.giki.rest.entity.User;

public interface UserDAO {

	int insertUser(Map<String, Object> u) throws Exception;
	int countPage(int size) throws Exception;
	boolean isExistsUser(int userId) throws SQLException;
	List<User> getListFriends(List<String> listId, int fOrg) throws SQLException;
	void updatePurches(int userId, long paymentTime, long paymentExpire, int paymentStatus) throws SQLException;
}

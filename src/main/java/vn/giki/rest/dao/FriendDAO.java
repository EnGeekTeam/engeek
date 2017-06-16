package vn.giki.rest.dao;

import java.util.List;

import vn.giki.rest.entity.User;

public interface FriendDAO {

	void save(int userId, int userFriend) throws Exception;

	boolean isExists(int userId, int userFriendId) throws Exception;
	
	void remove(int userId, int userFriend) throws Exception;
	
	void saveListByUserIdPlatform(int userId, List<String> listIdPlatform, String platform) throws Exception;
	
	void removeListUserIdPlatform(int userId, List<String> listIdPlatform, String platform) throws Exception;
	
	int getSizeListFriend(int userId) throws Exception;
	
	List<User> getListFriend(int userId, int size, int page) throws Exception;

}

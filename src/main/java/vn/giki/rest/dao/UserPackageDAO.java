package vn.giki.rest.dao;

import java.util.List;

import vn.giki.rest.entity.UserPack;

public interface UserPackageDAO {

	void save(String packageId, int userId)  throws Exception;
	
	List<UserPack> getList(int userId, String packageId);
}

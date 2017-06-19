package vn.giki.rest.dao;

import java.sql.SQLException;

public interface UserPackageDAO {

	void save(String packageId, int userId)  throws Exception;
	
	boolean isExistsUserPack(String packageId, int userId) throws SQLException;
	
}

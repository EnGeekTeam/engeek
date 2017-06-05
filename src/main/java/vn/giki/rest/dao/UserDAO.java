package vn.giki.rest.dao;

import java.util.Map;

public interface UserDAO {

	int insertUser(Map<String, Object> u) throws Exception;
	int countPage(int size) throws Exception;
}

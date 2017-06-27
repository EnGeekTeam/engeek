package vn.giki.rest.dao;

import java.sql.SQLException;
import java.util.List;

import vn.giki.rest.entity.Pay;

public interface PayDAO {
	
	void save(int userId, String platform, String pakage, String product, String reciept, String dateStart, String dateEnd) throws SQLException;

	boolean isExists(int userId, String product) throws SQLException;
	
	List<Pay> getListPayByDate() throws SQLException;
	
	void updateLastCheck(int id)throws SQLException;
	
	void updateDateEnd(int id, String dateEnd) throws SQLException;
	
}

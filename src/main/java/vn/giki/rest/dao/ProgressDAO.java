package vn.giki.rest.dao;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public interface ProgressDAO {
	List<Hashtable<String, Object>> allBadges(int userID) throws SQLException;
	Map<String, Object> allTimeData(int userID, List<Hashtable<String, Object>> badges)throws SQLException;
}

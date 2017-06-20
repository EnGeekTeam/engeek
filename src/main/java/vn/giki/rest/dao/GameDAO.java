package vn.giki.rest.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GameDAO {
	
	HashMap<String, Object>game2choice (String wordID) throws SQLException;
	Map<String, Object> game2data (List<String> listword) throws SQLException;
	boolean isReadyForGame1(int userID) throws SQLException;
	boolean isReadyForGame2 (int userID) throws SQLException;
	boolean isReadyForGame3 (int userID) throws SQLException;
	List<String>game2list(int userID)throws SQLException;
}

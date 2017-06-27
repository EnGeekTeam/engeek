package vn.giki.rest.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.giki.rest.entity.Game1;
import vn.giki.rest.entity.Game2;

public interface GameDAO {
	
	HashMap<String, Object>game2choice (String wordID, int userId) throws Exception;
//	Map<String, Object> game2data (List<String> listword) throws Exception;
	boolean isReadyForGame1(int userID) throws Exception;
	boolean isReadyForGame2 (int userID) throws Exception;
	boolean isReadyForGame3 (int userID) throws Exception;
	List<String>game2list(int userID)throws Exception;
	List<Game2> game2data(List<String>listword, int userId) throws Exception;
	List<Game1> game1data(int userId)throws Exception;
}

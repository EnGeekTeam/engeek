package vn.giki.rest.dao;

import java.sql.SQLException;

public interface UserWordDAO {

	boolean isExists(int userId, String wordId)  throws SQLException ;

	void insertUserWord(int userId, String wordId)  throws SQLException ;
	
	void update(int userId, String wordId, boolean isWrong) throws SQLException;

}

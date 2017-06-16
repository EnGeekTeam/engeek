package vn.giki.rest.dao;

public interface UserDeckDAO {
	void save(String deckId, int userId) throws Exception;
	
	boolean isExists(String deckId, int userId) throws Exception;
}

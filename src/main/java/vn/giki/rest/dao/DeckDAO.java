package vn.giki.rest.dao;

import java.util.HashMap;
import java.util.List;

import vn.giki.rest.entity.Deck;

public interface DeckDAO {

	List<Deck> getListByIdPac(String idPack) throws Exception;

	HashMap<String, Object> getInfoById(String id) throws Exception;

	boolean isExists(String deckId) throws Exception;
}

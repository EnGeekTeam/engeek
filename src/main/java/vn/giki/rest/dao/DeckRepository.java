package vn.giki.rest.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.giki.rest.entity.Deck;

public interface DeckRepository extends PagingAndSortingRepository<Deck, String> {

}

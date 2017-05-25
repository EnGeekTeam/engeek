package vn.giki.rest.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.giki.rest.entity.UserDeck;

public interface UserDeckRepository extends PagingAndSortingRepository<UserDeck, Integer> {

}

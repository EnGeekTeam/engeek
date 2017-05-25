package vn.giki.rest.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.giki.rest.entity.UserWord;

public interface UserWordRepository extends PagingAndSortingRepository<UserWord, Integer> {

}

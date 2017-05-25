package vn.giki.rest.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.giki.rest.entity.Word;

public interface WordRepository extends PagingAndSortingRepository<Word, String> {

}

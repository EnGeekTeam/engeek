package vn.giki.rest.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import vn.giki.rest.entity.User;


public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	Iterable<User> findAll();
	
	User findByUserName(String userName);
	
	User findByGoogleId(String googleId);
	
	User findByFacebookId(String facebookId);
	
	
}

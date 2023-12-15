package com.alien.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.alien.security.entity.UserModel;


public interface UserRepo extends CrudRepository<UserModel,Integer>{
	
	 boolean existsByUsername(String username);
	
	public List<UserModel> findByName(String name);
	             /*find = introducer
	             By = criteria
	             Name, User name = property*/
	
	public UserModel findByUsername(String username);
	
	//1. Java Persistent Query Language (JPQL)
	
	@Query("select u from UserModel u")
	public List<UserModel> getAlluser();
//	
//	@Query("select u from UserModel u WHERE u.username=:un")
//	public List<UserModel> findByUserName(@Param("un") String name);
	
	//2. Native SQL
	@Query(value = "select * from UserModel", nativeQuery = true)
	public List<UserModel> getAllUserNative();
	
	
}

package com.anlohse.backend.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anlohse.backend.model.entities.User;

public interface UserRepository extends  CrudRepository<User, String> {

	@Query("from User u where u.username = ?1 and u.password = ?2")
	User findByUsernameAndPassword(String username, String password);

}

package com.eureka.spring.repository.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.eureka.spring.db.databaseConnection;
import com.eureka.spring.model.User;
import com.eureka.spring.repository.UserRepository;
@Repository
public class UserRepositoryImpl implements UserRepository{
	
	@Autowired
	private databaseConnection database;
	
	@Override
	public User loginControl(String username, String password) {
		User user = database.dbLogin(username, password);
		return user;
	}

}

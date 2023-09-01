package com.eureka.spring.repository;

import com.eureka.spring.model.User;

public interface UserRepository {

	User loginControl(String username, String password);
}
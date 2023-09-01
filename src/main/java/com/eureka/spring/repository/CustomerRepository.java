package com.eureka.spring.repository;

import java.util.List;

import com.eureka.spring.model.Customer;

public interface CustomerRepository {
	Boolean saveCustomer(String name, String surname, String address, String tc, Float balance);
	List<Customer> getCustomers();
}

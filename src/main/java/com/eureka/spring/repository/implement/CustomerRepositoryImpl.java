package com.eureka.spring.repository.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eureka.spring.db.databaseConnection;
import com.eureka.spring.model.Customer;
import com.eureka.spring.repository.CustomerRepository;
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

	@Autowired
	private databaseConnection database;
	
	@Override
	public Boolean saveCustomer(String name, String surname, String address, String tc, Float balance) {
		Boolean isSave = database.dbCustomerSave(name, surname, address, tc, balance);
		return isSave;
	}

	@Override
	public List<Customer> getCustomers() {
		List<Customer> customer = database.dbGetCustomers();
		return customer;
	}

}

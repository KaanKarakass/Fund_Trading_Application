package com.eureka.spring.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.eureka.spring.model.Customer;
import com.eureka.spring.repository.implement.CustomerRepositoryImpl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class CustomerPageBean implements Serializable{
    private static final Logger logger = LogManager.getLogger(CustomerPageBean.class);
    
    private List<Customer> customers;

	private static final long serialVersionUID = -5153134576381837452L;
	private String name;
	private String surname; 
	private String tc;
	private String address;
	private Float balance;
	
	
	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTc() {
		return tc;
	}

	public void setTc(String tc) {
		this.tc = tc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Autowired
	private CustomerRepositoryImpl customerRepositoryImpl;
	
	@PostConstruct
    public void init() {
        listCustomers();
    }
	
	public void save() {
		Boolean check = customerRepositoryImpl.saveCustomer(name, surname, address, tc, balance);
		logger.info("Record Added "+check);
		clean();
		listCustomers();
	}
	
	public void listCustomers() {
		setCustomers(customerRepositoryImpl.getCustomers());
		for (Customer customer : customers) {
			logger.info("Customer ID: " + customer.getP_ID());
			logger.info("Name: " + customer.getName());
			logger.info("Surname: " + customer.getSurname());
			logger.info("Address: " + customer.getAddress());
			logger.info("TC: " + customer.getTc());
			logger.info("Balance: " + customer.getBalance());
			logger.info("-----------------------");
	    }
	}
	
	public void clean() {
		this.name = null;
		this.surname = null;
		this.address = null;
		this.tc = null;
		this.balance = null;
	}
	
}

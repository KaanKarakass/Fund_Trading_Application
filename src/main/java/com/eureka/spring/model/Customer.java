package com.eureka.spring.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long p_ID;
	
	private String name;
	private String surname;
	private String address;
	private String tc;
	private Date firstLogin;
	private Float balance;
	public Long getP_ID() {
		return p_ID;
	}
	public void setP_ID(Long p_ID) {
		this.p_ID = p_ID;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTc() {
		return tc;
	}
	public void setTc(String tc) {
		this.tc = tc;
	}
	public Date getFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(Date firstLogin) {
		this.firstLogin = firstLogin;
	}
	public Float getBalance() {
		return balance;
	}
	public void setBalance(Float balance) {
		this.balance = balance;
	}
	
}

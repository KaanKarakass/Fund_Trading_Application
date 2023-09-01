package com.eureka.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long p_ID;

    private String p_name;
    private String p_surname;
    private String p_mail;
    private String p_username;
    private String p_password;
	public Long getP_ID() {
		return p_ID;
	}
	public void setP_ID(Long p_ID) {
		this.p_ID = p_ID;
	}
	public String getP_name() {
		return p_name;
	}
	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public String getP_surname() {
		return p_surname;
	}
	public void setP_surname(String p_surname) {
		this.p_surname = p_surname;
	}
	public String getP_mail() {
		return p_mail;
	}
	public void setP_mail(String p_mail) {
		this.p_mail = p_mail;
	}
	public String getP_username() {
		return p_username;
	}
	public void setP_username(String p_username) {
		this.p_username = p_username;
	}
	public String getP_password() {
		return p_password;
	}
	public void setP_password(String p_password) {
		this.p_password = p_password;
	}



}

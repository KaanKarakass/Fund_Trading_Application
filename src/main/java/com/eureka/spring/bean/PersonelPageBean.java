package com.eureka.spring.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.eureka.spring.model.Personel;
import com.eureka.spring.repository.implement.PersonelRepositoryImpl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class PersonelPageBean implements Serializable {
	private static final Logger logger = LogManager.getLogger(PersonelPageBean.class);
	    
	private List<Personel> personels;
	private static final long serialVersionUID = 5828176128613400708L;
	
	private String p_name;
	private String p_surname;
	private String p_mail;
	private String p_username;
	private String p_password;
	
    private String p_role;

	
    
    public List<Personel> getPersonels() {
		return personels;
	}


	public void setPersonels(List<Personel> personels) {
		this.personels = personels;
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


	public String getP_role() {
		return p_role;
	}


	public void setP_role(String p_role) {
		this.p_role = p_role;
	}

	@Autowired
    private PersonelRepositoryImpl personelRepositoryImpl;
    
    @PostConstruct
    public void init() {
        listPersonels();
    }
    
    
    public void save() {
		Boolean check = personelRepositoryImpl.savePersonel(p_name, p_surname, p_mail, p_username, p_password,p_role);
		logger.info("Record Added "+check);
		clean();
		listPersonels();
	}

	public void listPersonels() {
		
		setPersonels(personelRepositoryImpl.getPersonels());
		for (Personel personel : personels) {
			logger.info("Personel ID: " + personel.getP_ID());
			logger.info("Name: " + personel.getP_name());
			logger.info("Surname: " + personel.getP_surname());
			logger.info("Mail: " + personel.getP_mail());
			logger.info("UserName: " + personel.getP_username());
			logger.info("Role: " + personel.getP_role());
			logger.info("-----------------------");
	    }
		
	}
	
	
	
	public void clean() {
		this.p_name = null;
		this.p_surname = null;
		this.p_mail = null;
		this.p_username = null;
		this.p_password = null;
	}
	
    
    
    

}

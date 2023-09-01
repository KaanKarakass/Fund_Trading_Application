package com.eureka.spring.repository;

import java.util.List;

import com.eureka.spring.model.Personel;

public interface PersonelRepository {
	
	Boolean savePersonel(String p_name, String p_surname, String p_mail, String p_username, String p_password,String p_role);
	List<Personel> getPersonels();

}

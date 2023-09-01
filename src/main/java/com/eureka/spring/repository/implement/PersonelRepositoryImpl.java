package com.eureka.spring.repository.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eureka.spring.db.databaseConnection;
import com.eureka.spring.model.Personel;
import com.eureka.spring.repository.PersonelRepository;

@Repository
public class PersonelRepositoryImpl implements  PersonelRepository {

	@Autowired
	private databaseConnection database;
	@Override
	public Boolean savePersonel(String p_name, String p_surname, String p_mail, String p_username, String p_password,
			String p_role) {
		
		Boolean isSave = database.dbPersonelSave(p_name, p_surname,p_mail,p_username,p_password,p_role);
		return isSave;
	}

	@Override
	public List<Personel> getPersonels() {
		List<Personel> personels = database.dbGetPersonels();
		return personels;
	}

}

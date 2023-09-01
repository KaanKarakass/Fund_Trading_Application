package com.eureka.spring.repository.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eureka.spring.db.databaseConnection;
import com.eureka.spring.model.FundsIdentification;
import com.eureka.spring.repository.FundIdentificationRepository;
@Repository
public class FundIdentificationRepositoryImpl implements FundIdentificationRepository{

	@Autowired
	private databaseConnection database;
	
	@Override
	public Boolean saveFundIdentification(String fundName, Integer fundNo, String fundType, String currency) {
		fundName = fundName.substring(0, 1).toUpperCase() + fundName.substring(1).toLowerCase();
	    fundType = capitalizeWords(fundType); 
	    currency = currency.toUpperCase(); 
	    return database.dbSaveFundIdentification(fundName, fundNo, fundType, currency);
	}

	@Override
	public List<FundsIdentification> listFunds() {
		return database.dbGetFunds();
	}
	
	public String capitalizeWords(String input) {
	    StringBuilder result = new StringBuilder();
	    String[] words = input.split("\\s+");
	    for (String word : words) {
	        if (word.length() > 0) {
	            result.append(Character.toUpperCase(word.charAt(0)));
	            result.append(word.substring(1).toLowerCase());
	            result.append(" ");
	        }
	    }
	    return result.toString().trim();
	}

}

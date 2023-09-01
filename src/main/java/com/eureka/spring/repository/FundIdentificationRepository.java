package com.eureka.spring.repository;

import java.util.List;

import com.eureka.spring.model.FundsIdentification;

public interface FundIdentificationRepository {
	Boolean saveFundIdentification(String fundName, Integer fundNo, String fundType, String currency);
	
	List<FundsIdentification> listFunds();
}	

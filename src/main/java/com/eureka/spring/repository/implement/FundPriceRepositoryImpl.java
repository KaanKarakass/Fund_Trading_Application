package com.eureka.spring.repository.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eureka.spring.db.databaseConnection;
import com.eureka.spring.model.FundsPrice;
import com.eureka.spring.repository.FundPriceRepository;
@Repository
public class FundPriceRepositoryImpl implements FundPriceRepository{

	@Autowired
	private databaseConnection database;
	
	@Override
	public Boolean saveFundPrice(String fundName, Integer fundNo, Float fundPrice) {
		return database.dbSaveFundPrice(fundName, fundNo, fundPrice);
	}

	@Override
	public List<FundsPrice> listFundsPrice() {
		return database.dbGetFundPrice();
	}

}

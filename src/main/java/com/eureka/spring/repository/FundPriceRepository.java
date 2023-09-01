package com.eureka.spring.repository;

import java.util.List;

import com.eureka.spring.model.FundsPrice;

public interface FundPriceRepository {
	Boolean saveFundPrice(String fundName, Integer fundNo, Float fundPrice);
	List<FundsPrice> listFundsPrice();
}

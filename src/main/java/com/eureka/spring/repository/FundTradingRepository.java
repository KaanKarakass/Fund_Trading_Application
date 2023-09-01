package com.eureka.spring.repository;

import java.util.List;

import com.eureka.spring.model.BalanceControl;
import com.eureka.spring.model.FundTrading;

public interface FundTradingRepository {
	Boolean tradeFund(Integer portfolio_no, Integer fund_no, String process_type, Float paid, Float price, Float piece, Float available_balance);
	BalanceControl control(Integer portfolio_no, Integer fund_no);
	List<FundTrading> getList();
}

package com.eureka.spring.repository.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eureka.spring.db.databaseConnection;
import com.eureka.spring.model.BalanceControl;
import com.eureka.spring.model.FundTrading;
import com.eureka.spring.repository.FundTradingRepository;
@Repository
public class FundTradingRepositoryImpl implements FundTradingRepository{

	@Autowired
	private databaseConnection database;
	
	@Override
	public Boolean tradeFund(Integer portfolio_no, Integer fund_no, String process_type, Float paid, Float price,
			Float piece, Float available_balance) {
		return database.dbTradeFunds(portfolio_no, fund_no, process_type, paid, price, piece, available_balance);
	}

	@Override
	public BalanceControl control(Integer portfolio_no, Integer fund_no) {
		return database.dbBalanceControl(portfolio_no, fund_no);
	}

	@Override
	public List<FundTrading> getList() {
		return database.dbGetTransactions();
	}
	
}

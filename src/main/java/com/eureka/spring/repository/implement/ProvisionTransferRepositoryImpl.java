package com.eureka.spring.repository.implement;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eureka.spring.db.databaseConnection;
import com.eureka.spring.model.BalanceControl;
import com.eureka.spring.model.ProvisionTransfer;
import com.eureka.spring.repository.ProvisionTransferRepository;

@Repository
public class ProvisionTransferRepositoryImpl implements ProvisionTransferRepository{
	
	@Autowired
	private databaseConnection database;
	
	@Override
	public Boolean saveProvisionTransferProcess(Integer portfolio_no, Date operationDate, Float amount,
			String process_type, Float newBalance) {
		return database.dbSaveProvisionTransfer(portfolio_no, operationDate, amount, process_type, newBalance);
	}

	@Override
	public BalanceControl control(Integer portfolio_no) {
		return database.dbBalanceControl(portfolio_no, -1);
	}

	@Override
	public List<ProvisionTransfer> listProvisionTransfer() {
		return database.dbGetProvisionTransfer();
	}

}

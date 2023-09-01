package com.eureka.spring.repository;

import java.sql.Date;
import java.util.List;

import com.eureka.spring.model.BalanceControl;
import com.eureka.spring.model.ProvisionTransfer;

public interface ProvisionTransferRepository {
	Boolean saveProvisionTransferProcess(Integer portfolio_no, Date operationDate, Float amount, String process_type, Float newBalance);
	BalanceControl control(Integer portfolio_no);
	List<ProvisionTransfer> listProvisionTransfer();
}

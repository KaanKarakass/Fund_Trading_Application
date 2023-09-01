package com.eureka.spring.bean;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.eureka.spring.model.BalanceControl;
import com.eureka.spring.model.ProvisionTransfer;
import com.eureka.spring.repository.implement.ProvisionTransferRepositoryImpl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped; 
import jakarta.inject.Named;


@Named
@SessionScoped
public class ProvisionPageBean implements Serializable {
	private static final Logger logger = LogManager.getLogger(ProvisionPageBean.class);
	private static final long serialVersionUID = -321485520649160L;

	private BalanceControl blc;
	
    private LocalDate operationDate =LocalDate.now();  

	private List<ProvisionTransfer> provisionTransfers;
	
    private Integer portfolio_no;
	
	private String process_type;
	
	private Float amount;

	private Float balance;
	
	
	
	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public BalanceControl getBlc() {
		return blc;
	}

	public void setBlc(BalanceControl blc) {
		this.blc = blc;
	}

	public LocalDate getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(LocalDate operationDate) {
		this.operationDate = operationDate;
	}

	public List<ProvisionTransfer> getProvisionTransfers() {
		return provisionTransfers;
	}

	public void setProvisionTransfers(List<ProvisionTransfer> provisionTransfers) {
		this.provisionTransfers = provisionTransfers;
	}

	public Integer getPortfolio_no() {
		return portfolio_no;
	}

	public void setPortfolio_no(Integer portfolio_no) {
		this.portfolio_no = portfolio_no;
	}

	public String getProcess_type() {
		return process_type;
	}

	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Autowired
	ProvisionTransferRepositoryImpl provisionTransfer;
	
	
	@PostConstruct
    public void init() {
		ListProcess();
    }
	public void balanceControl() {
		if(portfolio_no !=null) {
			setBlc(provisionTransfer.control(portfolio_no));
			setBalance(blc.getBalance());
		}
		
	}
    public void clean() {
    	this.portfolio_no = null;
    	this.amount = null;
    	this.process_type = null;
    	this.balance = null;
    }
   public void save() {
	   logger.info("portfolio_no "+portfolio_no);
	   logger.info("process_type "+process_type);
	   logger.info("amount "+amount);
	   logger.info("operationDate "+operationDate);
	   Date sqlDate = Date.valueOf(operationDate);
	   if(process_type.equals("Provizyon")) {
		   setBalance(balance + amount);
		   logger.info("is saved "+provisionTransfer.saveProvisionTransferProcess(portfolio_no, sqlDate, amount, process_type, balance));
	   }else if(process_type.equals("Havale")) {
		   if(balance > amount) {
			   setBalance(balance - amount);
			   logger.info("is saved "+provisionTransfer.saveProvisionTransferProcess(portfolio_no, sqlDate, amount, process_type, balance));
		   }
	   }
	   clean();
   }
   public void ListProcess() {
	   setProvisionTransfers(provisionTransfer.listProvisionTransfer());
	   for (ProvisionTransfer transaction : provisionTransfers) {
			logger.info("Portfolio No: " + transaction.getPortfolio_no());
			logger.info("Amount: " + transaction.getAmount());
			logger.info("Date: " + transaction.getTransaction_date());
			logger.info("Type: " + transaction.getProcess_type());
			logger.info("-----------------------");
	    }
   }
 
}


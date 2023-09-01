package com.eureka.spring.bean;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.eureka.spring.model.FundsIdentification;
import com.eureka.spring.repository.implement.FundIdentificationRepositoryImpl;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class FundIdentificationPageBean {
    private static final Logger logger = LogManager.getLogger(FundIdentificationPageBean.class);
	private List<FundsIdentification> fundIdent;

	private static final long serialVersionUID = 535678192413214L;

	private String fundName;
	private Integer fundNo;
	private String fundType;
	private String fundCurrency;
	
	
	public List<FundsIdentification> getFundIdent() {
		return fundIdent;
	}

	public void setFundIdent(List<FundsIdentification> fundIdent) {
		this.fundIdent = fundIdent;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public Integer getFundNo() {
		return fundNo;
	}

	public void setFundNo(Integer fundNo) {
		this.fundNo = fundNo;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getFundCurrency() {
		return fundCurrency;
	}

	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Autowired
	FundIdentificationRepositoryImpl fundRepository;
	
	@PostConstruct
    public void init() {
		listFundsIndent();
    }
	
	public void listFundsIndent() {
		setFundIdent(fundRepository.listFunds());
		for (FundsIdentification fund : fundIdent) {
			logger.info("Name: " + fund.getFundName());
			logger.info("No: " + fund.getFundNo());
			logger.info("Type: " + fund.getFundType());
			logger.info("-----------------------");
	    }
	}

	public void fundIdentClean() {
		this.fundName = null;
		this.fundCurrency = null;
		this.fundNo = null;
		this.fundType = null;
	}
	
	public void fundIdentSave() {
		logger.info("name :" +fundName);
		logger.info("fundNo :" +fundNo);
		logger.info("fundType :" +fundType);
		logger.info("fundCurrency :" +fundCurrency);
		logger.info("is saved :" +fundRepository.saveFundIdentification(fundName, fundNo, fundType, fundCurrency));
		fundIdentClean();
		listFundsIndent();
	}

}

package com.eureka.spring.bean;

import java.io.Serializable;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.eureka.spring.model.FundsPrice;
import com.eureka.spring.repository.implement.FundPriceRepositoryImpl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class FundPricePageBean implements Serializable {
	private List<FundsPrice> funds;
	private static final long serialVersionUID = 11234112314L;
    private static final Logger logger = LogManager.getLogger(FundPricePageBean.class);

	private String fundName;
	private Integer fundNo;
	private Float fundPrice;
	
	public List<FundsPrice> getFunds() {
		return funds;
	}
	public void setFunds(List<FundsPrice> funds) {
		this.funds = funds;
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
	public Float getFundPrice() {
		return fundPrice;
	}
	public void setFundPrice(Float fundPrice) {
		this.fundPrice = fundPrice;
	}
	public FundPriceRepositoryImpl getFundPriceRepository() {
		return fundPriceRepository;
	}
	public void setFundPriceRepository(FundPriceRepositoryImpl fundPriceRepository) {
		this.fundPriceRepository = fundPriceRepository;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Autowired
	private FundPriceRepositoryImpl fundPriceRepository;
	
	
	@PostConstruct
    public void init() {
		listFunds();
    }
	public void fundClean() {
		this.fundName = null;
		this.fundNo = null;
		this.fundPrice = null;
	}
	public void listFunds() {
		setFunds(fundPriceRepository.listFundsPrice());
		for (FundsPrice fund : funds) {
			logger.info("Name: " + fund.getFund_name());
			logger.info("No: " + fund.getFund_no());
			logger.info("Price: " + fund.getFund_price());
			logger.info("-----------------------");
	    }
	}
	public void fundSave(){
		logger.info("name "+ fundName);
		logger.info("fundNo "+ fundNo);
		logger.info("price "+ fundPrice);
		logger.info("is inserted"+ fundPriceRepository.saveFundPrice(fundName, fundNo, fundPrice));
		fundClean();
		listFunds();
	}
	
	
}

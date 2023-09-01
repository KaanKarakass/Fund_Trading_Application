package com.eureka.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FundsPrice {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long p_ID;
	private Integer fund_no;
	private String fund_name;
	private Float fund_price;
	private String fund_type;
	public Long getP_ID() {
		return p_ID;
	}
	public void setP_ID(Long p_ID) {
		this.p_ID = p_ID;
	}
	public Integer getFund_no() {
		return fund_no;
	}
	public void setFund_no(Integer fund_no) {
		this.fund_no = fund_no;
	}
	public String getFund_name() {
		return fund_name;
	}
	public void setFund_name(String fund_name) {
		this.fund_name = fund_name;
	}
	public Float getFund_price() {
		return fund_price;
	}
	public void setFund_price(Float fund_price) {
		this.fund_price = fund_price;
	}
	public String getFund_type() {
		return fund_type;
	}
	public void setFund_type(String fund_type) {
		this.fund_type = fund_type;
	}
	
	
}

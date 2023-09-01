package com.eureka.spring.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FundTrading {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long p_ID;
	
	private Integer porfolio_no;
	private Integer fund_no;
	private String fund_name;
	private Date process_date;
	private String process_type;
	private Float piece;
	public Long getP_ID() {
		return p_ID;
	}
	public void setP_ID(Long p_ID) {
		this.p_ID = p_ID;
	}
	public Integer getPorfolio_no() {
		return porfolio_no;
	}
	public void setPorfolio_no(Integer porfolio_no) {
		this.porfolio_no = porfolio_no;
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
	public Date getProcess_date() {
		return process_date;
	}
	public void setProcess_date(Date process_date) {
		this.process_date = process_date;
	}
	public String getProcess_type() {
		return process_type;
	}
	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}
	public Float getPiece() {
		return piece;
	}
	public void setPiece(Float piece) {
		this.piece = piece;
	}
	
}

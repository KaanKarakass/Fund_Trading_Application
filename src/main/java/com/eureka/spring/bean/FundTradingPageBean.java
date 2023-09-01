package com.eureka.spring.bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import com.eureka.spring.model.BalanceControl;
import com.eureka.spring.model.FundTrading;
import com.eureka.spring.repository.implement.FundTradingRepositoryImpl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;


import org.xhtmlrenderer.pdf.ITextRenderer;
import com.lowagie.text.pdf.BaseFont;
@Named
@SessionScoped
public class FundTradingPageBean implements Serializable {
	private static final long serialVersionUID = 50334112314L;
    private static final Logger logger = LogManager.getLogger(FundTradingPageBean.class);
    private List<FundTrading> transactions;
    private BalanceControl blc;
    private Integer portfolio_no;
    private Integer fund_no;
    private String process_type;
    private Float piece;
    private Float price;
    private Float cash;
    private Float balance;

   
	public List<FundTrading> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<FundTrading> transactions) {
		this.transactions = transactions;
	}

	public BalanceControl getBlc() {
		return blc;
	}

	public void setBlc(BalanceControl blc) {
		this.blc = blc;
	}

	public Integer getPortfolio_no() {
		return portfolio_no;
	}

	public void setPortfolio_no(Integer portfolio_no) {
		this.portfolio_no = portfolio_no;
	}

	public Integer getFund_no() {
		return fund_no;
	}

	public void setFund_no(Integer fund_no) {
		this.fund_no = fund_no;
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

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getCash() {
		return cash;
	}

	public void setCash(Float cash) {
		this.cash = cash;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Autowired
	FundTradingRepositoryImpl fundTradingRepositoryImpl;
	
	@PostConstruct
    public void init() {
		listTransaction();
    }
	
	
	public void listTransaction() {
		setTransactions(fundTradingRepositoryImpl.getList());
		for (FundTrading transaction : transactions) {
			logger.info("Name: " + transaction.getFund_name());
			logger.info("Fun No: " + transaction.getFund_no());
			logger.info("Portfolio No: " + transaction.getPorfolio_no());
			logger.info("-----------------------");
	    }
	}
	
	public void balanceControl() {
		if(portfolio_no !=null & fund_no !=null) {
			setBlc(fundTradingRepositoryImpl.control(portfolio_no, fund_no));
			setPrice(blc.getPrice());
			setBalance(blc.getBalance());
		}
		
	}
	
	
	public void cleanFund() {
		this.balance = null;
		this.cash = null;
		this.portfolio_no = null;
		this.fund_no = null;
		this.price = null;
		this.process_type = null;
		this.piece = null;
	}
	
	public void totalPrice() {
		logger.info("piece "+ piece);
		logger.info("price "+ price);
		if(piece !=null & price!=null) {
			setCash(piece * price);
		}
	}
	
	public void fundTrade() {
		logger.info("portfolio_no "+ portfolio_no);
		logger.info("fund_no "+ fund_no);
		logger.info("process_type "+ process_type);
		logger.info("piece "+ piece);
		logger.info("price "+ price);
		logger.info("cash "+ cash);
		
		if(process_type.equals("AL")) {
			if(balance > cash) {
				setBalance(balance - cash);
				logger.info("is saved "+ fundTradingRepositoryImpl.tradeFund(portfolio_no, fund_no, process_type, cash, price, piece, balance));
			}
		}else if(process_type.equals("SAT")){
			setBalance(balance + cash);
			logger.info("is saved "+ fundTradingRepositoryImpl.tradeFund(portfolio_no, fund_no, process_type, cash, price, piece, balance));
		}
		cleanFund();
	}
	
	public List<FundTrading> getDailyTopFunds() { //homepagede yer alan grafik için kullanıldı
	    LocalDate currentDate = LocalDate.now();
	    // transactions listesini sıralayarak en çok alınanları seç
	    List<FundTrading> sortedTransactions = new ArrayList<>(transactions);
	    sortedTransactions.sort(Comparator.comparing(FundTrading::getPiece).reversed());

	    // Sadece işlem tarihi güncel tarih olanları filtrele
	    List<FundTrading> dailyTopFunds = sortedTransactions.stream()
	            .filter(transaction -> transaction.getProcess_date().toLocalDate().isEqual(currentDate))
	            .collect(Collectors.toList());

	    // Fonları gruplayarak aynı olanları topla
	    Map<Integer, Float> fundTotals = dailyTopFunds.stream()
	            .collect(Collectors.groupingBy(FundTrading::getFund_no, Collectors.reducing(0f, FundTrading::getPiece, Float::sum)));

	    int limit = 4; // en çok işlenen 3  fonu seç
	    return fundTotals.entrySet().stream()
	            .sorted(Map.Entry.<Integer, Float>comparingByValue().reversed())
	            .limit(limit)
	            .map(entry -> dailyTopFunds.stream()
	                    .filter(transaction -> transaction.getFund_no().equals(entry.getKey()))
	                    .findFirst()
	                    .orElse(null))
	            .collect(Collectors.toList());
	}

		 
		private String generateHtmlContent(List<FundTrading> dailyTransactions) {
		    StringBuilder htmlContent = new StringBuilder();
		    htmlContent.append("<html><head><style>");
		    htmlContent.append("body { text-align: center; font-family: Arial, sans-serif; }");
		    htmlContent.append("h1 { margin-bottom: 20px; }");
		    htmlContent.append("table { margin: 0 auto; text-align: left; border-collapse: collapse; width: 80%; }");
		    htmlContent.append("table, th, td { border: 1px solid black; }");
		    htmlContent.append("th, td { padding: 10px; }");
		    htmlContent.append("</style></head><body>");
		    
		    // Add the title
		    htmlContent.append("<h2>GUNLUK FON ALIM SATIM RAPORU</h2>");
		    
		    // Add the table
		    htmlContent.append("<table>");
		    htmlContent.append("<tr>");
		    htmlContent.append("<th>PORTFOY NO</th>");
		    htmlContent.append("<th>ISLEM TARIHI</th>");
		    htmlContent.append("<th>FON ADI</th>");
		    htmlContent.append("<th>ADET</th>");
		    htmlContent.append("<th>ISLEM TIPI</th>");
		    htmlContent.append("</tr>");

		    // Tablo içeriği
		    for (FundTrading transaction : dailyTransactions) {
		        htmlContent.append("<tr>");
		        htmlContent.append("<td>" + transaction.getPorfolio_no() + "</td>");
		        htmlContent.append("<td>" + transaction.getProcess_date() + "</td>");
		        htmlContent.append("<td>" + transaction.getFund_name() + "</td>");
		        htmlContent.append("<td>" + transaction.getPiece() + "</td>");
		        htmlContent.append("<td>" + transaction.getProcess_type() + "</td>");
		        htmlContent.append("</tr>");
		    }

		    htmlContent.append("</table>");
		    htmlContent.append("</body></html>");

		    return htmlContent.toString();
		}

		public void generatePDFReport() {
		    FacesContext context = FacesContext.getCurrentInstance();
		    ExternalContext externalContext = context.getExternalContext();
		    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		   
		    LocalDate currentDate = LocalDate.now();
		    List<FundTrading> dailyTransactions = transactions.stream()
		        .filter(transaction -> transaction.getProcess_date().toLocalDate().isEqual(currentDate))
		        .collect(Collectors.toList());
		    
		    String htmlContent = generateHtmlContent(dailyTransactions);

		    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
		            ITextRenderer renderer = new ITextRenderer();
		            renderer.getFontResolver().addFont("src/main/resources/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		            renderer.setDocumentFromString(htmlContent);
		            renderer.layout();
		            renderer.createPDF(outputStream);

		            response.setContentType("application/pdf;charset=UTF-8");
		            response.setCharacterEncoding("UTF-8");
		            response.setHeader("Content-Disposition", "attachment; filename=\"FundTradingReport.pdf\"");
		            response.getOutputStream().write(outputStream.toByteArray());
		            response.getOutputStream().flush();

		            context.responseComplete();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		
}

package com.eureka.spring.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eureka.spring.model.BalanceControl;
import com.eureka.spring.model.Customer;
import com.eureka.spring.model.FundTrading;
import com.eureka.spring.model.FundsIdentification;
import com.eureka.spring.model.FundsPrice;
import com.eureka.spring.model.Personel;
import com.eureka.spring.model.ProvisionTransfer;
import com.eureka.spring.model.User;

@Component
public class databaseConnection {
	
    private static final Logger logger = LogManager.getLogger(databaseConnection.class);

	
	@Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
	
	public User dbLogin(String enteredUsername, String enteredPassword) {
		User user = new User();
		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
	        String query = "SELECT * FROM Personel WHERE p_username = ? AND p_password = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, enteredUsername);
	        preparedStatement.setString(2, enteredPassword);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        
	        
	        while(resultSet.next()) {
	        	user.setP_ID(resultSet.getLong(1));
	        	user.setP_name(resultSet.getString("p_name"));
	        	user.setP_surname(resultSet.getString("p_surname"));
	        	user.setP_mail(resultSet.getString("p_mail"));
	        	user.setP_username(resultSet.getString("p_username"));
	        	user.setP_password(resultSet.getString("p_password"));
	        }
	        
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
        	System.out.println("Connection Error");
            e.printStackTrace();
        }
		return user;
	}
	
	public Boolean dbCustomerSave(String enteredName, String enteredSurname, String enteredAddress, String enteredTc, Float balance) {
	    LocalDate localDate = LocalDate.now();
	    Date sqlDate = Date.valueOf(localDate);
	    try {
	        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
	        String query = "INSERT INTO dbo.customer (name, surname, address, tc, firstLogin, balance)\r\n"
	                + "VALUES (?, ?, ?, ?, ?, ?)";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, enteredName);
	        preparedStatement.setString(2, enteredSurname);
	        preparedStatement.setString(3, enteredAddress);
	        preparedStatement.setString(4, enteredTc);
	        preparedStatement.setDate(5, sqlDate);
	        preparedStatement.setFloat(6, balance);
	        int rowsAffected = preparedStatement.executeUpdate();

	        preparedStatement.close();
	        connection.close();

	        return rowsAffected > 0;
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public List<Customer> dbGetCustomers() {
	    List<Customer> customerList = new ArrayList<>(); 
		try {
			 Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			    Statement statement = connection.createStatement();
			    
			    String query = "SELECT * FROM customer";
			    ResultSet resultSet = statement.executeQuery(query);
			    
		        while(resultSet.next()) {
		            Customer customer = new Customer();
		        	customer.setP_ID(resultSet.getLong(1));
		        	customer.setName(resultSet.getString("name"));
		        	customer.setSurname(resultSet.getString("surname"));
		        	customer.setAddress(resultSet.getString("address"));
		        	customer.setTc(resultSet.getString("tc"));
		        	customer.setFirstLogin(resultSet.getDate("firstLogin"));
		        	customer.setBalance(resultSet.getFloat("balance"));
		            customerList.add(customer);
		        }
			    
			    resultSet.close();
			    statement.close();
			    connection.close();
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
		return customerList;
	}
	
	public Boolean dbSaveFundPrice(String fundName, Integer fundNo, Float fundPrice) {
		
		// Convert the first character of fundName to uppercase, and the rest to lowercase
        fundName = fundName.substring(0, 1).toUpperCase() + fundName.substring(1).toLowerCase();
		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
	        String query = "SELECT COUNT(*) AS count FROM fund_identification WHERE fund_name = ? AND fund_no = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, fundName);
	        preparedStatement.setLong(2, fundNo);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            int count = resultSet.getInt("count");
	            logger.info("Count : " + count);
	            if (count < 1) {
	                return false;
	            }
	        }
	        
	        preparedStatement.close(); 
	        
	        
	        query = "MERGE INTO dbo.fund_price AS target "
	        		+ "USING (SELECT ? AS fund_no) AS source "
	        		+ "ON target.fund_no = source.fund_no "
	        		+ "WHEN MATCHED THEN "
	        		+ "UPDATE SET price = ? "
	        		+ "WHEN NOT MATCHED THEN "
	        		+ "INSERT (fund_no, price) "
	        		+ "VALUES (?, ?);";
	       
	        
	        PreparedStatement mergeStatement = connection.prepareStatement(query);
	        mergeStatement.setInt(1, fundNo);
	        mergeStatement.setFloat(2, fundPrice);
	        mergeStatement.setInt(3, fundNo);
	        mergeStatement.setFloat(4, fundPrice);

	        int rowsAffected = mergeStatement.executeUpdate();

	        mergeStatement.close(); 
	        connection.close();

	        return rowsAffected > 0;
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public List<FundsPrice> dbGetFundPrice(){
		List<FundsPrice> funds = new ArrayList<>();
		try {
			 Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			    Statement statement = connection.createStatement();
			    
			    String query = "SELECT \r\n"
			    		+ "    fp.fund_no AS fund_no,\r\n"
			    		+ "    fp.price AS price,\r\n"
			    		+ "    fi.fund_name AS fund_name,\r\n"
			    		+ "    fi.fund_type AS fund_type\r\n"
			    		+ "FROM \r\n"
			    		+ "    fund_price fp\r\n"
			    		+ "JOIN \r\n"
			    		+ "    fund_identification fi ON fp.fund_no = fi.fund_no;";
			    
			    ResultSet resultSet = statement.executeQuery(query);
			    
		        while(resultSet.next()) {
		            FundsPrice fund = new FundsPrice();
		        	fund.setFund_name(resultSet.getString("fund_name"));
		        	fund.setFund_no(resultSet.getInt("fund_no"));
		        	fund.setFund_type(resultSet.getString("fund_type"));
		        	fund.setFund_price(resultSet.getFloat("price"));
		        	funds.add(fund);
		        }
			    
			    resultSet.close();
			    statement.close();
			    connection.close();
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
		return funds;
	}
	
	public Boolean dbSaveFundIdentification(String fundName, Integer fundNo, String fundType, String currency) {
		try {
	        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
	        String query = "INSERT INTO dbo.fund_identification (fund_name, fund_no, fund_type, currency)\r\n"
	        		+ "VALUES (?, ?, ?, ?)";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, fundName);
	        preparedStatement.setInt(2, fundNo);
	        preparedStatement.setString(3, fundType);
	        preparedStatement.setString(4, currency);
	        int rowsAffected = preparedStatement.executeUpdate();

	        preparedStatement.close();
	        connection.close();

	        return rowsAffected > 0;
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
		return false;
	}
	
	public List<FundsIdentification> dbGetFunds(){
		List<FundsIdentification> funds = new ArrayList<>();
		try {
			 Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			    Statement statement = connection.createStatement();
			    
			    String query = "Select * FROM fund_identification";
			    
			    ResultSet resultSet = statement.executeQuery(query);
			    
		        while(resultSet.next()) {
		        	FundsIdentification fund = new FundsIdentification();
		        	fund.setFundName(resultSet.getString("fund_name"));
		        	fund.setFundNo(resultSet.getInt("fund_no"));
		        	fund.setFundType(resultSet.getString("fund_type"));
		        	fund.setCurrency(resultSet.getString("currency"));
		        	funds.add(fund);
		        }
			    
			    resultSet.close();
			    statement.close();
			    connection.close();
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
		return funds;
	}

	public Boolean dbPersonelSave(String p_name, String p_surname, String p_mail, String p_username, String p_password,
			String p_role) {
        int role_id = 1;
		try 
		{
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			
			String takeRoleId = "SELECT * \r\n"
					+ "FROM dbo.Roles WHERE role = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(takeRoleId);
			preparedStatement.setString(1, p_role);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				role_id = resultSet.getInt("roleID");
			}
			
			preparedStatement.close();
			
		    String query = "INSERT INTO dbo.Personel (p_name, p_surname, p_mail, p_username, p_password, p_role)\r\n"
	                + "VALUES (?, ?, ?, ?, ?, ?)";
		    preparedStatement = connection.prepareStatement(query);
		    preparedStatement.setString(1, p_name);
		    preparedStatement.setString(2, p_surname);
		    preparedStatement.setString(3, p_mail);
		    preparedStatement.setString(4, p_username);
		    preparedStatement.setString(5, p_password);
		    preparedStatement.setInt(6, role_id);
		    
		    int rowsAffected = preparedStatement.executeUpdate();

	        preparedStatement.close();
	        connection.close();

	        return rowsAffected > 0;
		    
		}catch (Exception e) {
			System.out.println("Connection Error");
	        e.printStackTrace();
		}
		return false;
	}

	public List<Personel> dbGetPersonels() {
		List<Personel> personelList=new ArrayList<>();
		
		try 
		{
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
		    Statement statement = connection.createStatement();
		    
		    String query = "SELECT *\r\n"
		    		+ "FROM Personel\r\n"
		    		+ "JOIN Roles ON Personel.p_role = Roles.roleID;";
		    ResultSet resultSet = statement.executeQuery(query);
		    
		    while(resultSet.next()) {
	            Personel personel = new Personel();
	        	personel.setP_ID(resultSet.getLong(1));
	        	personel.setP_name(resultSet.getString("p_name"));
	        	personel.setP_surname(resultSet.getString("p_surname"));
	        	personel.setP_mail(resultSet.getString("p_mail"));
	        	personel.setP_username(resultSet.getString("p_username"));
	        	personel.setP_password(resultSet.getString("p_password"));
	        	personel.setP_role(resultSet.getString("role"));
	            personelList.add(personel);
	        }
		    resultSet.close();
		    statement.close();
		    connection.close();
			
		}catch (Exception e) {
			System.out.println("Connection Error");
	        e.printStackTrace();
		}
		
		return personelList;
	}


	
	public Boolean dbTradeFunds(Integer portfolio_no, Integer fund_no, String process_type, Float paid, Float price,
			Float piece, Float available_balance) {
		
		int updateControl = 0;
		int insertControl = 0;
		int rowsAffected = 0;
		LocalDate localDate = LocalDate.now();
	    Date sqlDate = Date.valueOf(localDate);
	    
		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			PreparedStatement preparedStatement = null;
			String query = null;
			if(process_type.equals("SAT")) {
				query = "MERGE INTO dbo.ownedFunds AS target\r\n" +
        		        "USING (SELECT ? AS portfolio_no, ? AS fund_no) AS source\r\n" +
        		        "ON target.portfolio_no = source.portfolio_no AND target.fund_no = source.fund_no\r\n" +
        		        "WHEN MATCHED AND target.piece >= ? THEN\r\n" +
        		        "    UPDATE SET target.piece = target.piece - ?;";
				preparedStatement = connection.prepareStatement(query);
        		preparedStatement.setInt(1, portfolio_no);
        		preparedStatement.setInt(2, fund_no);
        		preparedStatement.setFloat(3, piece);
        		preparedStatement.setFloat(4, piece);
        		insertControl = preparedStatement.executeUpdate();
        		preparedStatement.close();
        	}else if(process_type.equals("AL")){
        		query = "MERGE INTO dbo.ownedFunds AS target\r\n" +
	        	        "USING (SELECT ? AS portfolio_no, ? AS fund_no) AS source\r\n" +
	        	        "ON target.portfolio_no = source.portfolio_no AND target.fund_no = source.fund_no\r\n" +
	        	        "WHEN MATCHED THEN\r\n" +
	        	        "    UPDATE SET target.piece = target.piece + ?\r\n" +
	        	        "WHEN NOT MATCHED THEN\r\n" +
	        	        "    INSERT (portfolio_no, fund_no, piece) VALUES (?, ?, ?);";
	        	preparedStatement = connection.prepareStatement(query);
	        	preparedStatement.setInt(1, portfolio_no);
	        	preparedStatement.setInt(2, fund_no);
	        	preparedStatement.setFloat(3, piece);
	        	preparedStatement.setInt(4, portfolio_no);
	        	preparedStatement.setInt(5, fund_no);
	        	preparedStatement.setFloat(6, piece);
		        insertControl = preparedStatement.executeUpdate();
		        preparedStatement.close();
        	}
			
			
			
			if(insertControl > 0) {
				query = "INSERT INTO dbo.fund_trading (portfolio_no, process_date, fund_no, process_type, paid, price, piece)\r\n"
		        		+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		        preparedStatement = connection.prepareStatement(query);
		        preparedStatement.setInt(1, portfolio_no);
		        preparedStatement.setDate(2, sqlDate);
		        preparedStatement.setInt(3, fund_no);
		        preparedStatement.setString(4, process_type);
		        preparedStatement.setFloat(5, paid);
		        preparedStatement.setFloat(6, price);
		        preparedStatement.setFloat(7, piece);
		        rowsAffected = preparedStatement.executeUpdate();
		        preparedStatement.close();
			}
			
			if(rowsAffected > 0) {
	        	String updateBalanceQuery = "UPDATE dbo.customer\r\n"
		        		+ "SET balance = ?\r\n"
		        		+ "WHERE portfolio_no = ?";
		   
		        preparedStatement = connection.prepareStatement(updateBalanceQuery);
		        preparedStatement.setFloat(1, available_balance);
		        preparedStatement.setInt(2, portfolio_no);
		        updateControl = preparedStatement.executeUpdate();
		        preparedStatement.close();
	        }
	        
	        connection.close();

	        return updateControl > 0;
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
		return false;
	}
	
	public BalanceControl dbBalanceControl(Integer portfolio_no, Integer fund_no) {
	    BalanceControl blc = new BalanceControl();
	    try {
	        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
	        
	        String query = "Select * FROM fund_price WHERE fund_no = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setInt(1, fund_no);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            Float price = resultSet.getFloat("price");
	            blc.setPrice(price);
	        }
	        preparedStatement.close();

	        query = "Select * FROM customer WHERE portfolio_no = ?";
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setInt(1, portfolio_no);
	        resultSet = preparedStatement.executeQuery();
	        
	        if (resultSet.next()) {
	        	logger.info("DATABASE balance  : "+resultSet.getFloat("balance"));
	            Float balance = resultSet.getFloat("balance");
	            blc.setBalance(balance);
	        }
	        
	        preparedStatement.close();
	        resultSet.close();
	        connection.close();
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
	    return blc;
	}
	public List<FundTrading> dbGetTransactions() {
		List<FundTrading> transactions = new ArrayList<>();
		try {
			 Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			    Statement statement = connection.createStatement();
			    
			    String query = "SELECT *\r\n"
			    		+ "FROM fund_trading\r\n"
			    		+ "INNER JOIN fund_identification ON fund_trading.fund_no = fund_identification.fund_no";
			    
			    ResultSet resultSet = statement.executeQuery(query);
			    
		        while(resultSet.next()) {
		        	FundTrading transaction = new FundTrading();
		        	transaction.setFund_name(resultSet.getString("fund_name"));
		        	transaction.setFund_no(resultSet.getInt("fund_no"));
		        	transaction.setPiece(resultSet.getFloat("piece"));
		        	transaction.setPorfolio_no(resultSet.getInt("portfolio_no"));
		        	transaction.setProcess_date(resultSet.getDate("process_date"));
		        	transaction.setProcess_type(resultSet.getString("process_type"));
		        	transactions.add(transaction);
		        }
			    
			    resultSet.close();
			    statement.close();
			    connection.close();
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
		return transactions;
		
	}
	public Boolean dbSaveProvisionTransfer(Integer portfolio_no, Date operationDate, Float amount, String process_type, Float newBalance) {
		int updateControl = 0;
		int rowsAffected = 0;
		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			PreparedStatement preparedStatement = null;
			String query = null;
			query ="UPDATE dbo.customer "
					+ "SET balance = ? "
					+ "WHERE portfolio_no = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setFloat(1, newBalance);//eksilmiş veya arttırılmış para buraya gelcek
    		preparedStatement.setInt(2, portfolio_no);
    		updateControl = preparedStatement.executeUpdate();
    		preparedStatement.close();
			
    		
			if(updateControl > 0) {
				query = "INSERT INTO dbo.provision_transfer (portfolio_no, transaction_date, process_type, amount)\r\n"
						+ "VALUES (?, ?, ?, ?)";
		        preparedStatement = connection.prepareStatement(query);
		        preparedStatement.setInt(1, portfolio_no);
		        preparedStatement.setDate(2, operationDate);
		        preparedStatement.setString(3, process_type);
		        preparedStatement.setFloat(4, amount);
		        rowsAffected = preparedStatement.executeUpdate();
		        preparedStatement.close();
			}
	        
	        connection.close();

	        return rowsAffected > 0;
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
		return false;
	}
	public List<ProvisionTransfer> dbGetProvisionTransfer() {
		List<ProvisionTransfer> transactions = new ArrayList<>();
		try {
			 Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			    Statement statement = connection.createStatement();
			    
			    String query = "SELECT * \r\n"
			    		+ "FROM dbo.provision_transfer";
			    
			    ResultSet resultSet = statement.executeQuery(query);
			    
		        while(resultSet.next()) {
		        	ProvisionTransfer transaction = new ProvisionTransfer();
		        	transaction.setPortfolio_no(resultSet.getInt("portfolio_no"));
		        	transaction.setAmount(resultSet.getFloat("amount"));
		        	transaction.setProcess_type(resultSet.getString("process_type"));
		        	transaction.setTransaction_date(resultSet.getDate("transaction_date"));
		        	transactions.add(transaction);
		        }
			    
			    resultSet.close();
			    statement.close();
			    connection.close();
	    } catch (Exception e) {
	        System.out.println("Connection Error");
	        e.printStackTrace();
	    }
		return transactions;
		
	}
	
}

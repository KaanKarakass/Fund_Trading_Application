package com.eureka.spring.bean;


import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import com.eureka.spring.model.User;
import com.eureka.spring.repository.implement.UserRepositoryImpl;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;


@Named
@SessionScoped
public class LoginPageBean implements Serializable{
	private static final long serialVersionUID = -9153247473389897352L;
	private String username;
    private String password;
    
    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	 @Autowired
	 private UserRepositoryImpl userRepository;
	 
	

    public String login() {
    	User user = userRepository.loginControl(username, password);
    	if (user.getP_name() == null) {
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credentials", "message")
            );
            return "/loginPage.xhtml";
        } else {
        	// Kullanıcı adını sakla
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", username);
            return "/homePage.xhtml?faces-redirect=true";
           // return "/homePage.xhtml";
        }
    }
}
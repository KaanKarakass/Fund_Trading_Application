package com.eureka.spring.security;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import static org.springframework.security.config.Customizer.withDefaults;
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) { 
	    String generatedPassword = "test";
	    return new InMemoryUserDetailsManager(User.withUsername("test")
	            .password(passwordEncoder.encode(generatedPassword))
	            .roles("USER").build());
	}

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher(ApplicationEventPublisher delegate) { 
        return new DefaultAuthenticationEventPublisher(delegate);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatchers((matchers) -> matchers
                .requestMatchers(new AntPathRequestMatcher("/loginPage/**"))
                .requestMatchers(new AntPathRequestMatcher("/homePage/**"))
                .requestMatchers(new AntPathRequestMatcher("/customerPage/**"))
                .requestMatchers(new AntPathRequestMatcher("/fundpricePage/**"))
                .requestMatchers(new AntPathRequestMatcher("/fundIdentificationPage/**"))
                .requestMatchers(new AntPathRequestMatcher("/fundtrading/**"))
            )
            .authorizeHttpRequests((authorize) -> authorize
                .anyRequest().hasRole("USER")
            )
            .httpBasic(withDefaults());

        return http.build();
    }

   
}
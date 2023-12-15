package com.alien.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class QuizApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizApplication.class, args);
	}

   @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
     return builder.getAuthenticationManager();
    }
    
    @Bean 
	   public PasswordEncoder passwordEncoder() { 
	       return new BCryptPasswordEncoder(); 
	   } 
}

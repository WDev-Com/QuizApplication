package com.alien.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alien.security.jwt.JWTSecureEntryPoint;
import com.alien.security.jwt.JwtAuthenticationFilter;
import com.alien.security.jwt.TokenBlacklistService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	   
	  @Autowired
	  private UserDetailsService userdetailsService;
	
	  @Autowired
      private JWTSecureEntryPoint  point;
	  
      @Autowired
      private JwtAuthenticationFilter filter;
	  
      @Autowired
      private PasswordEncoder passwordEncoder;
      
      @Autowired
      private TokenBlacklistService tokenBlacklistService;

	/*
	 Custom User Configuration 
	 @Bean
	  public UserDetailsService userDetailsService() {
	        UserDetails user1 = User.builder().username("ram")
	        		.password(passwordEncoder.encode("pass"))
	        		.roles("ADMIN")
	        		.build();
	        UserDetails user2 = User.builder().username("sham")
	        		.password(passwordEncoder.encode("pass"))
	        		.roles("NORMAL")
	        		.build();
			 
			return new InMemoryUserDetailsManager(user1,user2);
			}*/
    
		
	   @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http.csrf((c)-> c.disable())
	        .cors((c) -> c.disable())
	        .authorizeHttpRequests((authz) -> 
	        authz.requestMatchers(
	        		"/api/login",
	        		"/api/user",
	        		"/api/createuser",
	        		"/api/adduser",
	        		"/api/refreshToken","/assignment/**","/question/**","/quiz/**").permitAll()
	        .requestMatchers("/api/**").authenticated()
            .anyRequest().authenticated()
            )
	        .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
            .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(logout -> logout
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler((request, response, authentication) -> {
                        // Invalidate token on logout
                        String token = (String) filter.extractTokenFromRequest(request); 
                        tokenBlacklistService.addToBlacklist(token);
                        tokenBlacklistService.toString();
                        response.setStatus(HttpServletResponse.SC_OK);
                    })
                    .invalidateHttpSession(true)
                );
             http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
	        return http.build();
	    }
 
	   private Customizer<FormLoginConfigurer<HttpSecurity>> withDefaults() {
	        return Customizer.withDefaults(); // Use Customizer.withDefaults() for form login
	    }
	   
	   @Bean
	   public DaoAuthenticationProvider  daoAuthenticationProvider() {
		   DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		   provider.setUserDetailsService(userdetailsService);
		   provider.setPasswordEncoder(passwordEncoder);
		   return provider;
	   }
	   
	  
}

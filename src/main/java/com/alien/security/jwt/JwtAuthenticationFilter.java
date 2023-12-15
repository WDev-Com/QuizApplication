package com.alien.security.jwt;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JWTHepler jwtHelper;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    
    public String extractTokenFromRequest(HttpServletRequest request) {
        // Extract token from Authorization header
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return (String) bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header: {}", requestHeader);

        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);
            System.out.println("Token Form Filter :- "+token);
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
                
            } catch (IllegalArgumentException e) {
                logger.error("Illegal Argument while fetching the username !!", e);
            } catch (ExpiredJwtException e) {
                logger.error("Given JWT token is expired !!", e);
            } catch (MalformedJwtException e) {
                logger.error("Some changes have been done in the token. Invalid Token", e);
            } catch (SignatureException e) {
                logger.error("JWT signature does not match locally computed signature", e);
            } catch (Exception e) {
                logger.error("An unexpected error occurred while processing the token", e);
            }
        } else {
            logger.info("Invalid Header Value !!");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	System.out.println("Username from JWTAuthfilter : " + username );
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if (validateToken) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("Validation fails !!");
            }
        }
        
        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            // Token is blacklisted, reject the request
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        

        filterChain.doFilter(request, response);
    }
}


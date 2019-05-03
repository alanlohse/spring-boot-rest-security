package com.anlohse.backend.security;


import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.anlohse.backend.model.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenAuthService {
    // 10 days
    private static final long VALIDITY_TIME_MS = 10 * 24 * 60 * 60 * 1000;
    private static final String AUTH_HEADER_NAME = "x-auth-token";

    @Value("${token.secret}")
    private String secret;


    public String addAuthentication(HttpServletResponse response, TokenAuthentication authentication) {
        String token = createTokenForUser(authentication.getUser());
      	response.addHeader(AUTH_HEADER_NAME, token);
        return token;
    }

	public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null && !token.isEmpty()) {
            final User user = parseUserFromToken(token);
            if (user != null) {
                return new TokenAuthentication(user);
            }
        }
        return null;
    }

	public User parseUserFromToken(String token) {
    	try {
	        String userJSON = Jwts.parser()
	                .setSigningKey(secret)
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	        return fromJSON(userJSON);
    	} catch (JwtException e) {
    		return null;
    	}
    }

    public String createTokenForUser(User user) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .setSubject(toJSON(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private User fromJSON(final String userJSON) {
        try {
            return new ObjectMapper().readValue(userJSON, User.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String toJSON(User user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}

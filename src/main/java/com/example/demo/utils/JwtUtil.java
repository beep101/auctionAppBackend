package com.example.demo.utils;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil implements IJwtUtil{
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.validTime}")
	private long validTime;
	
	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public String getIdFromToken(String token) {
		return getClaims(token).getId();
	}
	
	public String getUsernameFromToken(String token) {
		return getClaims(token).getSubject();
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaims(token).getExpiration();
	}
	
	private boolean isTokenExpired(String token) {
		Date expiration=getExpirationDateFromToken(token);
		return expiration.after(new Date());
	}
	
	public Object getClaim(String token, String claim) {
		return getClaims(token).get(claim);
	}
	
	public boolean validateToken(String token) {
		return (isTokenExpired(token));
	}
	
	public String generateToken(User user, Map<String,Object> claims) {
		return Jwts.builder().setClaims(claims).setSubject(user.getEmail()).setId(String.valueOf(user.getId()))
				.setExpiration(new Date(System.currentTimeMillis() + validTime*1000))
				.signWith(SignatureAlgorithm.HS512,secret).compact();
	}
	
}

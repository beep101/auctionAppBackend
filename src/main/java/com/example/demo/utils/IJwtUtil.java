package com.example.demo.utils;

import java.util.Date;
import java.util.Map;

import com.example.demo.entities.User;

public interface IJwtUtil {
	
	public String getIdFromToken(String token);
	public String getUsernameFromToken(String token);
	public Date getExpirationDateFromToken(String token);
	public Object getClaim(String token, String claim);
	
	public boolean validateToken(String token);
	public String generateToken(User user, Map<String,Object> claims);
}

package com.example.demo.utils;

import javax.annotation.PostConstruct;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HashUtil implements IHashUtil{
	private BCryptPasswordEncoder passwordEncoder;
	
	@PostConstruct
	void init() {
		passwordEncoder=new BCryptPasswordEncoder();
	}
	
	@Override
	public String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	@Override
	public boolean checkPassword(String password, String hash) {
		return passwordEncoder.matches(password, hash);
	}

}

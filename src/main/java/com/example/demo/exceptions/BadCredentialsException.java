package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

public class BadCredentialsException extends AuctionAppException {
	
	private Map<String,String> errors;
	
	public BadCredentialsException() {
		super("Bad email or password");
		errors=new HashMap<>();
	}
	
	public BadCredentialsException(Map<String,String> errors) {
		super("Bad email or password");
		this.errors=errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
}

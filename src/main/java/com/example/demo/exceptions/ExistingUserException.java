package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ExistingUserException extends AuctionAppException{

	private Map<String,String> errors;
	
	public ExistingUserException() {
		super("User with used email alredy exists");
		this.errors=new HashMap<>();
	}
	
	public ExistingUserException(Map<String,String> errors) {
		super("User with used email alredy exists");
		this.errors=errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

}

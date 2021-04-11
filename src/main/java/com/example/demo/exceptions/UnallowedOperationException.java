package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

public class UnallowedOperationException extends AuctionAppException{
	private Map<String,String> errors;
	
	public UnallowedOperationException() {
		super("Operation not allowed");
		errors=new HashMap<>();
	}
	
	public UnallowedOperationException(String errors) {
		super(errors);
		this.errors=new HashMap<>();
	}
	
	public UnallowedOperationException(Map<String,String> errors) {
		super("Operation not allowed");
		this.errors=errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
}

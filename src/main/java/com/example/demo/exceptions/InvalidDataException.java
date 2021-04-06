package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

public class InvalidDataException extends AuctionAppException {
	
	private Map<String,String> errors;
	
	public InvalidDataException() {
		super("Cannot accept data");
		errors=new HashMap<>();
	}
	
	public InvalidDataException(String errors) {
		super(errors);
		this.errors=new HashMap<>();
	}
	
	public InvalidDataException(Map<String,String> errors) {
		super("Cannot accept data");
		this.errors=errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
}

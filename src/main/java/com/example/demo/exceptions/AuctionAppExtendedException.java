package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

public class AuctionAppExtendedException extends AuctionAppException{

	private Map<String,String> errors;
	
	public AuctionAppExtendedException() {
		super();
		errors=new HashMap<>();
	}
	
	public AuctionAppExtendedException(String error) {
		super(error);
		errors=new HashMap<>();
	}
	
	public AuctionAppExtendedException(String error,Map<String,String> errors) {
		super(error);
		this.errors=errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
}

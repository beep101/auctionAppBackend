package com.example.demo.exceptions;

import java.util.Map;

public class BadCredentialsException extends AuctionAppExtendedException {
	
	public BadCredentialsException() {
		super("Bad email or password");
	}
	
	public BadCredentialsException(Map<String,String> errors) {
		super("Bad email or password",errors);
	}
}

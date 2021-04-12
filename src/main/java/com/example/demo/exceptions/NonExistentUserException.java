package com.example.demo.exceptions;

import java.util.Map;

public class NonExistentUserException extends AuctionAppExtendedException{

	public NonExistentUserException() {
		super("User does not exists");
	}
	
	public NonExistentUserException(Map<String,String> errors) {
		super("User does not exists",errors);
	}
	
}

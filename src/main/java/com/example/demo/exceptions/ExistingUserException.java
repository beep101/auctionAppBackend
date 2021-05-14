package com.example.demo.exceptions;

import java.util.Map;

public class ExistingUserException extends AuctionAppExtendedException{

	public ExistingUserException() {
		super("User with used email alredy exists");
	}
	
	public ExistingUserException(Map<String,String> errors) {
		super("User with used email alredy exists",errors);
	}

}

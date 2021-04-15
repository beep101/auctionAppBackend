package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

public class UnallowedOperationException extends AuctionAppExtendedException{
	
	public UnallowedOperationException() {
		super("Operation not allowed");
	}
	
	public UnallowedOperationException(String errors) {
		super(errors);
	}
	
	public UnallowedOperationException(Map<String,String> errors) {
		super("Operation not allowed",errors);
	}
	
}

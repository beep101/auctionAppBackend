package com.example.demo.exceptions;

import java.util.Map;

public class InvalidDataException extends AuctionAppExtendedException {

	public InvalidDataException() {
		super("Cannot accept data");
	}
	
	public InvalidDataException(String errors) {
		super(errors);
	}
	
	public InvalidDataException(Map<String,String> errors) {
		super("Cannot accept data",errors);
	}
	
}

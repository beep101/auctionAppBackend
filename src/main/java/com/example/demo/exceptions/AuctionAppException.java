package com.example.demo.exceptions;

public class AuctionAppException extends Exception{
	public AuctionAppException() {
		super();
	}
	
	public AuctionAppException(String error) {
		super(error);
	}
	
}

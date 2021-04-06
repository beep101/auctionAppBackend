package com.example.demo.exceptions;

public class InvalidTokenException extends AuctionAppException{
	public InvalidTokenException() {
		super("Provided token is invalid");
	}
}

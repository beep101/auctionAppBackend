package com.example.demo.exceptions;

public class UnauthenticatedException extends AuctionAppException{
	public UnauthenticatedException() {
		super("Authentication required");
	}
}

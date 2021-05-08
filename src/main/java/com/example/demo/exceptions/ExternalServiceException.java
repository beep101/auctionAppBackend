package com.example.demo.exceptions;

public class ExternalServiceException extends AuctionAppException{
	public ExternalServiceException() {
		super("Problem with external services");
	}
}

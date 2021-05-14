package com.example.demo.exceptions;

public class ExternalServiceError extends AuctionAppException{
	public ExternalServiceError() {
		super("Error caused by external service");
	}
}

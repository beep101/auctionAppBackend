package com.example.demo.exceptions;

public class BadInitializatinException extends AuctionAppException{
	public BadInitializatinException() {
		super("Cannot initialize components");
	}
}

package com.example.demo.exceptions;

public class InsertFailedException extends AuctionAppException{
	public InsertFailedException() {
		super("Cannot upload data");
	}
}

package com.example.demo.exceptions;

public class ImageHashException extends AuctionAppException{
	public ImageHashException() {
		super("Error on hashing uploaded image");
	}

}

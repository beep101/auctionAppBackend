package com.example.demo.exceptions;

public class ImageFetchException extends AuctionAppException{
	public ImageFetchException() {
		super("Error while fetching image");
	}

}

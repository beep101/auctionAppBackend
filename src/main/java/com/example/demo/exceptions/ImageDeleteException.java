package com.example.demo.exceptions;

public class ImageDeleteException extends AuctionAppException{
	public ImageDeleteException() {
		super("Error on deleting image");
	}
}

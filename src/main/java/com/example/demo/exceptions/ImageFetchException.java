package com.example.demo.exceptions;

public class ImageFetchException extends RuntimeException{
	public ImageFetchException() {
		super("Error while fetching image");
	}

}

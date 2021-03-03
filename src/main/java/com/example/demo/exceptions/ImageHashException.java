package com.example.demo.exceptions;

public class ImageHashException extends RuntimeException{
	public ImageHashException() {
		super("Error on hashing uploaded image");
	}

}

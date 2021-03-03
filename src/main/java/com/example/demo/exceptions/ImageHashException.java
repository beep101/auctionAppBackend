package com.example.demo.exceptions;

public class ImageHashException extends Exception{
	public ImageHashException() {
		super("Error on hashing uploaded image");
	}

}

package com.example.demo.exceptions;

public class ImageDeleteException extends RuntimeException{
	public ImageDeleteException() {
		super("Error on deleting image");
	}
}

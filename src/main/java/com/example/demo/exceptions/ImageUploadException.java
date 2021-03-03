package com.example.demo.exceptions;

public class ImageUploadException extends RuntimeException{
	public ImageUploadException() {
		super("Error on upoading new image");
	}
}

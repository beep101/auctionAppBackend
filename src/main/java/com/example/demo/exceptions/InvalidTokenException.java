package com.example.demo.exceptions;

public class InvalidTokenException extends Exception{
	public InvalidTokenException() {
		super("Provided token is invalid");
	}
}

package com.example.demo.exceptions;

public class UnauthenticatedException extends Exception{
	public UnauthenticatedException() {
		super("Authentication required");
	}
}

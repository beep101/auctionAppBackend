package com.example.demo.exceptions;


public class BadCredentialsException extends Exception {
	public BadCredentialsException() {
		super("Bad email or password");
	}
}

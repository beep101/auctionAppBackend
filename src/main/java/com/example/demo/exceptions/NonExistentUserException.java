package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NonExistentUserException extends Exception{
	
	private Map<String,String> errors;
	
	public NonExistentUserException() {
		super("User does not exists");
		errors=new HashMap<>();
	}
	
	public NonExistentUserException(Map<String,String> errors) {
		super("User does not exists");
		this.errors=errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	
}

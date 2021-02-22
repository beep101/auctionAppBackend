package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NonExistentUserException extends RuntimeException{
	public NonExistentUserException() {
		super("User does not exists");
	}
}

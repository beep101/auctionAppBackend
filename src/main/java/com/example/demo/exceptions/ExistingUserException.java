package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExistingUserException extends RuntimeException{
	public ExistingUserException() {
		super("User with used email alredy exists");
	}

}

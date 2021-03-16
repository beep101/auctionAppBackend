package com.example.demo.exceptions;

public class InvalidDataException extends Exception {
	public InvalidDataException() {
		super("Cannot accept data");
	}
	
	public InvalidDataException(String errors) {
		super(errors);
	}
}

package com.example.demo.exceptions;

public class InvalidDataException extends RuntimeException {
	public InvalidDataException() {
		super("Cannot accept data");
	}
}

package com.example.demo.exceptions;

public class InsertFailedException extends Exception{
	public InsertFailedException() {
		super("Cannot upload data");
	}
}

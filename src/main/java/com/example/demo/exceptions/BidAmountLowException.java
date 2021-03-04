package com.example.demo.exceptions;

public class BidAmountLowException extends Exception{
	public BidAmountLowException() {
		super("Bid amount is lower than current minimum");
	}
}

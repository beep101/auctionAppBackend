package com.example.demo.models;


public class PayMethodModel {
	
	private int id;
	private String onCardName;
	private String cardNumber;
	private String cvccw;
	private int expMonth;
	private int expYear;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOnCardName() {
		return onCardName;
	}
	public void setOnCardName(String onCardName) {
		this.onCardName = onCardName;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCvccw() {
		return cvccw;
	}
	public void setCvccw(String cvccw) {
		this.cvccw = cvccw;
	}
	public int getExpMonth() {
		return expMonth;
	}
	public void setExpMonth(int expMonth) {
		this.expMonth = expMonth;
	}
	public int getExpYear() {
		return expYear;
	}
	public void setExpYear(int expYear) {
		this.expYear = expYear;
	}
}

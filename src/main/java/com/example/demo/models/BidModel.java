package com.example.demo.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.demo.entities.Item;
import com.example.demo.entities.User;

public class BidModel {
	private int id;
	private BigDecimal amount;
	private int attempt;
	private Timestamp time;
	private UserModel bidder;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getAttempt() {
		return attempt;
	}
	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public UserModel getBidder() {
		return bidder;
	}
	public void setBidder(UserModel bidder) {
		this.bidder = bidder;
	}
}

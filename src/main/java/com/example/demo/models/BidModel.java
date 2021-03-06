package com.example.demo.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BidModel {
	private int id;
	private BigDecimal amount;
	private int attempt;
	private Timestamp time;
	private UserModel bidder;
	private ItemModel item;
	
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
	public ItemModel getItem() {
		return item;
	}
	public void setItem(ItemModel item) {
		this.item = item;
	}
}

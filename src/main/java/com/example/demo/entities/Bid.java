package com.example.demo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the "bids" database table.
 * 
 */
@Entity
@Table(name="\"bids\"")
@NamedQuery(name="Bid.findAll", query="SELECT b FROM Bid b")
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="\"id\"")
	private int id;

	@Column(name="\"amount\"")
	private BigDecimal amount;

	@Column(name="\"attempt\"")
	private int attempt;

	@Column(name="\"time\"")
	private Timestamp time;

	//bi-directional many-to-one association to Item
	@ManyToOne
	@JoinColumn(name="\"item\"")
	private Item item;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="\"bidder\"")
	private User bidder;

	public Bid() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getAttempt() {
		return this.attempt;
	}

	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

	public User getBidder() {
		return this.bidder;
	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}
package com.example.demo.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.example.demo.models.BidModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;


/**
 * The persistent class for the "bids" database table.
 * 
 */
@Entity
@Table(name="bids")
@NamedQuery(name="Bid.findAll", query="SELECT b FROM Bid b")
public class Bid implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final MathContext roundiongContext=new MathContext(3);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="amount")
	private BigDecimal amount;

	@Column(name="attempt")
	private int attempt;

	@Column(name="time")
	private Timestamp time;

	//bi-directional many-to-one association to Item
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name="item")
	private Item item;

	//bi-directional many-to-one association to User
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name="bidder")
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
	
	public BidModel toModel() {
		BidModel model=new BidModel();
		
		model.setId(this.getId());
		model.setAmount(this.getAmount());
		model.setBidder(this.getBidder().toModel());
		model.setTime(this.getTime());
		
		return model;
	}
	
	public static Bid fromModel(BidModel model) {
		Bid entity=new Bid();
		
		entity.setId(model.getId());
		entity.setAmount(model.getAmount().round(roundiongContext));
		User bidder=new User();
		bidder.setId(model.getBidder().getId());
		entity.setBidder(bidder);
		entity.setTime(model.getTime());
		Item item=new Item();
		item.setId(model.getItem().getId());
		entity.setItem(item);
		
		return entity;
	}

}
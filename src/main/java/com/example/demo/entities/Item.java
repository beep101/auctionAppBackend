package com.example.demo.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the "items" database table.
 * 
 */
@Entity
@Table(name="\"items\"")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="\"id\"")
	private int id;

	@Column(name="\"description\"")
	private String description;

	@Column(name="\"endtime\"")
	private Timestamp endtime;

	@Column(name="\"name\"")
	private String name;

	@Column(name="\"startingprice\"")
	private BigDecimal startingprice;

	@Column(name="\"starttime\"")
	private Timestamp starttime;

	//bi-directional many-to-one association to Bid
	@OneToMany(mappedBy="item")
	private List<Bid> bids;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="\"seller\"")
	private User seller;

	public Item() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getSeller() {
		return this.seller;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public BigDecimal getStartingprice() {
		return this.startingprice;
	}

	public void setStartingprice(BigDecimal startingprice) {
		this.startingprice = startingprice;
	}

	public Timestamp getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public List<Bid> getBids() {
		return this.bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public Bid addBid(Bid bid) {
		getBids().add(bid);
		bid.setItem(this);

		return bid;
	}

	public Bid removeBid(Bid bid) {
		getBids().remove(bid);
		bid.setItem(null);

		return bid;
	}

}
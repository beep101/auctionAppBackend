package com.example.demo.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;

public class ItemModel {
	private int id;
	private String name;
	private String description;
	private BigDecimal startingprice;
	private Timestamp starttime;
	private Timestamp endtime;
	private Boolean sold;
	private List<BidModel> bids;
	private UserModel seller;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getStartingprice() {
		return startingprice;
	}

	public void setStartingprice(BigDecimal startingprice) {
		this.startingprice = startingprice;
	}

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public Timestamp getEndtime() {
		return endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}

	public Boolean getSold() {
		return sold;
	}

	public void setSold(Boolean sold) {
		this.sold = sold;
	}

	public List<BidModel> getBids() {
		return bids;
	}

	public void setBids(List<BidModel> bids) {
		this.bids = bids;
	}

	public UserModel getSeller() {
		return seller;
	}

	public void setSeller(UserModel seller) {
		this.seller = seller;
	}
}

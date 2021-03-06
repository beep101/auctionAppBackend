package com.example.demo.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class ItemModel implements ModelWithImages{
	private int id;
	private String name;
	private String description;
	private BigDecimal startingprice;
	private Timestamp starttime;
	private Timestamp endtime;
	private Boolean sold;
	private Boolean paid;
	private List<BidModel> bids;
	private UserModel seller;
	private UserModel winner;
	private List<String> images;
	
	private AddressModel address;
	private SubcategoryModel subcategory;
	private List<byte[]> imageFiles;
	
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

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public AddressModel getAddress() {
		return address;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
	}

	public List<byte[]> getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(List<byte[]> imageFiles) {
		this.imageFiles = imageFiles;
	}

	public SubcategoryModel getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(SubcategoryModel subcategory) {
		this.subcategory = subcategory;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public UserModel getWinner() {
		return winner;
	}

	public void setWinner(UserModel winner) {
		this.winner = winner;
	}
	}

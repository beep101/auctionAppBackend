package com.example.demo.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.example.demo.models.ItemModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The persistent class for the "items" database table.
 * 
 */
@Entity
@Table(name="items")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="description")
	private String description;

	@Column(name="endtime")
	private Timestamp endtime;

	@Column(name="name")
	private String name;

	@Column(name="startingprice")
	private BigDecimal startingprice;

	@Column(name="starttime")
	private Timestamp starttime;
	
	@Column(name="sold")
	private Boolean sold;
	
	@Column(name="paid")
	private Boolean paid;
	
	//bi-directional many-to-one association to Bid
	@OneToMany(mappedBy="item")
	private List<Bid> bids;
	
	@OneToMany(mappedBy="item")
	private List<Order> orders;
	
	//bi-directional many-to-one association to User
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="seller")
	private User seller;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="winner")
	private User winner;
	
	@ManyToOne
	@JoinColumn(name="subcategory")
	private Subcategory subcategory;
	
	@ManyToOne
	@JoinColumn(name="address")
	private Address address;

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

	public Category getCategory() {
		return subcategory.getCategory();
	}

	public void setCategory(Category category) {
		this.subcategory.setCategory(category);;
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

	public Boolean getSold() {
		return sold;
	}

	public void setSold(Boolean sold) {
		this.sold = sold;
	}
	
	public Subcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(Subcategory subcategory) {
		this.subcategory = subcategory;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public User getWinner() {
		return winner;
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

	public ItemModel toModel() {
		ItemModel model=new ItemModel();
		model.setId(this.getId());
		model.setName(this.getName());
		model.setDescription(this.getDescription());
		model.setStartingprice(this.getStartingprice());
		model.setStarttime(this.getStarttime());
		model.setEndtime(this.getEndtime());
		model.setSold(this.getSold());
		model.setSeller(this.seller.toModel());
		model.setSubcategory(this.getSubcategory().toModel());
		model.setPaid(this.getPaid());
		
		if(this.address!=null) {
			model.setAddress(this.getAddress().toModel());
		}
		if(this.winner!=null) {
			model.setWinner(this.getWinner().toModel());
		}
		if(this.bids!=null)
			model.setBids(this.getBids().stream().map(x->x.toModel()).collect(Collectors.toList()));
		return model;
	}
	
	public void populate(ItemModel model) {
		this.setId(model.getId());
		this.setName(model.getName());
		this.setDescription(model.getDescription());
		this.setStartingprice(model.getStartingprice());
		this.setStarttime(model.getStarttime());
		this.setEndtime(model.getEndtime());
		this.setSold(model.getSold());
		this.setPaid(model.getPaid());
		if(model.getSubcategory()!=null) {
			Subcategory subcategory=new Subcategory();
			subcategory.setId(model.getSubcategory().getId());
			this.setSubcategory(subcategory);
		}
		
		if(model.getSeller()!=null) {
			User seller=new User();
			seller.setId(model.getSeller().getId());
			this.setSeller(seller);
		}
		
		if(model.getAddress()!=null) {
			Address address=new Address();
			address.populate(model.getAddress());
			this.setAddress(address);
		}
	}
	

}
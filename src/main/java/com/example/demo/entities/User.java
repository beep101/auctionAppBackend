package com.example.demo.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import com.example.demo.models.UserModel;

import java.util.List;


/**
 * The persistent class for the "users" database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="email")
	private String email;

	@Column(name="name")
	private String name;

	@Column(name="surname")
	private String surname;
	
	@Column(name="passwd")
	private String passwd;
	
	@Column(name="forgot_password_token")
	private String forgotPasswordToken;
	
	@Column(name="gender")
	private String gender;

	@Column(name="birthday")
	private Date birthday;
	
	@Column(name="forgot_password_token_end_time")
	private Timestamp forgotPasswordTokenEndTime;
	
	//bi-directional many-to-one association to Bid
	@OneToMany(mappedBy="bidder")
	private List<Bid> bids;

	//bi-directional many-to-one association to Item
	@OneToMany(mappedBy="seller")
	private List<Item> items;
	
	@ManyToOne
	@JoinColumn(name="address")
	private Address address;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getForgotPasswordToken() {
		return forgotPasswordToken;
	}

	public void setForgotPasswordToken(String fpToken) {
		this.forgotPasswordToken = fpToken;
	}

	public List<Bid> getBids() {
		return this.bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Timestamp getForgotPasswordTokenEndTime() {
		return forgotPasswordTokenEndTime;
	}

	public void setForgotPasswordTokenEndTime(Timestamp fpTokenEndtime) {
		this.forgotPasswordTokenEndTime = fpTokenEndtime;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Bid addBid(Bid bid) {
		getBids().add(bid);
		bid.setBidder(this);

		return bid;
	}

	public Bid removeBid(Bid bid) {
		getBids().remove(bid);
		bid.setBidder(null);

		return bid;
	}

	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Item addItem(Item item) {
		getItems().add(item);
		item.setSeller(this);

		return item;
	}

	public Item removeItem(Item item) {
		getItems().remove(item);
		item.setSeller(null);

		return item;
	}
	
	public UserModel toModel() {
		UserModel model=new UserModel();
		model.setId(this.getId());
		model.setFirstName(this.getName());;
		model.setLastName(this.getSurname());
		model.setEmail(this.getEmail());
		return model;
	}

}
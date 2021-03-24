package com.example.demo.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the "addresses" database table.
 * 
 */
@Entity
@Table(name="addresses")
@NamedQuery(name="Address.findAll", query="SELECT a FROM Address a")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="addres")
	private String address;

	@Column(name="city")
	private String city;

	@Column(name="country")
	private String country;

	@Column(name="phone")
	private String phone;

	@Column(name="zip")
	private String zip;
	
	public Address() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String addres) {
		this.address = addres;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}
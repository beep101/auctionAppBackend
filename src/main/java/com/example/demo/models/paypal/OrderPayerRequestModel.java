package com.example.demo.models.paypal;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class OrderPayerRequestModel {
	private String email;
	private Name name;
	private Address address;
	
	@JsonGetter("email_address")
	public String getEmail() {
		return email;
	}
	@JsonSetter("email_address")
	public void setEmail(String email) {
		this.email = email;
	}
		
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

	public static class Name{
		private String name;
		private String surname;
		
		public Name() {
			super();
		}

		public Name(String name, String surname) {
			this.name=name;
			this.surname=surname;
		}
		
		@JsonGetter("given_name")
		public String getName() {
			return name;
		}
		@JsonSetter("given_name")
		public void setName(String name) {
			this.name = name;
		}
		@JsonGetter("surname")
		public String getSurname() {
			return surname;
		}
		@JsonSetter("surname")
		public void setSurname(String surname) {
			this.surname = surname;
		}
	}

	public static class Address{
		private String address;
		private String city;
		private String country;
		private String zip;
		
		public Address() {
			super();
		}
		
		public Address(String address, String city,String country,String zip) {
			this.address=address;
			this.city=city;
			this.country=country;
			this.zip=zip;
		}

		@JsonGetter("address_line_1")
		public String getAddress() {
			return address;
		}
		@JsonSetter("address_line_1")
		public void setAddress(String address) {
			this.address = address;
		}
		@JsonGetter("admin_area_2")
		public String getCity() {
			return city;
		}
		@JsonSetter("admin_area_2")
		public void setCity(String city) {
			this.city = city;
		}
		@JsonGetter("country_code")
		public String getCounrty() {
			return country;
		}
		@JsonSetter("country_code")
		public void setCountry(String county) {
			this.country = county;
		}
		@JsonGetter("postal_code")
		public String getZip() {
			return zip;
		}
		@JsonSetter("postal_code")
		public void setZip(String zip) {
			this.zip = zip;
		}
	}
	
	public static class Phone{
		private String phone;
		
		public Phone() {
			super();
		}
		
		public Phone(String phone) {
			this.phone=phone;
		}

		@JsonGetter("phone_number")
		public String getPhone() {
			return phone;
		}
		@JsonSetter("phone_number")
		public void setPhone(String phone) {
			this.phone = phone;
		}
	}
}


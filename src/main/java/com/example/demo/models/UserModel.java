package com.example.demo.models;

import java.sql.Timestamp;

import com.example.demo.utils.Gender;

public class UserModel {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String jwt;
	private String forgotPasswordToken;
	private Gender gender;
	private Timestamp birthday;
	private AddressModel address;
	private PayMethodModel payMethod;
	
	public UserModel() {
		super();
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getForgotPasswordToken() {
		return forgotPasswordToken;
	}

	public void setForgotPasswordToken(String fpToken) {
		this.forgotPasswordToken = fpToken;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public AddressModel getAddress() {
		return address;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
	}

	public PayMethodModel getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(PayMethodModel payMethod) {
		this.payMethod = payMethod;
	}
}

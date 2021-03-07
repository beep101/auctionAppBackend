package com.example.demo.models;

public class UserModel {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String jwt;
	private String fpToken;
	
	public UserModel() {
		super();
	}
	
	public UserModel(String firstName, String lastName, String email, String password, String jwt) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.jwt = jwt;
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

	public String getFpToken() {
		return fpToken;
	}

	public void setFpToken(String fpToken) {
		this.fpToken = fpToken;
	}
	
}

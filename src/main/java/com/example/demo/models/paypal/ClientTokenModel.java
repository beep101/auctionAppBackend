package com.example.demo.models.paypal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientTokenModel {
	private String client_token;
	private String client_id;
	private String bncode;
	private int expires_in;
	
	public String getClient_token() {
		return client_token;
	}
	public void setClient_token(String client_token) {
		this.client_token = client_token;
	}
	
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getBncode() {
		return bncode;
	}
	public void setBncode(String bncode) {
		this.bncode = bncode;
	}
}

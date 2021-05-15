package com.example.demo.models.paypal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientTokenModel {
	@JsonProperty("client_token")
	private String clientToken;
	@JsonProperty("client_id")
	private String clientId;
	@JsonProperty("client_merchant_id")
	private String clientMerchantId;
	private String bncode;
	@JsonProperty("expires_in")
	private int expiresIn;
	
	public String getClientToken() {
		return clientToken;
	}
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientMerchantId() {
		return clientMerchantId;
	}
	public void setClientMerchantId(String clientMerchantId) {
		this.clientMerchantId = clientMerchantId;
	}
	public String getBncode() {
		return bncode;
	}
	public void setBncode(String bncode) {
		this.bncode = bncode;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}

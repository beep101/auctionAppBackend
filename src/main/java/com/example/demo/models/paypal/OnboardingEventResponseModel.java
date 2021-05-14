package com.example.demo.models.paypal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OnboardingEventResponseModel {
	private String partner_client_id;
	private String merchant_id;
	private String tracking_id;
	
	public String getPartner_client_id() {
		return partner_client_id;
	}
	public void setPartner_client_id(String partner_client_id) {
		this.partner_client_id = partner_client_id;
	}
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getTracking_id() {
		return tracking_id;
	}
	public void setTracking_id(String tracking_id) {
		this.tracking_id = tracking_id;
	}
	
}

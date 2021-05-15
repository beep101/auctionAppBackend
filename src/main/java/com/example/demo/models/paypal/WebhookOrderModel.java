package com.example.demo.models.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookOrderModel {
	@JsonProperty("event_type")
	private String eventType;
	private PaymentEventResponseModel resource;
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public PaymentEventResponseModel getResource() {
		return resource;
	}
	public void setResource(PaymentEventResponseModel resource) {
		this.resource = resource;
	}
	
}

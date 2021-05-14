package com.example.demo.models.paypal;

public class WebhookOrderModel {
	private String event_type;
	private PaymentEventResponseModel resource;
	
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public PaymentEventResponseModel getResource() {
		return resource;
	}
	public void setResource(PaymentEventResponseModel resource) {
		this.resource = resource;
	}
	
}

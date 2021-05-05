package com.example.demo.models.paypal;

public class WebhookOnboardingModel {
	private String event_type;
	private OnboardingEventResponseModel resource;

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public OnboardingEventResponseModel getResource() {
		return resource;
	}

	public void setResource(OnboardingEventResponseModel resource) {
		this.resource = resource;
	}
	
}

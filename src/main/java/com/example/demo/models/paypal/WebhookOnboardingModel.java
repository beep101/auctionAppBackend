package com.example.demo.models.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookOnboardingModel {
	@JsonProperty("event_type")
	private String eventType;
	private OnboardingEventResponseModel resource;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public OnboardingEventResponseModel getResource() {
		return resource;
	}

	public void setResource(OnboardingEventResponseModel resource) {
		this.resource = resource;
	}
	
}

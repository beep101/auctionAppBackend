package com.example.demo.models.paypal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OnboardingUrlResponseModel {
	private List<LinkResponseModel> links;

	public List<LinkResponseModel> getLinks() {
		return links;
	}

	public void setLinks(List<LinkResponseModel> links) {
		this.links = links;
	}
}

package com.example.demo.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;
import java.util.Optional;

import com.example.demo.entities.User;
import com.example.demo.exceptions.ExternalServiceError;
import com.example.demo.models.paypal.AccessTokenResponseModel;
import com.example.demo.models.paypal.OnboardingUrlModel;
import com.example.demo.models.paypal.OnboardingUrlResponseModel;
import com.example.demo.models.paypal.WebhookOnboardingModel;
import com.example.demo.repositories.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPayPalTransactionService {
	private static final String TOKEN_PATH="/v1/oauth2/token";
	private static final String ONBOARDING_REQ_PATH="/v2/customer/partner-referrals";
	private static final String ONBOARDING_REQ_BODY="{\r\n"
			+ "    \"tracking_id\": \"%d\",\r\n"
			+ "    \"operations\": [\r\n"
			+ "      {\r\n"
			+ "        \"operation\": \"API_INTEGRATION\",\r\n"
			+ "        \"api_integration_preference\": {\r\n"
			+ "          \"rest_api_integration\": {\r\n"
			+ "            \"integration_method\": \"PAYPAL\",\r\n"
			+ "            \"integration_type\": \"THIRD_PARTY\",\r\n"
			+ "            \"third_party_details\": {\r\n"
			+ "              \"features\": [\r\n"
			+ "                \"PAYMENT\",\r\n"
			+ "                \"REFUND\"\r\n"
			+ "             ]\r\n"
			+ "            }\r\n"
			+ "          }\r\n"
			+ "        }\r\n"
			+ "      }\r\n"
			+ "    ],\r\n"
			+ "    \"products\": [\r\n"
			+ "      \"EXPRESS_CHECKOUT\"\r\n"
			+ "    ],\r\n"
			+ "    \"legal_consents\": [\r\n"
			+ "      {\r\n"
			+ "        \"type\": \"SHARE_DATA_CONSENT\",\r\n"
			+ "        \"granted\": true\r\n"
			+ "      }\r\n"
			+ "    ]\r\n"
			+ "}";
	
	private UsersRepository usersRepo;
	
	private String id;
	private String secret;
	private String baseUrl;
	
	private HttpClient httpClient;
	private ObjectMapper objectMapper;
	
	private String key;
	
	public DefaultPayPalTransactionService(String id,String secret, String baseUrl, UsersRepository usersRepo) {
		this.id=id;
		this.secret=secret;
		this.baseUrl=baseUrl;
		
	    this.httpClient = HttpClient.newHttpClient();
	    this.objectMapper=new ObjectMapper();
	    
	    this.usersRepo=usersRepo;
	}

	public int fetchKey() {
		Builder requestBuilder=HttpRequest.newBuilder();
		requestBuilder.uri(URI.create(baseUrl+TOKEN_PATH))
			.header("Authorization", "Basic "+Base64.getEncoder().encodeToString((id+":"+secret).getBytes()))
			.header("Accept", "application/json")
			.header("Accept-Language", "en_US")
			.header("Content-Type", "application/x-www-form-urlencoded");
		requestBuilder.POST(BodyPublishers.ofString("grant_type=client_credentials"));
		HttpResponse<String> response;
		try {
			response = this.httpClient.send(requestBuilder.build(),BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			return 1;
		}
		if(response.statusCode()>=300) {
			return 1;
		}
		AccessTokenResponseModel data;
		try {
			data = objectMapper.readValue(response.body(), AccessTokenResponseModel.class);
		} catch (JsonProcessingException e) {
			return 1;
		}
		this.key=data.getAccess_token();
		return data.getExpires_in();
	}
	
	public OnboardingUrlModel getOnboardingUrl(User principal) throws ExternalServiceError {
		OnboardingUrlModel model=new OnboardingUrlModel();
		
		Builder requestBuilder=HttpRequest.newBuilder();
		requestBuilder.uri(URI.create(baseUrl+ONBOARDING_REQ_PATH))
			.header("Authorization", "Bearer "+this.key)
			.header("Content-Type", "application/json");
		requestBuilder.POST(BodyPublishers.ofString(String.format(ONBOARDING_REQ_BODY, principal.getId())));
		HttpResponse<String> response;
		try {
			response = this.httpClient.send(requestBuilder.build(),BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			throw new ExternalServiceError();
		}
		OnboardingUrlResponseModel data;
		try {
			data = objectMapper.readValue(response.body(), OnboardingUrlResponseModel.class);
		} catch (JsonProcessingException e) {
			throw new ExternalServiceError();
		}
		model.setUrl(data.getLinks().get(1).getHref());
		return model;
	}
	
	public void onboardingEvent(WebhookOnboardingModel data) {
		switch(data.getEvent_type()) {
			case "MERCHANT.ONBOARDING.COMPLETED":
				onboardUser(data);
				break;
			case "CUSTOMER.MERCHANT-INTEGRATION.SELLER-ALREADY-INTEGRATED":
				userAlreadyOnboard(data);
				break;
		}
	}

	private void onboardUser(WebhookOnboardingModel data) {
		int userId=0;
		try {
			userId=Integer.parseInt(data.getResource().getTracking_id());
		}catch(NumberFormatException e) {
			return;
		}
		Optional<User> userOpt=usersRepo.findById(userId);
		if(userOpt.isPresent()) {
			User user=userOpt.get();
			user.setMerchantId(data.getResource().getMerchant_id());
			usersRepo.save(user);
		}
	}
	
	private void userAlreadyOnboard(WebhookOnboardingModel data) {
		int userId=0;
		try {
			userId=Integer.parseInt(data.getResource().getTracking_id());
		}catch(NumberFormatException e) {
			return;
		}
		Optional<User> userOpt=usersRepo.findById(userId);
		if(userOpt.isPresent()) {
			User user=userOpt.get();
			if(user.getMerchantId().isBlank()) {
				user.setMerchantId(data.getResource().getMerchant_id());
				usersRepo.save(user);
			}
		}
	}
}

package com.example.demo.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.example.demo.entities.Address;
import com.example.demo.entities.Item;
import com.example.demo.entities.Order;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.ExternalServiceError;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.HttpResponseModel;
import com.example.demo.models.paypal.AccessTokenResponseModel;
import com.example.demo.models.paypal.ClientTokenModel;
import com.example.demo.models.paypal.OnboardingUrlModel;
import com.example.demo.models.paypal.OnboardingUrlResponseModel;
import com.example.demo.models.paypal.OrderModel;
import com.example.demo.models.paypal.OrderPayerRequestModel;
import com.example.demo.models.paypal.OrderRequestModel;
import com.example.demo.models.paypal.OrderResponseModel;
import com.example.demo.models.paypal.WebhookOnboardingModel;
import com.example.demo.models.paypal.WebhookOrderModel;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.OrdersRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.utils.CountryCodeUtil;
import com.example.demo.utils.HttpClientAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPayPalTransactionService {
	public static final String ONBOARDING_REQUEST_BODY_FILE_LOCATION="/onboarding_request_body.json";
	
	private static final String TOKEN_PATH="/v1/oauth2/token";
	private static final String ONBOARDING_REQ_PATH="/v2/customer/partner-referrals";
	private static final String CLIENT_TOKEN_PATH="/v1/identity/generate-token";
	private static final String ORDER_PATH="/v2/checkout/orders";

	private final static int ONBOARDING_URL_INDEX=1;
	private final static int DEFAULT_RETRY_DELAY=1;
	
	private UsersRepository usersRepo;
	private ItemsRepository itemsRepo;
	private OrdersRepository ordersRepo;
	
	private String id;
	private String secret;
	private String bncode;
	private String baseUrl;
	private String merchantId;
	
	private HttpClientAdapter httpClient;
	private CountryCodeUtil ccUtil;
	private ObjectMapper objectMapper;
	
	private String key;
	private String onboardingRequestBody;
	
	public DefaultPayPalTransactionService(String id, String secret, String bncode, String merchantId, String baseUrl,OrdersRepository ordersRepo, UsersRepository usersRepo,ItemsRepository itemsRepo, CountryCodeUtil ccUtil,HttpClientAdapter httpClient){
		this.id=id;
		this.secret=secret;
		this.bncode=bncode;
		this.baseUrl=baseUrl;
		this.merchantId=merchantId;
		
	    this.httpClient = httpClient;
	    this.ccUtil=ccUtil;
	    this.objectMapper=new ObjectMapper();
	    
	    this.usersRepo=usersRepo;
	    this.itemsRepo=itemsRepo;
	    this.ordersRepo=ordersRepo;
	    
	    //InputStream inputStream = getClass().getResourceAsStream(ONBOARDING_REQUEST_BODY_FILE_LOCATION);
	    //BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		//onboardingRequestBody=reader.lines().collect(Collectors.joining(System.lineSeparator()));
	    onboardingRequestBody="{\r\n"
	    		+ "	\"tracking_id\": \"%d\",\r\n"
	    		+ "	\"operations\": [\r\n"
	    		+ "		{\r\n"
	    		+ "			\"operation\": \"API_INTEGRATION\",\r\n"
	    		+ "			\"api_integration_preference\": {\r\n"
	    		+ "				\"rest_api_integration\": {\r\n"
	    		+ "					\"integration_method\": \"PAYPAL\",\r\n"
	    		+ "					\"integration_type\": \"THIRD_PARTY\",\r\n"
	    		+ "					\"third_party_details\": {\r\n"
	    		+ "						\"features\": [\"PAYMENT\",\"REFUND\"]\r\n"
	    		+ "					}\r\n"
	    		+ "				}\r\n"
	    		+ "			}\r\n"
	    		+ "		}\r\n"
	    		+ "	],\r\n"
	    		+ "	\"products\": [\r\n"
	    		+ "		\"EXPRESS_CHECKOUT\"\r\n"
	    		+ "	],\r\n"
	    		+ "	\"legal_consents\": [\r\n"
	    		+ "		{\r\n"
	    		+ "			\"type\": \"SHARE_DATA_CONSENT\",\r\n"
	    		+ "			\"granted\": true\r\n"
	    		+ "		}\r\n"
	    		+ "	]\r\n"
	    		+ "}";
	}

	public int refreshAccessKey() {
		Map<String,String> headers=new HashMap<>();
		headers.put("Authorization","Basic "+Base64.getEncoder().encodeToString((id+":"+secret).getBytes()));
		headers.put("Accept", "application/json");
		headers.put("Accept-Language", "en_US");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		byte[] reqBody="grant_type=client_credentials".getBytes();
		
		HttpResponseModel response;
		try {
			response = httpClient.sendHttpRequest(HttpMethod.POST, baseUrl+TOKEN_PATH, headers, reqBody);
		} catch (AuctionAppException e1) {
			return DEFAULT_RETRY_DELAY;
		}


		if(response.getStatusCode()>=300) {
			return DEFAULT_RETRY_DELAY;
		}
		
		AccessTokenResponseModel data;
		try {
			data = objectMapper.readValue(response.getBody(), AccessTokenResponseModel.class);
		} catch (JsonProcessingException e) {
			return DEFAULT_RETRY_DELAY;
		}
		this.key=data.getAccessToken();
		return data.getExpiresIn();
	}
	
	public OnboardingUrlModel getOnboardingUrl(User principal) throws ExternalServiceError {
		Map<String,String> headers=new HashMap<>();
		headers.put("Authorization", "Bearer "+this.key);
		headers.put("Content-Type", "application/json");
		byte[] reqBody=String.format(onboardingRequestBody, principal.getId()).getBytes();
		
		HttpResponseModel response;
		try {
			response = httpClient.sendHttpRequest(HttpMethod.POST, baseUrl+ONBOARDING_REQ_PATH, headers, reqBody);
		} catch (AuctionAppException e1) {
			throw new ExternalServiceError();
		}
		
		OnboardingUrlResponseModel data;
		try {
			data = objectMapper.readValue(response.getBody(), OnboardingUrlResponseModel.class);
		} catch (JsonProcessingException e) {
			throw new ExternalServiceError();
		}

		OnboardingUrlModel model=new OnboardingUrlModel();
		model.setUrl(data.getLinks().get(ONBOARDING_URL_INDEX).getHref());
		return model;
	}
	
	public ClientTokenModel getClientToken() throws ExternalServiceError {
		Map<String,String> headers=new HashMap<>();
		
		headers.put("Authorization", "Bearer "+this.key);
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		headers.put("Accept-Language", "en_US");
		
		HttpResponseModel response;
		try {
			response = httpClient.sendHttpRequest(HttpMethod.POST, baseUrl+CLIENT_TOKEN_PATH, headers, null);
		} catch (AuctionAppException e1) {
			throw new ExternalServiceError();
		}
	
		ClientTokenModel model;
		try {
			model = objectMapper.readValue(response.getBody(), ClientTokenModel.class);
		} catch (JsonProcessingException e) {
			throw new ExternalServiceError();
		}
		
		model.setClientId(this.id);
		model.setBncode(bncode);
		model.setClientMerchantId(merchantId);
		return model;		
	}
	
	public void onboardingEvent(WebhookOnboardingModel data) {
		switch(data.getEventType()) {
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
			if(!user.getMerchantId().isBlank())
				return;
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
	
	public OrderModel getOrder(int itemId,User principal) throws AuctionAppException{
		Item item=new Item();
		item.setId(itemId);
		List<Order> orders=ordersRepo.findByItem(item);
		if(orders.isEmpty())
			return null;
		if(orders.get(0).getItem().getWinner().getId()!=principal.getId())
			throw new UnallowedOperationException();
		return orders.get(0).toModel();
	}
	
	public OrderModel createOrder(int itemId,User principal) throws AuctionAppException {
		Optional<Item> itemOpt=itemsRepo.findById(itemId);
		if(itemOpt.isEmpty())
			throw new InvalidDataException();
		Item item=itemOpt.get();
		if(item.getWinner().getId()!=principal.getId())
			throw new UnallowedOperationException();
		BigDecimal price=item.getBids().stream().max((a,b)->a.getAmount().compareTo(b.getAmount())).get().getAmount();
		OrderRequestModel orderReqData=new OrderRequestModel(item.getId(),price,item.getSeller().getMerchantId());
		
		OrderPayerRequestModel requestPayerModel=new OrderPayerRequestModel();
		requestPayerModel.setEmail(item.getWinner().getEmail());
		requestPayerModel.setName(new OrderPayerRequestModel.Name(item.getWinner().getName(), item.getWinner().getSurname()));
		
		if(item.getWinner().getAddress()!=null) {
			Address address=item.getWinner().getAddress();
			String countryCode=ccUtil.getCode(address.getCountry());
			if(countryCode!=null)
				requestPayerModel.setAddress(new OrderPayerRequestModel.Address(
						address.getAddress(), address.getCity(),
						countryCode, address.getZip()));
		}

		orderReqData.setPayer(requestPayerModel);
		
		Map<String,String> headers=new HashMap<>();
		headers.put("Authorization", "Bearer "+this.key);
		headers.put("PayPal-Partner-Attribution-Id", this.bncode);
		headers.put("Content-Type", "application/json");
		
		HttpResponseModel response;
		try {
			response = httpClient.sendHttpRequest(HttpMethod.POST,baseUrl+ORDER_PATH, headers, objectMapper.writeValueAsString(orderReqData).getBytes());
			System.out.println(objectMapper.writeValueAsString(orderReqData));
			System.out.println(response.getBody());
		} catch (JsonProcessingException | AuctionAppException e1) {
			throw new ExternalServiceError();
		}
		if(response.getStatusCode()>=300)
			throw new ExternalServiceError();

		OrderResponseModel orderResponse;
		try {
			orderResponse = objectMapper.readValue(response.getBody(), OrderResponseModel.class);
		} catch (JsonProcessingException e) {
			throw new ExternalServiceError();
		}
		
		if(!orderResponse.getStatus().equals("CREATED"))
			throw new ExternalServiceError();
		
		Order orderEntity=new Order();
		orderEntity.setOrderId(orderResponse.getId());
		orderEntity.setItem(item);
		orderEntity.setSuccesseful(false);
		ordersRepo.save(orderEntity);
		OrderModel orderResponseModel=new OrderModel(orderEntity.getOrderId(),item.getSeller().getMerchantId(),price,false);
		return orderResponseModel;
	}
	
	public synchronized OrderModel captureOrder(String orderId) throws AuctionAppException {

		Optional<Order> orderOpt=ordersRepo.findById(orderId);
		if(orderOpt.isEmpty())
			throw new InvalidDataException();
		
		Order orderEntity=orderOpt.get();
		if(orderEntity.isSuccesseful())
			return orderEntity.toModel();
		
		Map<String,String> headers=new HashMap<>();
		headers.put("Authorization", "Bearer "+this.key);
		headers.put("PayPal-Partner-Attribution-Id", this.bncode);
		headers.put("Content-Type", "application/json");
		
		HttpResponseModel response;
		try {
			response = httpClient.sendHttpRequest(HttpMethod.POST,baseUrl+ORDER_PATH+"/"+orderId+"/capture", headers, "{}".getBytes());
		} catch (AuctionAppException e) {
			throw new ExternalServiceError();
		}

		if(response.getStatusCode()!=201)
			throw new ExternalServiceError();
		
		orderEntity.setSuccesseful(true);
		ordersRepo.save(orderEntity);
		Item item=orderEntity.getItem();
		item.setPaid(true);
		itemsRepo.save(item);
		return orderEntity.toModel();
	}
	
	public void orderEvent(WebhookOrderModel order) {
		if(order.getEventType().equals("CHECKOUT.ORDER.APPROVED")) {
			try {
				captureOrder(order.getResource().getId());
			} catch (AuctionAppException e) {
				System.out.print("Order "+order.getResource().getId()+" was unsuccessful\n");
			}
		}
	}
}

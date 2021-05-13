package com.example.demo.controllers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AuthUser;
import com.example.demo.entities.Item;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.ExternalServiceError;
import com.example.demo.models.paypal.ClientTokenModel;
import com.example.demo.models.paypal.OnboardingUrlModel;
import com.example.demo.models.paypal.OrderModel;
import com.example.demo.models.paypal.WebhookOnboardingModel;
import com.example.demo.models.paypal.WebhookOrderModel;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.OrdersRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.DefaultPayPalTransactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "PayPal Controller", tags= {"PayPal Controller"}, description = "Enables users to use PayPal integration")
@RestController
public class PayPalController {
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private ItemsRepository itemsRepo;
	@Autowired
	private OrdersRepository ordersRepo;
	
	@Value("${paypal.id}")
	private String payPalId;
	@Value("${paypal.key}")
	private String payPalKey;
	@Value("${paypal.bncode}")
	private String bncode;
	@Value("${paypal.merchantid}")
	private String merchantId;
	@Value("${paypal.url}")
	private String baseUrl;
	
	private DefaultPayPalTransactionService payPalService;
	
	@PostConstruct
	public void init() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		payPalService=new DefaultPayPalTransactionService(payPalId, payPalKey,bncode,merchantId,baseUrl,ordersRepo,usersRepo,itemsRepo);
		int delay=payPalService.fetchKey();

		Runnable task=()->{
			refreshKey(executor,payPalService);
		};
		
		executor.schedule(task, delay, TimeUnit.SECONDS);
	}
	private void refreshKey(ScheduledExecutorService executor,DefaultPayPalTransactionService payPalService) {
		int delay=payPalService.fetchKey();
		executor.schedule(()->{refreshKey(executor,payPalService);}, delay, TimeUnit.SECONDS);
	}

	@ApiOperation(value = "Fetches PayPal onboarding url for user", notes = "Only authenticated users")
	@GetMapping("/api/getOnboardingUrl")
	public OnboardingUrlModel getOnboardingUrl(@AuthUser User principal) throws ExternalServiceError {
		return payPalService.getOnboardingUrl(principal);
	}

	@ApiOperation(value = "Fetches token for users to interact with PayPal", notes = "Only authenticated users")
	@GetMapping("/api/getClientToken")
	public ClientTokenModel getClientToken(@AuthUser User principal) throws ExternalServiceError {
		return payPalService.getClientToken();
	}

	@ApiOperation(value = "Creates order for item", notes = "Only authenticated users")
	@PostMapping("/api/createOrder")
	public OrderModel createOrder(@RequestBody Item item,@AuthUser User principal) throws AuctionAppException {
		return payPalService.createOrder(item,principal);
	}

	@ApiOperation(value = "Notifies about approved payment", notes = "Only authenticated users")
	@PostMapping("/api/captureOrder/{id}")
	public OrderModel captureOrder(@PathVariable String id,@AuthUser User principal) throws AuctionAppException {
		return payPalService.captureOrder(id);
	}
	//webhook endpoints
	@ApiOperation(value = "Webhook used by PayPal to notify onboarding related stuff", notes = "Used by PayPal service")
	@PostMapping("/api/onboardingHook")
	public boolean paypalOnboardEvent(@RequestBody WebhookOnboardingModel data) {
		payPalService.onboardingEvent(data);
		return true;
	}

	@ApiOperation(value = "Webhook used by PayPal to notify payment related stuff", notes = "Used by PayPal service")
	@PostMapping("/api/orderHook")
	public boolean paypalOrderEvent(@RequestBody WebhookOrderModel data) {
		payPalService.orderEvent(data);
		return true;
	}
}

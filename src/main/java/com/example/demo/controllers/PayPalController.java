package com.example.demo.controllers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AuthUser;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ExternalServiceError;
import com.example.demo.models.paypal.OnboardingUrlModel;
import com.example.demo.models.paypal.WebhookOnboardingModel;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.DefaultPayPalTransactionService;

import io.swagger.annotations.Api;

@Api(value = "PayPal Controller", tags= {"PayPal Controller"}, description = "Enables users to use PayPal integration")
@RestController
public class PayPalController {
	@Autowired
	private UsersRepository usersRepo;
	
	@Value("${paypal.id}")
	private String payPalId;
	@Value("${paypal.key}")
	private String payPalKey;
	@Value("${paypal.url}")
	private String baseUrl;
	
	private DefaultPayPalTransactionService payPalService;
	
	@PostConstruct
	public void init() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		payPalService=new DefaultPayPalTransactionService(payPalId, payPalKey,baseUrl,usersRepo);
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
	
	@GetMapping("/api/getOnboardingUrl")
	public OnboardingUrlModel getOnboardingUrl(@AuthUser User principal) throws ExternalServiceError {
		return payPalService.getOnboardingUrl(principal);
	}
	
	@PostMapping("/api/onboardingHook")
	public boolean paypalWebhookEvent(@RequestBody WebhookOnboardingModel data) {
		payPalService.onboardingEvent(data);
		return true;
	}
}

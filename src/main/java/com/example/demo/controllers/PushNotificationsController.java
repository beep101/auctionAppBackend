package com.example.demo.controllers;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AuthUser;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.NotificationModel;
import com.example.demo.models.PushServiceSubModel;
import com.example.demo.repositories.NotificationsRepository;
import com.example.demo.repositories.PushSubsRepository;
import com.example.demo.services.DefaultPushNotificationsService;
import com.example.demo.services.interfaces.PushNotificationsService;
import com.example.demo.utils.DefaultHttpClientAdapter;
import com.example.demo.utils.PushMessageEncryptionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Push Notifications Controller", tags= {"Push Notifications Controller"}, description = "Enables users to subscribe to push notifications")
@RestController
public class PushNotificationsController {
	
	@Value("${push.privateKey}")
	private String privateKey;
	@Value("${push.publicKey}")
	private String publicKey;
	
	@Autowired
	private PushSubsRepository pushSubsRepo;
	@Autowired
	private NotificationsRepository notificationsRepo;
	@Autowired
	private PushMessageEncryptionUtil msgEncryptionUtil;
	
	private PushNotificationsService pushNotificationsService;
	
	@PostConstruct
	public void init() throws AuctionAppException {
		this.pushNotificationsService=new DefaultPushNotificationsService(pushSubsRepo,notificationsRepo,msgEncryptionUtil,new DefaultHttpClientAdapter(),privateKey,publicKey);
	}

	@ApiOperation(value = "Fetches push notifications service public key", notes = "Only authenticated users")
	@GetMapping("/api/pushNotifications/publicKey")
	public String getPublicKey(@AuthUser User principal) throws AuctionAppException{
		return pushNotificationsService.getPublicKeyBase64();
	}

	@ApiOperation(value = "Subscribes browser to push notifications", notes = "Only authenticated users")
	@PostMapping("/api/pushNotifications")
	public PushServiceSubModel subscribe(@RequestBody PushServiceSubModel subData,@AuthUser User principal) throws AuctionAppException{
		return pushNotificationsService.subscribe(subData, principal);
	}
	
	@ApiOperation(value = "Unsubscribes browser that uses link passed in request", notes = "Only authenticated users")
	@DeleteMapping("/api/pushNotifications")
	public PushServiceSubModel unsubscribe(@RequestParam String link,@AuthUser User principal) throws AuctionAppException{
		return pushNotificationsService.unsubscribe(link, principal);
	}
	
	@GetMapping("/api/pushNotifications")
	public List<NotificationModel> getAllNotifications(@AuthUser User principal) throws AuctionAppException{
		return pushNotificationsService.getAllNotificationsForUser(principal);
	}
}

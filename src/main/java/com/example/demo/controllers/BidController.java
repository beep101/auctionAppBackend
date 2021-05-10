package com.example.demo.controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AuthUser;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.BidModel;
import com.example.demo.repositories.BidsRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.NotificationsRepository;
import com.example.demo.repositories.PushSubsRepository;
import com.example.demo.services.DefaultBidService;
import com.example.demo.services.DefaultPushNotificationsService;
import com.example.demo.services.interfaces.BidService;
import com.example.demo.services.interfaces.PushNotificationsService;
import com.example.demo.utils.DefaultHttpClientAdapter;
import com.example.demo.utils.PushMessageEncryptionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Bids Controller", tags = { "Bids Controller" }, description = "Manages bid related data")
@RestController
public class BidController {
	@Autowired
	private BidsRepository bidsRepo;
	@Autowired
	private ItemsRepository itemsRepo;
	
	private BidService bidService;
	
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
	
	@PostConstruct
	public void init() throws AuctionAppException {
		PushNotificationsService notificationsService=new DefaultPushNotificationsService(pushSubsRepo, notificationsRepo, msgEncryptionUtil,new DefaultHttpClientAdapter(), privateKey, publicKey);
		bidService=new DefaultBidService(bidsRepo, itemsRepo,notificationsService);
	}
	
	@ApiOperation(value = "Adds bid to item", notes = "Only authenticated users")
	@PostMapping("/api/bids")
	public BidModel addBid( @RequestBody BidModel bid,@AuthUser User principal) throws AuctionAppException{
		return bidService.addBid(bid,principal);
	}
	
	@ApiOperation(value = "Returns all bids for chosen item", notes = "Public access")
	@GetMapping("/api/items/{itemId}/bids")
	public Collection<BidModel> getBidsForItem(@PathVariable(name="itemId")int itemId, @RequestParam(required = false, defaultValue = "null") Integer limit){
		return bidService.getBids(itemId,limit);
		
	}
}

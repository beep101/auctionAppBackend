package com.example.demo.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;
import com.example.demo.entities.Notification;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.NotificationsRepository;
import com.example.demo.repositories.PushSubsRepository;
import com.example.demo.services.interfaces.PushNotificationsService;
import com.example.demo.utils.DefaultHttpClientAdapter;
import com.example.demo.utils.PushMessageEncryptionUtil;

@Component
public class ScheduledSoldItemsCheckComponent{

	@Autowired
	private ItemsRepository itemsRepo;
	
	@Autowired
	private PushSubsRepository pushSubsRepo;
	@Autowired
	private NotificationsRepository notificationsRepo;
	@Autowired
	private PushMessageEncryptionUtil msgEncryptionUtil;
	
	@Value("${push.privateKey}")
	private String privateKey;
	@Value("${push.publicKey}")
	private String publicKey;

	private PushNotificationsService notificationsService;
	
	public ScheduledSoldItemsCheckComponent() {
		super();
	}
	
	public ScheduledSoldItemsCheckComponent(ItemsRepository itemsRepo, PushNotificationsService notificationsService) throws AuctionAppException {
		super();
		
		this.itemsRepo=itemsRepo;
		this.notificationsService=notificationsService;
	}
	
	@PostConstruct
	public void init() throws AuctionAppException {
		this.notificationsService=new DefaultPushNotificationsService(pushSubsRepo,notificationsRepo,msgEncryptionUtil,new DefaultHttpClientAdapter(),privateKey,publicKey);
	}

	@Transactional
	@Scheduled(fixedDelay = 60000)
	public void checkAndNotify() {
		List<Item> items=itemsRepo.findBySoldFalseAndEndtimeBefore(new Timestamp(System.currentTimeMillis()));
		if(items.isEmpty())
			return;
		List<Notification> notifications=items.stream().map(this::makeNotifications).flatMap(List::stream).collect(Collectors.toList());
		this.notificationsService.sendMultipleNotifications(notifications);
		items.stream().forEach(x->{
			x.setSold(true);
			if(!x.getBids().isEmpty())
				x.setWinner(x.getBids().stream().max((a,b)->a.getAmount().compareTo(b.getAmount())).get().getBidder());
			});
		itemsRepo.saveAll(items);
	}
	
	private List<Notification> makeNotifications(Item item){
		List<Notification> ntfs;
		if(item.getBids().isEmpty()) {
			ntfs=new ArrayList<>(1);
			Notification notSold=new Notification(item.getName()+" auction was unsuccesseful", "Your item did not sell","/item?id="+item.getId());
			notSold.setUser(item.getSeller());
			notSold.setTime(new Timestamp(System.currentTimeMillis()));
			ntfs.add(notSold);
		}else {
			ntfs=new ArrayList<>(2);
			Bid maxBid;
			if(item.getBids().size()==1)
				maxBid=item.getBids().get(0);
			else
				maxBid=item.getBids().stream().max((a,b)->a.getAmount().compareTo(b.getAmount())).get();
			
			Notification sold=new Notification(item.getName()+" auction was successeful", "Your item sold to "+maxBid.getBidder().getName()+" "+maxBid.getBidder().getSurname(),"/item?id="+item.getId());
			sold.setUser(item.getSeller());
			sold.setTime(new Timestamp(System.currentTimeMillis()));
			ntfs.add(sold);

			Notification won=new Notification("You won auction for "+item.getName(), "Price you are paying for item is $"+maxBid.getAmount(),"/item?id="+item.getId());
			won.setUser(maxBid.getBidder());
			won.setTime(new Timestamp(System.currentTimeMillis()));
			ntfs.add(won);
		}
		return ntfs;
	}

}

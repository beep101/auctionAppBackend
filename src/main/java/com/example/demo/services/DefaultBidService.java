package com.example.demo.services;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;
import com.example.demo.entities.Notification;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.BidAmountLowException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.BidModel;
import com.example.demo.repositories.BidsRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.interfaces.BidService;
import com.example.demo.services.interfaces.PushNotificationsService;


public class DefaultBidService implements BidService{
	
	private BidsRepository bidsRepo;
	private ItemsRepository itemsRepo;
	
	private PushNotificationsService notificationsService;

	public DefaultBidService(BidsRepository bidsRepo,ItemsRepository itemsRepo, PushNotificationsService notificationsService) {
		this.bidsRepo=bidsRepo;
		this.itemsRepo=itemsRepo;
		this.notificationsService=notificationsService;
	}
	
	@Override
	public synchronized BidModel addBid(BidModel bidModel, User user) throws AuctionAppException{
		if(bidModel.getBidder().getId()!=user.getId()) {
			throw new InvalidDataException();
		}
		
		//fetch bidded item
		Bid bidEntity=Bid.fromModel(bidModel);
		Item item=null;
		try {
			item=itemsRepo.getOne(bidEntity.getItem().getId());
		}catch(EntityNotFoundException ex) {
			throw new NotFoundException();
		}
		
		//check rules
		//check if item expored or sold
		if(item.getSold()||item.getEndtime().compareTo(new Timestamp(System.currentTimeMillis()))<=0){
			Map<String,String> errors=new HashMap<>();
			errors.put("Bid", "Not allowed to bid expired or sold item");
			throw new UnallowedOperationException(errors);
		}
		
		//check if bidder is owner
		if(item.getSeller().getId()==user.getId()){
			Map<String,String> errors=new HashMap<>();
			errors.put("User", "Not allowed to bid own items");
			throw new UnallowedOperationException(errors);
		}
		//check if bidder is already max bidder
		Optional<Bid> maxBid=item.getBids().stream().max((a,b)->a.getAmount().compareTo(b.getAmount()));
		if(maxBid.isPresent()&&maxBid.get().getBidder().getId()==user.getId()) {
			Map<String,String> errors=new HashMap<>();
			errors.put("User", "Already highest bidder");
			throw new UnallowedOperationException(errors);
		}
		//check if bid amount is enough
		if(maxBid.isPresent()) {
			if(maxBid.get().getAmount().compareTo(bidEntity.getAmount())>=0) {
				throw new BidAmountLowException();
			}
		}else if(item.getStartingprice().compareTo(bidEntity.getAmount())>=0) {
			throw new BidAmountLowException();
		}	

		Timestamp crr=new Timestamp(System.currentTimeMillis());
		bidEntity.setTime(crr);
		bidEntity=bidsRepo.save(bidEntity);
		
		//notify last bidder and seller
		if(maxBid.isPresent()) {
			User maxBidder=maxBid.get().getBidder();
			maxBidder.getPushSub();
			Notification ntf=new Notification(
					"You are outbidded!",
					"Someone bidded more than you on "+item.getName(),
					"/item?id="+item.getId());
			ntf.setUser(maxBidder);
			ntf.setTime(new Timestamp(System.currentTimeMillis()));
			notificationsService.sendNotification(ntf);
		}
		User seller=item.getSeller();
		seller.getPushSub();
		Notification ntf=new Notification(
				"Your item received a bid",
				user.getName()+" "+user.getSurname()+" bidded on "+item.getName(),
				"/item?id="+item.getId());
		ntf.setUser(seller);
		ntf.setTime(new Timestamp(System.currentTimeMillis()));
		notificationsService.sendNotification(ntf);
		
		return bidEntity.toModel();
	}
	
	@Override
	public Collection<BidModel> getBids(int itemId,Integer limit) {
		Item item=new Item();
		item.setId(itemId);
		if(limit!=null) {
			return bidsRepo.findByItemEqualsOrderByIdDesc(item,PageRequest.of(0,limit.intValue())).stream().map(x->x.toModel()).collect(Collectors.toList());
		}else {
			return bidsRepo.findByItemEqualsOrderByIdDesc(item,Pageable.unpaged()).stream().map(x->x.toModel()).collect(Collectors.toList());
		}
	}
	
	
}

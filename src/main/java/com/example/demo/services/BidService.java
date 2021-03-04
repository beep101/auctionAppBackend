package com.example.demo.services;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;
import com.example.demo.entities.User;
import com.example.demo.exceptions.BidAmountLowException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.BidModel;
import com.example.demo.repositories.BidsRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.interfaces.IBidService;


public class BidService implements IBidService{
	
	private BidsRepository bidsRepo;
	private ItemsRepository itemsRepo;

	public BidService(BidsRepository bidsRepo,ItemsRepository itemsRepo) {
		this.bidsRepo=bidsRepo;
		this.itemsRepo=itemsRepo;
	}
	
	//tests if user bidder mismatch throws error
	//if bid entity valid
	//if checks max amount
	//if checks base price
	
	@Override
	public synchronized BidModel addBid(BidModel bidModel, User user) throws InvalidDataException,BidAmountLowException,NotFoundException {
		if(bidModel.getBidder().getId()!=user.getId()) {
			throw new InvalidDataException();
		}
		Bid bidEntity=Bid.fromModel(bidModel);
		
		OptionalDouble maxVal=bidsRepo.findByItemEquals(bidEntity.getItem()).stream().mapToDouble(x->x.getAmount().doubleValue()).max();
		if(maxVal.isPresent()) {
			if(maxVal.getAsDouble()>=bidEntity.getAmount().doubleValue()) {
				throw new BidAmountLowException();
			}
		}else {
			Optional<Item> item=itemsRepo.findById(bidEntity.getItem().getId());
			if(item.isPresent()) {
				if(item.get().getStartingprice().doubleValue()>bidEntity.getAmount().doubleValue()) {
					throw new BidAmountLowException();
				}
			}else {
				throw new NotFoundException();
			}
		}
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		bidEntity.setTime(crr);
		bidModel.setTime(crr);
		return bidsRepo.save(bidEntity).toModel();
	}
	
	//check item creation
	//check sorting
	//check to model

	@Override
	public Collection<BidModel> getBids(int itemId,int limit) {
		Item item=new Item();
		item.setId(itemId);
		List<BidModel> bids= bidsRepo.findByItemEquals(item).stream().map(x->x.toModel())
				.collect(Collectors.toList());
		bids.sort(new Comparator<BidModel>() {
			@Override
			public int compare(BidModel o1, BidModel o2) {
				return o2.getAmount().compareTo(o1.getAmount());
			}
		});
		if(limit!=0&&limit<bids.size()) {
			return bids.subList(0, limit);
		}
		return bids;
	}
	
	
}

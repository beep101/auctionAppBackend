package com.example.demo.services.interfaces;

import java.util.Collection;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.BidModel;

public interface BidService {
	BidModel addBid(BidModel bidModel, User user)  throws AuctionAppException;
	Collection<BidModel> getBids(int itemId,Integer limit);
}

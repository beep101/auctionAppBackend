package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.WishModel;

public interface WishService {
	WishModel createWish(WishModel wish,User principal) throws AuctionAppException;
	WishModel deleteWish(WishModel wish,User principal) throws AuctionAppException;
	List<WishModel> getWishes(User principal) throws AuctionAppException;
}

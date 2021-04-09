package com.example.demo.services.interfaces;

import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.UserModel;

public interface IAccountService{
	UserModel login(UserModel login) throws AuctionAppException ;
	UserModel signUp(UserModel signup) throws AuctionAppException ;
	UserModel forgotPassword(UserModel forgotPassword) throws AuctionAppException;
	UserModel newPassword(UserModel newPassword) throws AuctionAppException;
}

package com.example.demo.services.interfaces;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.AddressModel;
import com.example.demo.models.PayMethodModel;
import com.example.demo.models.UserModel;

public interface IAccountService{
	UserModel login(UserModel login) throws AuctionAppException ;
	UserModel signUp(UserModel signup) throws AuctionAppException ;
	UserModel forgotPassword(UserModel forgotPassword) throws AuctionAppException;
	UserModel newPassword(UserModel newPassword) throws AuctionAppException;
	UserModel updateAccount(UserModel userData,User authUser)throws AuctionAppException;
	UserModel addAddress(AddressModel addressData,User authUser)throws AuctionAppException;
	UserModel modAddress(AddressModel addressData,User authUser)throws AuctionAppException;
	UserModel addPayMethod(PayMethodModel payData,User principal)throws AuctionAppException;
	UserModel modPayMethod(PayMethodModel payData,User principal)throws AuctionAppException;
	UserModel refreshToken(User principal) throws AuctionAppException;
}
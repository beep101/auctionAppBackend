package com.example.demo.services.interfaces;

import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.ExistingUserException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NonExistentUserException;
import com.example.demo.models.UserModel;

public interface IAccountService{
	UserModel login(UserModel login) throws BadCredentialsException ;
	UserModel signUp(UserModel signup) throws InvalidDataException,ExistingUserException,NonExistentUserException ;
	UserModel forgotPassword(UserModel forgotPassword);
}

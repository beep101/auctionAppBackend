package com.example.demo.services.interfaces;

import com.example.demo.models.UserModel;

public interface IAccountService {
	UserModel login(UserModel login);
	UserModel signUp(UserModel signup);
	UserModel forgotPassword(UserModel forgotPassword);
}

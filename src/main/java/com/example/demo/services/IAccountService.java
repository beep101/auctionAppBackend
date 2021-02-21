package com.example.demo.services;

import com.example.demo.models.ForgotPasswordModel;
import com.example.demo.models.LoginModel;
import com.example.demo.models.SignupModel;

public interface IAccountService {
	String login(LoginModel login);
	String signUp(SignupModel signup);
	void forgotPassword(ForgotPasswordModel forgotPassword);
}

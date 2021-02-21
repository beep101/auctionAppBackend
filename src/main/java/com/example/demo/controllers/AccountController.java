package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ForgotPasswordModel;
import com.example.demo.models.LoginModel;
import com.example.demo.models.SignupModel;
import com.example.demo.services.IAccountService;


@RestController
public class AccountController {
	
	@Autowired
	IAccountService accountService;
	
	@PostMapping("/api/login")
	public String login(@RequestBody LoginModel data) {
		return accountService.login(data);
	}
	
	@PostMapping("/api/signup")
	public String signup(@RequestBody SignupModel data) {
		return accountService.signUp(data);
	}
	
	@PostMapping("/api/forgotPassword")
	public void forgotPassword(@RequestBody ForgotPasswordModel data) {
		accountService.forgotPassword(data);
	}
}

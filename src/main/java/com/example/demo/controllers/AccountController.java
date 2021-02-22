package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ForgotPasswordModel;
import com.example.demo.models.LoginModel;
import com.example.demo.models.SignupModel;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.AccountService;
import com.example.demo.services.IAccountService;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;


@RestController
public class AccountController {
	
	@Autowired
	IHashUtil hashUtil;
	@Autowired
	IJwtUtil jwtUtil;
	@Autowired
	UsersRepository usersRepo;
	
	IAccountService accountService=new AccountService(hashUtil, jwtUtil, usersRepo);
	
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

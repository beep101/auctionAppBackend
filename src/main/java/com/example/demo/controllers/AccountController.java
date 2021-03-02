package com.example.demo.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.UserModel;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.AccountService;
import com.example.demo.services.interfaces.IAccountService;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;


@RestController
public class AccountController {
	
	@Autowired
	private IHashUtil hashUtil;
	@Autowired
	private IJwtUtil jwtUtil;
	@Autowired
	private UsersRepository usersRepo;
	private IAccountService accountService;
	
	@PostConstruct
	public void init() {
		accountService=new AccountService(hashUtil, jwtUtil, usersRepo);
	}
	
	@PostMapping("/api/login")
	public UserModel login(@RequestBody UserModel data) {
		return accountService.login(data);
	}
	
	@PostMapping("/api/signup")
	public UserModel signup(@RequestBody UserModel data) {
		return accountService.signUp(data);
	}
	
	@PostMapping("/api/forgotPassword")
	public void forgotPassword(@RequestBody UserModel data) {
		accountService.forgotPassword(data);
	}
}

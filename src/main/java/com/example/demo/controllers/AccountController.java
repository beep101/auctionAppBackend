package com.example.demo.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.ExistingUserException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.InvalidTokenException;
import com.example.demo.exceptions.NonExistentUserException;
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
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${mail.subject}")
	private String subject;
	@Value("${mail.content}")
	private String content;
	@Value("${mail.link}")
	private String link;

	private IAccountService accountService;
	
	@PostConstruct
	public void init() {
		accountService=new AccountService(hashUtil, jwtUtil, usersRepo,mailSender,subject,content,link);
	}
	
	@PostMapping("/api/login")
	public UserModel login(@RequestBody UserModel data) throws BadCredentialsException {
		return accountService.login(data);
	}
	
	@PostMapping("/api/signup")
	public UserModel signup(@RequestBody UserModel data) throws InvalidDataException, ExistingUserException, NonExistentUserException {
		return accountService.signUp(data);
	}
	
	@PostMapping("/api/forgotPassword")
	public UserModel forgotPassword(@RequestBody UserModel data) throws NonExistentUserException {
		return accountService.forgotPassword(data);
	}
	
	@PostMapping("api/newPassword")
	public UserModel newPassword(@RequestBody UserModel data) throws InvalidTokenException, InvalidDataException {
		return accountService.newPassword(data);
	}
}

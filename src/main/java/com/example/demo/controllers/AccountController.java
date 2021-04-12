package com.example.demo.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.UnauthenticatedException;
import com.example.demo.models.AddressModel;
import com.example.demo.models.PayMethodModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.PayMethodRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.AccountService;
import com.example.demo.services.interfaces.IAccountService;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Account controller", tags = { "Account controller" }, description =  "Manges user's interaction with his account data")
@RestController
public class AccountController {
	
	@Autowired
	private IHashUtil hashUtil;
	@Autowired
	private IJwtUtil jwtUtil;
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private AddressesRepository addressRepo;
	@Autowired
	private PayMethodRepository payMethodRepo;
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
		accountService=new AccountService(hashUtil, jwtUtil, usersRepo,addressRepo,payMethodRepo,mailSender,subject,content,link);
	}
	
	@ApiOperation(value = "Requires valid email and password to return JWT", notes = "Public access")
	@PostMapping("/api/login")
	public UserModel login(@RequestBody UserModel data) throws AuctionAppException {
		return accountService.login(data);
	}
	
	@ApiOperation(value = "Creates new user account", notes = "Public access")
	@PostMapping("/api/signup")
	public UserModel signup(@RequestBody UserModel data) throws AuctionAppException {
		return accountService.signUp(data);
	}
	
	@ApiOperation(value = "Uses user email to generate password recovery link that is sent to email", notes = "Public access")
	@PostMapping("/api/forgotPassword")
	public UserModel forgotPassword(@RequestBody UserModel data) throws AuctionAppException {
		return accountService.forgotPassword(data);
	}
	
	@ApiOperation(value = "Link used to change forgotten password, requires recovery token", notes = "Public access")
	@PostMapping("api/newPassword")
	public UserModel newPassword(@RequestBody UserModel data) throws AuctionAppException {
		return accountService.newPassword(data);
	}

	@ApiOperation(value = "Updates account data", notes = "Only authenticated users")
	@PutMapping("api/account")
	public UserModel updateData(@RequestBody UserModel data) throws AuctionAppException{
		User principal=null;
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		return accountService.updateAccount(data,principal);
	}
	
	@ApiOperation(value = "Bind new address to account", notes = "Only authenticated users")
	@PostMapping("api/account/address")
	public UserModel addAddress(@RequestBody AddressModel data) throws AuctionAppException{
		User principal=null;
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		return accountService.addAddress(data,principal);
	}
	
	@ApiOperation(value = "Modifies current address", notes = "Only authenticated users")
	@PutMapping("api/account/address")
	public UserModel modAddress(@RequestBody AddressModel data) throws AuctionAppException{
		User principal=null;
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		return accountService.modAddress(data,principal);
	}
	
	@ApiOperation(value = "Bind new pay method to account", notes = "Only authenticated users")
	@PostMapping("api/account/payMethod")
	public UserModel addPayMethod(@RequestBody PayMethodModel data) throws AuctionAppException{
		User principal=null;
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		return accountService.addPayMethod(data,principal);
	}
	
	@ApiOperation(value = "Modifies current pay method", notes = "Only authenticated users")
	@PutMapping("api/account/payMethod")
	public UserModel modPayMethod(@RequestBody PayMethodModel data) throws AuctionAppException{
		User principal=null;
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		return accountService.modPayMethod(data,principal);
	}
}

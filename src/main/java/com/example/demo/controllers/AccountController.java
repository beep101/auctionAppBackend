package com.example.demo.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AuthUser;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.AddressModel;
import com.example.demo.models.PayMethodModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.BidsRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.PayMethodRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.DefaultAccountService;
import com.example.demo.services.S3ImageStorageService;
import com.example.demo.services.interfaces.AccountService;
import com.example.demo.services.interfaces.ImageStorageService;
import com.example.demo.utils.AwsS3Adapter;
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
	@Autowired
	private ItemsRepository itemsRepo;
	@Autowired
	private BidsRepository bidsRepo;
	
	@Value("${mail.subject}")
	private String subject;
	@Value("${mail.content}")
	private String content;
	@Value("${mail.link}")
	private String link;
	
	@Value("${s3.id}")
	private String id;
	@Value("${s3.key}")
	private String key;
	@Value("${s3.userImageBucketUrl}")
	private String imageBucketBaseUrl;
	@Value("${s3.userBucketName}")
	private String bucketName;

	private AccountService accountService;
	private ImageStorageService<UserModel> imageService;
	
	@PostConstruct
	public void init() {
		imageService=new S3ImageStorageService<UserModel>(bucketName,imageBucketBaseUrl,new AwsS3Adapter(id, key));
		accountService=new DefaultAccountService(imageService,hashUtil, jwtUtil, usersRepo,addressRepo,payMethodRepo,itemsRepo,bidsRepo,mailSender,subject,content,link);
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
	public UserModel updateData(@RequestBody UserModel data,@AuthUser User principal) throws AuctionAppException{
		return accountService.updateAccount(data,principal);
	}
	
	@Transactional
	@ApiOperation(value = "Deletes account with specified id", notes = "Only authenticated users")
	@DeleteMapping("api/account/{id}")
	public UserModel deleteAccount(@PathVariable int id,@RequestParam boolean permanent,@AuthUser User principal) throws AuctionAppException{
		return accountService.deleteAccount(id,permanent,principal);
	}
	
	@ApiOperation(value = "Bind new address to account", notes = "Only authenticated users")
	@PostMapping("api/account/address")
	public UserModel addAddress(@RequestBody AddressModel data,@AuthUser User principal) throws AuctionAppException{
		return accountService.addAddress(data,principal);
	}
	
	@ApiOperation(value = "Modifies current address", notes = "Only authenticated users")
	@PutMapping("api/account/address")
	public UserModel modAddress(@RequestBody AddressModel data,@AuthUser User principal) throws AuctionAppException{
		return accountService.modAddress(data,principal);
	}
	
	@ApiOperation(value = "Bind new pay method to account", notes = "Only authenticated users")
	@PostMapping("api/account/payMethod")
	public UserModel addPayMethod(@RequestBody PayMethodModel data,@AuthUser User principal) throws AuctionAppException{
		return accountService.addPayMethod(data,principal);
	}
	
	@ApiOperation(value = "Modifies current pay method", notes = "Only authenticated users")
	@PutMapping("api/account/payMethod")
	public UserModel modPayMethod(@RequestBody PayMethodModel data,@AuthUser User principal) throws AuctionAppException{
		return accountService.modPayMethod(data,principal);
	}
	
	@ApiOperation(value = "Sets new profile image for user", notes = "Only authenticated users")
	@PostMapping("api/account/image")
	public UserModel addProfileImage(@RequestBody UserModel data,@AuthUser User principal) throws AuctionAppException{
		return accountService.setProfileImage(data,principal);
	}
	
	@ApiOperation(value = "Returns new extended JWT if old still valid", notes = "Only authenticated users")
	@PostMapping("api/refresh")
	public UserModel refreshToken(@AuthUser User principal) throws AuctionAppException{
		return accountService.refreshToken(principal);
	}
	
	@ApiOperation(value = "Turn ON/OFF push notifications", notes = "Only authenticated users")
	@PostMapping("api/account/pushNotifications")
	public UserModel pushNotificationsOnOff(@AuthUser User principal) throws AuctionAppException{
		return accountService.pushNotificationsOnOff(principal);
	}
	
}

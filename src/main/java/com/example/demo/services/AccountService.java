package com.example.demo.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.demo.entities.Address;
import com.example.demo.entities.PayMethod;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.ExistingUserException;
import com.example.demo.exceptions.ImageHashException;
import com.example.demo.exceptions.ImageUploadException;
import com.example.demo.exceptions.InsertFailedException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.InvalidTokenException;
import com.example.demo.exceptions.NonExistentUserException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.AddressModel;
import com.example.demo.models.PayMethodModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.PayMethodRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.interfaces.IAccountService;
import com.example.demo.services.interfaces.IImageStorageService;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;
import com.example.demo.validations.AddressRequest;
import com.example.demo.validations.PayMethodRequest;
import com.example.demo.validations.UserRequest;

public class AccountService implements IAccountService{
	private UsersRepository usersRepo;
	private AddressesRepository addressRepo;
	private PayMethodRepository payMethodRepo;
	private IHashUtil hashUtil;
	private IJwtUtil jwtUtil;
	private JavaMailSender mailSender;
	
	public static final int ONE_DAY_DELAY_MILIS = 24*60*60*1000;
	
	private String mailSubject;
	private String mailContent;
	private String mailLink;

	private IImageStorageService<UserModel> imageService;
	
	public AccountService(IImageStorageService<UserModel> imageService,IHashUtil hashUtil,IJwtUtil jwtUtil, UsersRepository userRepo,AddressesRepository addressRepo,PayMethodRepository payMethodRepo, JavaMailSender mailSender,
						  String subject,String content,String link) {
		this.imageService=imageService;
		
		this.usersRepo=userRepo;
		this.addressRepo=addressRepo;
		this.payMethodRepo=payMethodRepo;
		this.hashUtil=hashUtil;
		this.jwtUtil=jwtUtil;
		this.mailSender=mailSender;
		
		this.mailSubject=subject;
		this.mailContent=content;
		this.mailLink=link;
	}

	@Override
	public UserModel login(UserModel login) throws AuctionAppException {
		Optional<User> users=usersRepo.findByEmail(login.getEmail());
		if(users.isPresent()) {
			if(hashUtil.checkPassword(login.getPassword(), users.get().getPasswd())) {
				Map<String,Object> data= new HashMap<String, Object>();
				if(users.get().getAddress()!=null)
					data.put("address", users.get().getAddress().toModel());
				data.put("user", imageService.loadImagesForItem(users.get().toModelWithPayMethod()));
				String jwt=jwtUtil.generateToken(users.get(),data);
				login.setId(users.get().getId());
				login.setFirstName(users.get().getName());
				login.setLastName(users.get().getSurname());
				login.setPassword(null);
				login.setJwt(jwt);
				return login;
			}else {
				Map<String,String> problems=new HashMap<>();
				problems.put("password", "Invalid password");
				throw new BadCredentialsException(problems);
			}
		}else {
			Map<String,String> problems=new HashMap<>();
			problems.put("email", "No user with email");
			throw new BadCredentialsException(problems);
		}
	}
	

	@Override
	public UserModel refreshToken(User principal) throws AuctionAppException {
		UserModel refresh=principal.toModel();
		Map<String,Object> data= new HashMap<String, Object>();
		data.put("user", imageService.loadImagesForItem(principal.toModelWithPayMethod()));
		String jwt=jwtUtil.generateToken(principal,data);
		refresh.setJwt(jwt);
		return refresh;
	}

	@Override
	public UserModel signUp(UserModel signup)throws AuctionAppException {
		Map<String,String> problems=(new UserRequest(signup)).validate();
		if(!problems.isEmpty()) {
			throw new InvalidDataException(problems);
		}
		if(usersRepo.findByEmail(signup.getEmail()).isPresent()) {
			problems=new HashMap<>();
			problems.put("email", "Email alredy in use");
			throw new ExistingUserException(problems);
		}
		User newUser=new User();
		newUser.setName(signup.getFirstName());
		newUser.setSurname(signup.getLastName());
		newUser.setEmail(signup.getEmail());
		newUser.setPasswd(hashUtil.hashPassword(signup.getPassword()));
		usersRepo.save(newUser);
		Optional<User> users=usersRepo.findByEmail(signup.getEmail());
		if(users.isPresent()) {
			Map<String,Object> data= new HashMap<String, Object>();
			data.put("user", users.get().toModelWithPayMethod());
			String jwt=jwtUtil.generateToken(users.get(),data);
			signup.setJwt(jwt);
			return signup;
		}else {
			throw new NonExistentUserException();
		}
	}

	
	@Override
	public UserModel forgotPassword(UserModel forgotPassword) throws AuctionAppException {
		Map<String,String> problems=(new UserRequest(forgotPassword)).validate();
		if(problems.containsKey("email")) {
			String msg=problems.get("email");
			problems.clear();
			problems.put("email", msg);
			throw new NonExistentUserException(problems);
		}
		Optional<User> users=usersRepo.findByEmail(forgotPassword.getEmail());
		if(users.isEmpty()) {
			problems.clear();
			problems.put("email", "No user with email");
			throw new NonExistentUserException(problems);
		}
		User user=users.get();
		String token=UUID.randomUUID().toString().replace("-","");
		user.setForgotPasswordToken(token);
		user.setForgotPasswordTokenEndTime(new Timestamp(System.currentTimeMillis()+ONE_DAY_DELAY_MILIS));
		user=usersRepo.save(user);
		
		sendMail(user.getEmail(),user.getForgotPasswordToken());
		
		return imageService.loadImagesForItem(user.toModel());
	}
	
	@Override
	public UserModel newPassword(UserModel newPassword) throws AuctionAppException {
		Map<String,String> problems=(new UserRequest(newPassword)).validate();
		if(problems.containsKey("password")) {
			String msg=problems.get("password");
			problems.clear();
			problems.put("password", msg);
			throw new InvalidDataException(problems);
		}
		if(newPassword.getForgotPasswordToken()==null || newPassword.getForgotPasswordToken().equals("")) {
			throw new InvalidTokenException();
		}
		
		Optional<User> users=usersRepo.findByForgotPasswordTokenAndForgotPasswordTokenEndTimeAfter(newPassword.getForgotPasswordToken(),new Timestamp(System.currentTimeMillis()));
		if(users.isEmpty()) {
			throw new InvalidTokenException();
		}
		
		User user=users.get();
		user.setPasswd(hashUtil.hashPassword(newPassword.getPassword()));
		user.setForgotPasswordTokenEndTime(new Timestamp(System.currentTimeMillis()));
		return imageService.loadImagesForItem(usersRepo.save(user).toModel());
	}
	
	private void sendMail(String to,String token) {
		SimpleMailMessage message=new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject(mailSubject);
		message.setText(mailContent+"\n"+mailLink+token);
		
		mailSender.send(message);
	}

	@Override
	public UserModel updateAccount(UserModel userData, User authUser) throws AuctionAppException {
		UserRequest request=new UserRequest(userData);
		Map<String,String> problems=request.validateIncludingGenderAndBirthday();
		List<String> keys=new ArrayList<>();
		keys.add("firstName");
		keys.add("lastName");
		keys.add("email");
		keys.add("gender");
		keys.add("birthday");
		if(keys.stream().filter(key->problems.containsKey(key)).count()!=0)
			throw new InvalidDataException(problems);
		authUser.setEmail(userData.getEmail());
		authUser.setName(userData.getFirstName());
		authUser.setSurname(userData.getLastName());
		authUser.setGender(userData.getGender());
		authUser.setBirthday(userData.getBirthday());
		return imageService.loadImagesForItem(usersRepo.save(authUser).toModel());
	}

	@Override
	public UserModel addAddress(AddressModel addressData, User authUser) throws AuctionAppException {
		AddressRequest request=new AddressRequest(addressData);
		Map<String,String> problems=request.validate();
		if(!problems.isEmpty())
			throw new InvalidDataException(problems);
		Address address=new Address();
		address.populate(addressData);
		address=addressRepo.save(address);
		authUser.setAddress(address);
		authUser=usersRepo.save(authUser);
		if(authUser.getAddress()!=null&&authUser.getAddress().getId()==address.getId())
			return imageService.loadImagesForItem(authUser.toModelWithPayMethod());
		else {
			addressRepo.delete(address);
			problems.clear();
			problems.put("save", "Cannot save data");
			throw new InvalidDataException(problems);
		}
	}

	@Override
	public UserModel modAddress(AddressModel addressData, User authUser) throws AuctionAppException {
		AddressRequest request=new AddressRequest(addressData);
		Map<String,String> problems=request.validate();
		if(!problems.isEmpty())
			throw new InvalidDataException(problems);
		if(authUser.getAddress()==null) {
			throw new UnallowedOperationException("Cannot update nonexistent address");
		}
		Address address=new Address();
		address.populate(addressData);
		address.setId(authUser.getAddress().getId());
		authUser.setAddress(addressRepo.save(address));
		return imageService.loadImagesForItem(authUser.toModelWithPayMethod());
	}

	@Override
	public UserModel addPayMethod(PayMethodModel payData, User authUser) throws AuctionAppException {
		PayMethodRequest request=new PayMethodRequest(payData);
		Map<String,String> problems=request.validate();
		if(!problems.isEmpty())
			throw new InvalidDataException(problems);
		PayMethod payMethod=new PayMethod();
		payMethod.populate(payData);
		payMethod=payMethodRepo.save(payMethod);
		authUser.setPayMethod(payMethod);
		authUser=usersRepo.save(authUser);
		if(authUser.getPayMethod()!=null&&authUser.getPayMethod().getId()==payMethod.getId())
			return imageService.loadImagesForItem(authUser.toModelWithPayMethod());
		else {
			payMethodRepo.delete(payMethod);
			problems.clear();
			problems.put("save", "Cannot save data");
			throw new InvalidDataException(problems);
		}
	}

	@Override
	public UserModel modPayMethod(PayMethodModel payData, User authUser) throws AuctionAppException {
		PayMethodRequest request=new PayMethodRequest(payData);
		Map<String,String> problems=request.validate();
		if(!problems.isEmpty())
			throw new InvalidDataException(problems);
		if(authUser.getPayMethod()==null) {
			throw new UnallowedOperationException("Cannot update nonexistent pay method data");
		}
		PayMethod payMethod=new PayMethod();
		payMethod.populate(payData);
		payMethod.setId(authUser.getPayMethod().getId());
		authUser.setPayMethod(payMethodRepo.save(payMethod));
		return imageService.loadImagesForItem(authUser.toModelWithPayMethod());
	}

	@Override
	public UserModel setProfileImage(UserModel userData,User principal) throws AuctionAppException {
		try {
			imageService.addImage(Integer.toString(principal.getId()), userData.getNewImage());
		} catch (ImageUploadException | ImageHashException e) {
			throw new InsertFailedException();
		}
		return imageService.loadImagesForItem(principal.toModelWithPayMethod());
	}

}

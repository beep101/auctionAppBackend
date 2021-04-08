package com.example.demo.services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.ExistingUserException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.InvalidTokenException;
import com.example.demo.exceptions.NonExistentUserException;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.interfaces.IAccountService;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;
import com.example.demo.validations.UserRequest;

public class AccountService implements IAccountService{
	private UsersRepository usersRepo;
	private IHashUtil hashUtil;
	private IJwtUtil jwtUtil;
	private JavaMailSender mailSender;
	
	public static final int ONE_DAY_DELAY_MILIS = 24*60*60*1000;
	
	private String mailSubject;
	private String mailContent;
	private String mailLink;
	
	public AccountService(IHashUtil hashUtil,IJwtUtil jwtUtil, UsersRepository userRepo, JavaMailSender mailSender,
						  String subject,String content,String link) {
		this.usersRepo=userRepo;
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
				String jwt=jwtUtil.generateToken(users.get(),data);
				login.setId(users.get().getId());
				login.setFirstName(users.get().getName());
				login.setLastName(users.get().getSurname());
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
			String jwt=jwtUtil.generateToken(users.get(), new HashMap<String, Object>());
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
		
		return user.toModel();
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
		return usersRepo.save(user).toModel();
	}
	
	private void sendMail(String to,String token) {
		SimpleMailMessage message=new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject(mailSubject);
		message.setText(mailContent+"\n"+mailLink+token);
		
		mailSender.send(message);
	}

}

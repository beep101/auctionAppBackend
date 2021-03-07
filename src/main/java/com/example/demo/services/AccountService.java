package com.example.demo.services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.User;
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

@Service
public class AccountService implements IAccountService{
	private UsersRepository usersRepo;
	private IHashUtil hashUtil;
	private IJwtUtil jwtUtil;
	private JavaMailSender mailSender;
	
	private String subject="Reset your Auction Purple password";
	private String content="Follow link below to set new password for your Auction purple account";
	private String link="http://localhost:8080/newPassword?token=";
	
	public AccountService(IHashUtil hashUtil,IJwtUtil jwtUtil, UsersRepository userRepo, JavaMailSender mailSender) {
		this.usersRepo=userRepo;
		this.hashUtil=hashUtil;
		this.jwtUtil=jwtUtil;
		this.mailSender=mailSender;
	}

	@Override
	public UserModel login(UserModel login) throws BadCredentialsException {
		List<User> users=usersRepo.findByEmail(login.getEmail());
		if(users.size()==1) {
			if(hashUtil.checkPassword(login.getPassword(), users.get(0).getPasswd())) {
				String jwt=jwtUtil.generateToken(users.get(0), new HashMap<String, Object>());
				login.setId(users.get(0).getId());
				login.setFirstName(users.get(0).getName());
				login.setLastName(users.get(0).getSurname());
				login.setJwt(jwt);
				return login;
			}else {
				throw new BadCredentialsException();
			}
		}else {
			throw new BadCredentialsException();
		}
	}

	@Override
	public UserModel signUp(UserModel signup)throws InvalidDataException,ExistingUserException,NonExistentUserException {
		if(signup.getEmail().isBlank()||signup.getPassword().isBlank()||signup.getFirstName().isBlank()||signup.getLastName().isBlank()) {
			throw new InvalidDataException();
		}
		if(usersRepo.findByEmail(signup.getEmail()).size()!=0) {
			throw new ExistingUserException();
		}
		User newUser=new User();
		newUser.setName(signup.getFirstName());
		newUser.setSurname(signup.getLastName());
		newUser.setEmail(signup.getEmail());
		newUser.setPasswd(hashUtil.hashPassword(signup.getPassword()));
		usersRepo.save(newUser);
		List<User> users=usersRepo.findByEmail(signup.getEmail());
		if(users.size()==1) {
			String jwt=jwtUtil.generateToken(users.get(0), new HashMap<String, Object>());
			signup.setJwt(jwt);
			return signup;
		}else {
			throw new NonExistentUserException();
		}
	}

	
	@Override
	public UserModel forgotPassword(UserModel forgotPassword) throws NonExistentUserException {
		if( forgotPassword.getEmail()==null||forgotPassword.getEmail().equals("")) {
			throw new NonExistentUserException();
		}
		List<User> users=usersRepo.findByEmail(forgotPassword.getEmail());
		if(users.size()==0) {
			throw new NonExistentUserException();
		}
		
		User user=users.get(0);
		String token=UUID.randomUUID().toString().replace("-","");
		user.setFpToken(token);
		user.setFpTokenEndtime(new Timestamp(System.currentTimeMillis()+24*60*60*1000));
		user=usersRepo.save(user);
		
		sendMail(user.getEmail(),user.getFpToken());
		
		return user.toModel();
	}
	
	@Override
	public UserModel newPassword(UserModel newPassword) throws InvalidTokenException,InvalidDataException {
		if(newPassword.getFpToken()==null || newPassword.getFpToken().equals("")) {
			throw new InvalidTokenException();
		}
		if(newPassword.getPassword()==null || newPassword.getPassword().equals("")) {
			throw new InvalidDataException();
		}
		
		List<User> users=usersRepo.findByFpTokenAndFpTokenEndtimeAfter(newPassword.getFpToken(),new Timestamp(System.currentTimeMillis()));
		if(users.size()==0) {
			throw new InvalidTokenException();
		}
		
		User user=users.get(0);
		user.setPasswd(hashUtil.hashPassword(newPassword.getPassword()));
		user.setFpTokenEndtime(new Timestamp(System.currentTimeMillis()));
		return usersRepo.save(user).toModel();
	}
	
	private void sendMail(String to,String token) {
		SimpleMailMessage message=new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content+"\n"+link+token);
		
		mailSender.send(message);
	}

}

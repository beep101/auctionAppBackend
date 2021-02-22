package com.example.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.User;
import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.ExistingUserException;
import com.example.demo.models.ForgotPasswordModel;
import com.example.demo.models.LoginModel;
import com.example.demo.models.SignupModel;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;

@Service
public class AccountService implements IAccountService{
	@Autowired
	UsersRepository usersRepo;
	@Autowired
	IHashUtil hashUtil;
	@Autowired
	IJwtUtil jwtUtil;
	
	public AccountService(IHashUtil hashUtil,IJwtUtil jwtUtil, UsersRepository userRepo) {
		this.usersRepo=userRepo;
		this.hashUtil=hashUtil;
		this.jwtUtil=jwtUtil;
	}

	@Override
	public String login(LoginModel login) {
		List<User> users=usersRepo.findByEmail(login.email);
		if(users.size()==1) {
			if(hashUtil.checkPassword(login.password, users.get(0).getPasswd())) {
				return jwtUtil.generateToken(users.get(0), new HashMap<String, Object>());
			}else {
				throw new BadCredentialsException();
			}
		}else {
			throw new BadCredentialsException();
		}
	}

	@Override
	public String signUp(SignupModel signup) {
		if(usersRepo.findByEmail(signup.email).size()!=0) {
			throw new ExistingUserException();
		}
		User newUser=new User();
		newUser.setName(signup.firstName);
		newUser.setSurname(signup.lastName);
		newUser.setEmail(signup.email);
		newUser.setPasswd(hashUtil.hashPassword(signup.password));
		usersRepo.save(newUser);
		LoginModel creds=new LoginModel();
		creds.email=signup.email;
		creds.password=signup.password;
		return login(creds);
	}

	@Override
	public void forgotPassword(ForgotPasswordModel forgotPassword) {
		// TODO Auto-generated method stub
		//throw unimplemented exception
	}

}

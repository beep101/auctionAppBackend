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
import com.example.demo.exceptions.InvalidDataException;
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
	
	public AccountService(IHashUtil hashUtil,IJwtUtil jwtUtil, UsersRepository userRepo) {
		this.usersRepo=userRepo;
		this.hashUtil=hashUtil;
		this.jwtUtil=jwtUtil;
	}

	@Override
	public UserModel login(UserModel login) throws BadCredentialsException {
		List<User> users=usersRepo.findByEmail(login.getEmail());
		if(users.size()==1) {
			if(hashUtil.checkPassword(login.getPassword(), users.get(0).getPasswd())) {
				String jwt=jwtUtil.generateToken(users.get(0), new HashMap<String, Object>());
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
	public UserModel forgotPassword(UserModel forgotPassword) {
		return null;
	}

}

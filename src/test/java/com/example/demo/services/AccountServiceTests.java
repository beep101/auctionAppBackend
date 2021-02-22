package com.example.demo.services;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.*;

import com.example.demo.entities.User;
import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.ExistingUserException;
import com.example.demo.exceptions.NonExistentUserException;
import com.example.demo.models.LoginModel;
import com.example.demo.models.SignupModel;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;

@RunWith(EasyMockRunner.class)
public class AccountServiceTests {
	
	@Mock
	IJwtUtil jwtUtilMock;
	@Mock
	IHashUtil hashUtilMock;
	@Mock
	UsersRepository usersRepoMock;
	
	@TestSubject
	AccountService accountControler=new AccountService(hashUtilMock,jwtUtilMock,usersRepoMock);
	
	//login tests
	@Test
	public void loginValidCredentialsShouldReturnMockedToken() throws Exception{
		LoginModel loginModel=new LoginModel();
		loginModel.email="rajko@mail.com";
		loginModel.password="rajko123";
		
		User userEntity=new User();
		userEntity.setEmail(loginModel.email);
		userEntity.setPasswd(loginModel.password);
		
		List<User> users=new ArrayList<>();
		users.add(userEntity);
		
		expect(hashUtilMock.checkPassword(loginModel.password,userEntity.getPasswd())).andReturn(true);
		expect(usersRepoMock.findByEmail(loginModel.email)).andReturn(users);
		expect(jwtUtilMock.generateToken(userEntity,new HashMap<String, Object>())).andReturn("fakeJWT");
		replay(hashUtilMock,usersRepoMock,jwtUtilMock);
		
		String jwtResult=accountControler.login(loginModel);
		
		assertEquals("fakeJWT", jwtResult);
	}
	
	@Test(expected = BadCredentialsException.class)
	public void loginBadPasswordShouldThrowException() throws Exception{
		LoginModel loginModel=new LoginModel();
		loginModel.email="rajko@mail.com";
		loginModel.password="rajko123";
		
		User userEntity=new User();
		userEntity.setEmail(loginModel.email);
		userEntity.setPasswd(loginModel.password);
		
		List<User> users=new ArrayList<>();
		users.add(userEntity);
		
		expect(hashUtilMock.checkPassword(loginModel.password,userEntity.getPasswd())).andReturn(false);
		expect(usersRepoMock.findByEmail(loginModel.email)).andReturn(users);
		expect(jwtUtilMock.generateToken(userEntity,new HashMap<String, Object>())).andReturn("fakeJWT");
		replay(hashUtilMock,usersRepoMock,jwtUtilMock);
		
		accountControler.login(loginModel);	
	}
	
	@Test(expected = BadCredentialsException.class)
	public void loginNonExistingUserShouldThrowException() throws Exception{
		LoginModel loginModel=new LoginModel();
		loginModel.email="rajko@mail.com";
		loginModel.password="rajko123";
		
		User userEntity=new User();
		userEntity.setEmail(loginModel.email);
		userEntity.setPasswd(loginModel.password);
		
		List<User> users=new ArrayList<>();
		
		expect(hashUtilMock.checkPassword(loginModel.password,userEntity.getPasswd())).andReturn(true);
		expect(usersRepoMock.findByEmail(loginModel.email)).andReturn(users);
		expect(jwtUtilMock.generateToken(userEntity,new HashMap<String, Object>())).andReturn("fakeJWT");
		replay(hashUtilMock,usersRepoMock,jwtUtilMock);
		
		accountControler.login(loginModel);	
	}
	
	@Test
	public void signupValidShouldReturnMockedToken() throws Exception{
		SignupModel signupModel=new SignupModel();
		signupModel.email="rajko@mail.com";
		signupModel.password="rajko123";
		signupModel.firstName="Rajko";
		signupModel.lastName="Pavlovic";
		
		List<User> users=new ArrayList<>();
		User user=new User();
		users.add(user);
		
		expect(usersRepoMock.findByEmail(signupModel.email)).andReturn(new ArrayList<User>()).once();
		expect(usersRepoMock.findByEmail(signupModel.email)).andReturn(users).once();
		expect(usersRepoMock.save(anyObject())).andReturn(null);
		expect(hashUtilMock.hashPassword(signupModel.password)).andReturn("qwerty");
		expect(jwtUtilMock.generateToken(user,new HashMap<String, Object>())).andReturn("fakeJWT");
		replay(hashUtilMock,usersRepoMock,jwtUtilMock);
		
		String jwtResult=accountControler.signUp(signupModel);	
		
		assertEquals("fakeJWT", jwtResult);
	}
	
	@Test(expected = ExistingUserException.class)
	public void signupExistingUserShouldThrowException() throws Exception{
		SignupModel signupModel=new SignupModel();
		signupModel.email="rajko@mail.com";
		List<User> users=new ArrayList<>();
		User user=new User();
		users.add(user);
		
		expect(usersRepoMock.findByEmail(signupModel.email)).andReturn(users);
		replay(usersRepoMock);
		
		accountControler.signUp(signupModel);	
	}
	
	@Test(expected = NonExistentUserException.class)
	public void signupCantWriteToDbShouldThrowException() throws Exception{
		SignupModel signupModel=new SignupModel();
		signupModel.email="rajko@mail.com";
		List<User> users=new ArrayList<>();
		
		expect(usersRepoMock.findByEmail(anyString())).andReturn(users).anyTimes();
		expect(usersRepoMock.save(anyObject())).andReturn(null);
		expect(hashUtilMock.hashPassword(signupModel.password)).andReturn("qwerty");
		replay(hashUtilMock,usersRepoMock);
		
		accountControler.signUp(signupModel);	
	}
}

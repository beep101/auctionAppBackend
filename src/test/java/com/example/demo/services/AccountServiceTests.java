package com.example.demo.services;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.*;

import com.example.demo.entities.User;
import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.ExistingUserException;
import com.example.demo.exceptions.NonExistentUserException;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;

@RunWith(EasyMockRunner.class)
public class AccountServiceTests extends EasyMockSupport {
	
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
		UserModel loginModel=new UserModel();
		loginModel.setEmail("rajko@mail.com");
		loginModel.setPassword("rajko123");
		
		User userEntity=new User();
		userEntity.setEmail(loginModel.getEmail());
		userEntity.setPasswd(loginModel.getPassword());
		
		List<User> users=new ArrayList<>();
		users.add(userEntity);
		
		expect(hashUtilMock.checkPassword(loginModel.getPassword(),userEntity.getPasswd())).andReturn(true);
		expect(usersRepoMock.findByEmail(loginModel.getEmail())).andReturn(users);
		expect(jwtUtilMock.generateToken(userEntity,new HashMap<String, Object>())).andReturn("fakeJWT");
		replayAll();
		
		UserModel result=accountControler.login(loginModel);
		
		assertEquals("fakeJWT", result.getJwt());
		
		verifyAll();
	}
	
	@Test(expected = BadCredentialsException.class)
	public void loginBadPasswordShouldThrowException() throws Exception{
		UserModel loginModel=new UserModel();
		loginModel.setEmail("rajko@mail.com");
		loginModel.setPassword("rajko123");
		
		User userEntity=new User();
		userEntity.setEmail(loginModel.getEmail());
		userEntity.setPasswd(loginModel.getPassword());
		
		List<User> users=new ArrayList<>();
		users.add(userEntity);
		
		expect(hashUtilMock.checkPassword(loginModel.getPassword(),userEntity.getPasswd())).andReturn(false);
		expect(usersRepoMock.findByEmail(loginModel.getEmail())).andReturn(users);
		expect(jwtUtilMock.generateToken(userEntity,new HashMap<String, Object>())).andReturn("fakeJWT");
		replayAll();
		
		accountControler.login(loginModel);	
		verifyAll();
	}
	
	@Test(expected = BadCredentialsException.class)
	public void loginNonExistingUserShouldThrowException() throws Exception{
		UserModel loginModel=new UserModel();
		loginModel.setEmail("rajko@mail.com");
		loginModel.setPassword("rajko123");
		
		User userEntity=new User();
		userEntity.setEmail(loginModel.getEmail());
		userEntity.setPasswd(loginModel.getPassword());
		
		List<User> users=new ArrayList<>();
		
		expect(hashUtilMock.checkPassword(loginModel.getPassword(),userEntity.getPasswd())).andReturn(true);
		expect(usersRepoMock.findByEmail(loginModel.getEmail())).andReturn(users);
		expect(jwtUtilMock.generateToken(userEntity,new HashMap<String, Object>())).andReturn("fakeJWT");
		replayAll();
		
		accountControler.login(loginModel);	
		verifyAll();
	}
	
	@Test
	public void signupValidShouldReturnMockedToken() throws Exception{
		UserModel signupModel=new UserModel();
		signupModel.setEmail("rajko@mail.com");
		signupModel.setPassword("rajko123");
		signupModel.setFirstName("Rajko");
		signupModel.setLastName("Pavlovic");
		
		List<User> users=new ArrayList<>();
		User user=new User();
		users.add(user);
		
		expect(usersRepoMock.findByEmail(signupModel.getEmail())).andReturn(new ArrayList<User>()).once();
		expect(usersRepoMock.findByEmail(signupModel.getEmail())).andReturn(users).once();
		expect(usersRepoMock.save(anyObject())).andReturn(null);
		expect(hashUtilMock.hashPassword(signupModel.getPassword())).andReturn("qwerty");
		expect(jwtUtilMock.generateToken(user,new HashMap<String, Object>())).andReturn("fakeJWT");
		replayAll();
		
		UserModel result=accountControler.signUp(signupModel);	
		
		assertEquals("fakeJWT", result.getJwt());
		verifyAll();
	}
	
	@Test(expected = ExistingUserException.class)
	public void signupExistingUserShouldThrowException() throws Exception{
		UserModel signupModel=new UserModel();
		signupModel.setEmail("rajko@mail.com");
		List<User> users=new ArrayList<>();
		User user=new User();
		users.add(user);
		
		expect(usersRepoMock.findByEmail(signupModel.getEmail())).andReturn(users);
		replayAll();
		
		accountControler.signUp(signupModel);	
		verifyAll();
	}
	
	@Test(expected = NonExistentUserException.class)
	public void signupCantWriteToDbShouldThrowException() throws Exception{
		UserModel signupModel=new UserModel();
		signupModel.setEmail("rajko@mail.com");
		signupModel.setPassword("rajko123");
		List<User> users=new ArrayList<>();
		
		expect(usersRepoMock.findByEmail(anyString())).andReturn(users).anyTimes();
		expect(usersRepoMock.save(anyObject())).andReturn(null);
		expect(hashUtilMock.hashPassword(signupModel.getPassword())).andReturn("qwerty");
		replayAll();
		
		accountControler.signUp(signupModel);	
		verifyAll();
	}
}

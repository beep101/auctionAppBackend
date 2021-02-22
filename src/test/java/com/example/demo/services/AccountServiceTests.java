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
import com.example.demo.models.LoginModel;
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
}

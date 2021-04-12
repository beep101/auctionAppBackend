package com.example.demo.services;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Optional;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.BadCredentialsException;
import com.example.demo.exceptions.ExistingUserException;
import com.example.demo.exceptions.NonExistentUserException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.InvalidTokenException;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.PayMethodRepository;
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
	@Mock
	AddressesRepository addressRepoMock;
	@Mock
	PayMethodRepository payMethodRepoMock;
	@Mock
	JavaMailSender mailSenderMock;
	
	String subject="Subject";
	String content="Contetn";
	String link="Link";
	
	
	@TestSubject
	AccountService accountService=new AccountService(hashUtilMock,jwtUtilMock,usersRepoMock,addressRepoMock,payMethodRepoMock,mailSenderMock,subject,content,link);
	
	//login tests
	@Test
	public void loginValidCredentialsShouldReturnMockedToken() throws Exception{
		UserModel loginModel=new UserModel();
		loginModel.setEmail("rajko@mail.com");
		loginModel.setPassword("rajko123");
		
		User userEntity=new User();
		userEntity.setEmail(loginModel.getEmail());
		userEntity.setPasswd(loginModel.getPassword());
		
		Optional<User> users=Optional.of(userEntity);
		
		expect(hashUtilMock.checkPassword(anyString(),anyString())).andReturn(true);
		expect(usersRepoMock.findByEmail(anyString())).andReturn(users);
		expect(jwtUtilMock.generateToken(anyObject(),anyObject())).andReturn("fakeJWT");
		replayAll();
		
		UserModel result=accountService.login(loginModel);
		
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
		
		Optional<User> users=Optional.of(userEntity);
		
		expect(hashUtilMock.checkPassword(loginModel.getPassword(),userEntity.getPasswd())).andReturn(false);
		expect(usersRepoMock.findByEmail(loginModel.getEmail())).andReturn(users);
		expect(jwtUtilMock.generateToken(userEntity,new HashMap<String, Object>())).andReturn("fakeJWT");
		replayAll();
		
		accountService.login(loginModel);	
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
		
		Optional<User> users=Optional.empty();
		
		expect(hashUtilMock.checkPassword(loginModel.getPassword(),userEntity.getPasswd())).andReturn(true);
		expect(usersRepoMock.findByEmail(loginModel.getEmail())).andReturn(users);
		expect(jwtUtilMock.generateToken(userEntity,new HashMap<String, Object>())).andReturn("fakeJWT");
		replayAll();
		
		accountService.login(loginModel);	
		verifyAll();
	}
	
	@Test
	public void signupValidShouldReturnMockedToken() throws Exception{
		UserModel signupModel=new UserModel();
		signupModel.setEmail("rajko@mail.com");
		signupModel.setPassword("rajko123");
		signupModel.setFirstName("Rajko");
		signupModel.setLastName("Pavlovic");
		
		User user=new User();
		Optional<User> users=Optional.of(user);
		
		expect(usersRepoMock.findByEmail(signupModel.getEmail())).andReturn(Optional.empty()).once();
		expect(usersRepoMock.findByEmail(signupModel.getEmail())).andReturn(users).once();
		expect(usersRepoMock.save(anyObject())).andReturn(null);
		expect(hashUtilMock.hashPassword(signupModel.getPassword())).andReturn("qwerty");
		expect(jwtUtilMock.generateToken(user,new HashMap<String, Object>())).andReturn("fakeJWT");
		replayAll();
		
		UserModel result=accountService.signUp(signupModel);	
		
		assertEquals("fakeJWT", result.getJwt());
		verifyAll();
	}
	
	@Test(expected = ExistingUserException.class)
	public void signupExistingUserShouldThrowException() throws Exception{
		UserModel signupModel=new UserModel();
		signupModel.setEmail("rajko@mail.com");
		signupModel.setPassword("rajko123");
		signupModel.setFirstName("Rajko");
		signupModel.setLastName("Pavlovic");

		Optional<User> users=Optional.of(new User());
		
		expect(usersRepoMock.findByEmail(signupModel.getEmail())).andReturn(users);
		replayAll();
		
		accountService.signUp(signupModel);	
		verifyAll();
	}
	
	@Test(expected = NonExistentUserException.class)
	public void signupCantWriteToDbShouldThrowException() throws Exception{
		UserModel signupModel=new UserModel();
		signupModel.setEmail("rajko@mail.com");
		signupModel.setPassword("rajko123");
		signupModel.setFirstName("Rajko");
		signupModel.setLastName("Pavlovic");
		
		expect(usersRepoMock.findByEmail(anyString())).andReturn(Optional.empty()).anyTimes();
		expect(usersRepoMock.save(anyObject())).andReturn(null);
		expect(hashUtilMock.hashPassword(signupModel.getPassword())).andReturn("qwerty");
		replayAll();
		
		accountService.signUp(signupModel);	
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void signupEmptyFieldsShouldThrowException() throws Exception{
		UserModel signupModel=new UserModel();
		signupModel.setEmail("");
		signupModel.setPassword("");
		signupModel.setFirstName("");
		signupModel.setLastName("");
		replayAll();
		
		accountService.signUp(signupModel);	
		verifyAll();		
	}
	
	@Test(expected=NonExistentUserException.class)
	public void forgotPasswordEmailIsNullShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setEmail(null);
		
		accountService.forgotPassword(model);
	}
	
	@Test(expected=NonExistentUserException.class)
	public void forgotPasswordEmailIsEmptyStringShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setEmail("");
		
		accountService.forgotPassword(model);
	}
	
	@Test(expected=NonExistentUserException.class)
	public void forgotPasswordNoUserInDbShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setEmail("user@mail.com");
		
		expect(usersRepoMock.findByEmail(anyString())).andReturn(Optional.empty()).anyTimes();
		replayAll();
		
		accountService.forgotPassword(model);
		
		verifyAll();
	}
	
	@Test
	public void forgotPasswordShouldSetTokenData() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setEmail("user@mail.com");
		
		User user=new User();
		user.setForgotPasswordToken(null);
		user.setForgotPasswordTokenEndTime(null);
		Optional<User> users=Optional.of(user);
		
		Capture<User> userCapture=EasyMock.newCapture(CaptureType.ALL);
		
		expect(usersRepoMock.findByEmail(anyString())).andReturn(users).anyTimes();
		expect(usersRepoMock.save(capture(userCapture))).andReturn(user).anyTimes();
		mailSenderMock.send(anyObject(SimpleMailMessage.class));
		expectLastCall();
		replayAll();
		
		accountService.forgotPassword(model);
		
		assertNotNull(userCapture.getValue().getForgotPasswordToken());
		assertNotNull(userCapture.getValue().getForgotPasswordTokenEndTime());
		
		verifyAll();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void newPasswordTokenIsNullShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setForgotPasswordToken(null);
		model.setPassword("123123");
		replayAll();
		
		accountService.newPassword(model);
		
		verifyAll();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void newPasswordTokenIsEmptyStringShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setForgotPasswordToken("");
		model.setPassword("123123");
		replayAll();
		
		accountService.newPassword(model);
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void newPasswordPasswordIsNullShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setForgotPasswordToken("a100d2");
		model.setPassword(null);
		replayAll();
		
		accountService.newPassword(model);
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void newPasswordPasswordEmptyShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setForgotPasswordToken("a100d2");
		model.setPassword("");
		replayAll();
		
		accountService.newPassword(model);
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void newPasswordPasswordShortShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setForgotPasswordToken("a100d2");
		model.setPassword("abc");
		replayAll();
		
		accountService.newPassword(model);
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void newPasswordPasswordIsEmptyStringShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setForgotPasswordToken("a100d2");
		model.setPassword("");
		replayAll();
		
		accountService.newPassword(model);
		
		verifyAll();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void newPasswordNoValidRecordInDbShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		model.setForgotPasswordToken("a100d2");
		model.setPassword("qwerty");
		
		expect(usersRepoMock.findByForgotPasswordTokenAndForgotPasswordTokenEndTimeAfter(anyString(),anyObject())).andReturn(Optional.empty()).anyTimes();
		replayAll();
		
		accountService.newPassword(model);
		
		verifyAll();
	}
	
	@Test
	public void newPasswordShouldSetPasswordAndFpTokenEndtime() throws AuctionAppException {
		UserModel model=new UserModel();
		String hash="abcdefg";
		model.setForgotPasswordToken("a100d2");
		model.setPassword("qwerty");
		
		User user=new User();
		user.setPasswd(null);
		user.setForgotPasswordTokenEndTime(null);
		Optional<User> users=Optional.of(user);
		
		Capture<User> userCapture=EasyMock.newCapture(CaptureType.ALL);
		
		expect(usersRepoMock.findByForgotPasswordTokenAndForgotPasswordTokenEndTimeAfter(anyString(),anyObject())).andReturn(users).anyTimes();
		expect(usersRepoMock.save(capture(userCapture))).andReturn(user).anyTimes();
		expect(hashUtilMock.hashPassword(model.getPassword())).andReturn(hash);
		replayAll();
		
		accountService.newPassword(model);
		
		assertEquals(userCapture.getValue().getPasswd(),hash);
		assertNotNull(userCapture.getValue().getForgotPasswordTokenEndTime());
		
		verifyAll();
	}
}

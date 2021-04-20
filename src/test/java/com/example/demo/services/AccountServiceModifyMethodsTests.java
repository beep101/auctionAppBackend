package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.Calendar;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mail.javamail.JavaMailSender;
import static org.easymock.EasyMock.*;

import com.example.demo.entities.Address;
import com.example.demo.entities.PayMethod;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.AddressModel;
import com.example.demo.models.PayMethodModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.PayMethodRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.services.interfaces.IImageStorageService;
import com.example.demo.utils.Gender;
import com.example.demo.utils.IHashUtil;
import com.example.demo.utils.IJwtUtil;

@RunWith(EasyMockRunner.class)
public class AccountServiceModifyMethodsTests  extends EasyMockSupport{
	@Mock
	IImageStorageService<UserModel> imageService;
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
	AccountService accountService=new AccountService(imageService,hashUtilMock,jwtUtilMock,usersRepoMock,addressRepoMock,payMethodRepoMock,mailSenderMock,subject,content,link);
	

	@Test
	public void testUpdateAccountHappyFlow() throws AuctionAppException {
		UserModel model=new UserModel();
		User entity=new User();
		
		model.setId(13);
		entity.setId(13);
		
		model.setEmail("rko@mail.com");
		model.setFirstName("Rko");
		model.setLastName("Rko");
		model.setGender(Gender.M);
		Calendar birthday=Calendar.getInstance();
		birthday.add(Calendar.YEAR, -20);
		model.setBirthday(new Timestamp(birthday.getTimeInMillis()));
		
		expect(usersRepoMock.save(anyObject())).andReturn(entity);
		replayAll();
		
		UserModel result=accountService.updateAccount(model, entity);
		
		assertEquals(model.getEmail(), result.getEmail());
		assertEquals(model.getFirstName(), result.getFirstName());
		assertEquals(model.getLastName(), result.getLastName());
		assertEquals(model.getBirthday(), result.getBirthday());
		assertEquals(model.getGender(), result.getGender());
		
		verifyAll();

	}

	@Test(expected = InvalidDataException.class)
	public void testUpdateAccountInvalidDataShouldThrowException() throws AuctionAppException {
		UserModel model=new UserModel();
		User entity=new User();
		
		model.setId(13);
		entity.setId(13);
		
		model.setEmail("rko@mail.com");
		model.setFirstName("Rko");
		model.setLastName("Rko");
		model.setGender(Gender.M);
		Calendar birthday=Calendar.getInstance();
		birthday.add(Calendar.YEAR, -5);
		model.setBirthday(new Timestamp(birthday.getTimeInMillis()));
		
		expect(usersRepoMock.save(anyObject())).andReturn(entity).anyTimes();
		replayAll();
		
		accountService.updateAccount(model, entity);
		
		birthday=Calendar.getInstance();
		birthday.add(Calendar.YEAR, -150);
		model.setBirthday(new Timestamp(birthday.getTimeInMillis()));
		accountService.updateAccount(model, entity);
		
		
		verifyAll();

	}
	
	@Test
	public void testAddAddressHappyFlow() throws AuctionAppException {
		AddressModel addressModel=new AddressModel();
		addressModel.setAddress("Street and Number");
		addressModel.setCity("City");
		addressModel.setZip("123123");
		addressModel.setCountry("Country");
		Address address=new Address();
		address.populate(addressModel);

		User user=new User();
		user.setAddress(address);
		
		expect(addressRepoMock.save(anyObject())).andReturn(address).anyTimes();
		expect(usersRepoMock.save(anyObject())).andReturn(user).anyTimes();
		replayAll();
		
		UserModel resultUser=accountService.addAddress(addressModel, user);
		AddressModel result=resultUser.getAddress();
		
		assertEquals(addressModel.getAddress(), result.getAddress());
		assertEquals(addressModel.getCity(), result.getCity());
		assertEquals(addressModel.getZip(), result.getZip());
		assertEquals(addressModel.getCountry(), result.getCountry());
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void testAddAddressInvalidDataShouldThrowException() throws AuctionAppException {
		AddressModel addressModel=new AddressModel();
		addressModel.setAddress("");
		addressModel.setCity("");
		addressModel.setZip("");
		addressModel.setCountry("");

		User user=new User();

		replayAll();
		
		accountService.addAddress(addressModel, user);
		
		
		verifyAll();
	}

	@Test(expected = InvalidDataException.class)
	public void testAddAddressBadSaveShouldThrowException() throws AuctionAppException {
		AddressModel addressModel=new AddressModel();
		addressModel.setAddress("Street and Number");
		addressModel.setCity("City");
		addressModel.setZip("123123");
		addressModel.setCountry("Country");
		Address address=new Address();
		address.setId(13);
		address.populate(addressModel);
		User user=new User();
		user.setAddress(address);
		
		expect(addressRepoMock.save(anyObject())).andReturn(address).anyTimes();
		expect(usersRepoMock.save(anyObject())).andReturn(new User()).anyTimes();
		addressRepoMock.delete(anyObject());
		expectLastCall().atLeastOnce();
		replayAll();
		
		accountService.addAddress(addressModel, user);
		
		verifyAll();
	}
	
	@Test
	public void testModAddressHappyFlow() throws AuctionAppException{
		AddressModel addressModel=new AddressModel();
		addressModel.setAddress("Street and Number");
		addressModel.setCity("City");
		addressModel.setZip("123123");
		addressModel.setCountry("Country");
		Address address=new Address();
		address.setId(13);
		User user=new User();
		user.setAddress(address);
		Address addressReturn=new Address();
		addressReturn.populate(addressModel);
		expect(addressRepoMock.save(anyObject())).andReturn(addressReturn).anyTimes();
		replayAll();
		
		UserModel resultUser=accountService.modAddress(addressModel, user);
		AddressModel result=resultUser.getAddress();
		
		assertEquals(addressModel.getAddress(), result.getAddress());
		assertEquals(addressModel.getCity(), result.getCity());
		assertEquals(addressModel.getZip(), result.getZip());
		assertEquals(addressModel.getCountry(), result.getCountry());
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void testModAddressIvalidDataThrowsException() throws AuctionAppException{
		AddressModel addressModel=new AddressModel();
		addressModel.setAddress("");
		addressModel.setCity("");
		addressModel.setZip("");
		addressModel.setCountry("");
		
		User user=new User();
		
		replayAll();
		
		accountService.modAddress(addressModel, user);

		
		verifyAll();
	}

	@Test(expected = UnallowedOperationException.class)
	public void testModAddressNonexistentAddressThrowsException() throws AuctionAppException{
		AddressModel addressModel=new AddressModel();
		addressModel.setAddress("Street and Number");
		addressModel.setCity("City");
		addressModel.setZip("123123");
		addressModel.setCountry("Country");
		
		User user=new User();
		
		replayAll();
		
		accountService.modAddress(addressModel, user);
		
		verifyAll();
	}
	
	@Test
	public void testAddPayMethodHappyFlow() throws AuctionAppException {
		PayMethodModel payModel=new PayMethodModel();
		payModel.setCardNumber("123123123");
		payModel.setCvccw("111");
		payModel.setExpMonth(6);
		payModel.setExpYear(2021);
		payModel.setOnCardName("Rko Rko");
		
		PayMethod payEntity=new PayMethod();
		payEntity.populate(payModel);

		User user=new User();
		user.setPayMethod(payEntity);
		
		expect(payMethodRepoMock.save(anyObject())).andReturn(payEntity).anyTimes();
		expect(usersRepoMock.save(anyObject())).andReturn(user).anyTimes();
		replayAll();
		
		UserModel resultUser=accountService.addPayMethod(payModel, user);
		PayMethodModel result=resultUser.getPayMethod();
		
		assertEquals(payModel.getCardNumber(), result.getCardNumber());
		assertEquals(payModel.getCvccw(), result.getCvccw());
		assertEquals(payModel.getExpMonth(), result.getExpMonth());
		assertEquals(payModel.getExpYear(), result.getExpYear());
		assertEquals(payModel.getOnCardName(), result.getOnCardName());
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void testAddPayMethodInvalidDataShouldThrowException() throws AuctionAppException {
		PayMethodModel payModel=new PayMethodModel();
		payModel.setCardNumber("");
		payModel.setCvccw("");
		payModel.setExpMonth(14);
		payModel.setExpYear(23);
		payModel.setOnCardName("");

		User user=new User();

		replayAll();
		
		accountService.addPayMethod(payModel, user);
		
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void testAddPayMethodBadSaveShouldThrowException() throws AuctionAppException {
		PayMethodModel payModel=new PayMethodModel();
		payModel.setCardNumber("123123123");
		payModel.setCvccw("111");
		payModel.setExpMonth(6);
		payModel.setExpYear(2021);
		payModel.setOnCardName("Rko Rko");
		
		PayMethod payEntity=new PayMethod();
		payEntity.setId(13);
		payEntity.populate(payModel);

		User user=new User();
		user.setPayMethod(payEntity);

		
		expect(payMethodRepoMock.save(anyObject())).andReturn(payEntity).anyTimes();
		expect(usersRepoMock.save(anyObject())).andReturn(new User()).anyTimes();
		payMethodRepoMock.delete(anyObject());
		expectLastCall().atLeastOnce();
		replayAll();
		
		accountService.addPayMethod(payModel, user);
		
		verifyAll();
	}
	
	@Test
	public void testModPayMethodHappyFlow() throws AuctionAppException{
		PayMethodModel payModel=new PayMethodModel();
		payModel.setCardNumber("123123123");
		payModel.setCvccw("111");
		payModel.setExpMonth(6);
		payModel.setExpYear(2021);
		payModel.setOnCardName("Rko Rko");
		
		PayMethod payEntity=new PayMethod();
		payEntity.setId(13);

		User user=new User();
		
		user.setPayMethod(payEntity);
		PayMethod payReturn=new PayMethod();
		payReturn.populate(payModel);
		
		expect(payMethodRepoMock.save(anyObject())).andReturn(payReturn).anyTimes();
		replayAll();
		
		UserModel resultUser=accountService.modPayMethod(payModel, user);
		PayMethodModel result=resultUser.getPayMethod();
		
		assertEquals(payModel.getCardNumber(), result.getCardNumber());
		assertEquals(payModel.getCvccw(), result.getCvccw());
		assertEquals(payModel.getExpMonth(), result.getExpMonth());
		assertEquals(payModel.getExpYear(), result.getExpYear());
		assertEquals(payModel.getOnCardName(), result.getOnCardName());
		
		
		verifyAll();
	}
	
	@Test(expected = InvalidDataException.class)
	public void testModPayMethodIvalidDataThrowsException() throws AuctionAppException{
		PayMethodModel payModel=new PayMethodModel();
		payModel.setCardNumber("");
		payModel.setCvccw("");
		payModel.setExpMonth(14);
		payModel.setExpYear(23);
		payModel.setOnCardName("");
		
		User user=new User();
		
		replayAll();
		
		accountService.modPayMethod(payModel, user);

		
		verifyAll();
	}

	@Test(expected = UnallowedOperationException.class)
	public void testModPayMethodNonexistentAddressThrowsException() throws AuctionAppException{
		PayMethodModel payModel=new PayMethodModel();
		payModel.setCardNumber("123123123");
		payModel.setCvccw("111");
		payModel.setExpMonth(6);
		payModel.setExpYear(2021);
		payModel.setOnCardName("Rko Rko");
		
		User user=new User();
		
		replayAll();
		
		accountService.modPayMethod(payModel, user);
		
		verifyAll();
	}
}

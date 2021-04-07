package com.example.demo.services;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entities.Address;
import com.example.demo.entities.Item;
import com.example.demo.entities.Subcategory;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.ImageHashException;
import com.example.demo.exceptions.ImageUploadException;
import com.example.demo.exceptions.InsertFailedException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.models.AddressModel;
import com.example.demo.models.ItemModel;
import com.example.demo.models.SubcategoryModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.SubcategoriesRepository;
import com.example.demo.services.interfaces.IImageStorageService;

@RunWith(EasyMockRunner.class)
public class ItemServiceAddItemTests extends EasyMockSupport{
	private static final int TWO_DAYS_MILIS=2*24*60*60*1000;
	
	@Mock
	ItemsRepository itemsRepoMock;
	@Mock
	CategoriesRepository categoriesRepoMock;
	@Mock
	IImageStorageService imageServiceMock;
	@Mock
	SubcategoriesRepository subcategoriesRepoMock;
	@Mock
	AddressesRepository addressesRepoMock;
	
	@TestSubject
	ItemService itemService=new ItemService(imageServiceMock,itemsRepoMock, categoriesRepoMock,subcategoriesRepoMock,addressesRepoMock);
	
	private ItemModel validItemModel(AddressModel address) {
		ItemModel model=new ItemModel();
		
		model.setName("name");
		model.setDescription("description");
		model.setStartingprice(new BigDecimal(1));
		model.setStarttime(new Timestamp(System.currentTimeMillis()));
		model.setEndtime(new Timestamp(System.currentTimeMillis()+TWO_DAYS_MILIS));
		model.setAddress(address);
		model.setSubcategory(new SubcategoryModel());
		model.setSeller(new UserModel());
		
		List<byte[]> imgs=new ArrayList<>();
		imgs.add(new byte[] {1,2,3,4,5});
		imgs.add(new byte[] {6,5,4,3,2,1,0});
		imgs.add(new byte[] {2,4,6});
		model.setImageFiles(imgs);
		
		return model;
	}
	
	private AddressModel validAddressModel() {
		AddressModel model=new AddressModel();
		
		model.setAddress("Street and Number");
		model.setCity("City");
		model.setZip("123123");
		model.setCountry("Country");
		
		return model;
	}
	
	private User validUser() {
		User user=new User();
		user.setId(1);
		Address address=new Address();
		address.setId(1);
		user.setAddress(address);
		return user;
	}
	
	
	@Test
	public void addItemInvalidSubcategoryShouldThrowException() throws  AuctionAppException {
		expect(subcategoriesRepoMock.findById(anyInt())).andReturn(Optional.empty()).anyTimes();
		replayAll();
		
		try {
			itemService.addItem(validItemModel(validAddressModel()), validUser());
		}catch(InvalidDataException ex) {
			assertTrue("Nonexistent subcategory".equals(ex.getErrors().get("subcategory")));
		}
		
		verifyAll();
	}
	
	@Test
	public void addItemInvalidModelShouldThrowException() throws  AuctionAppException {
		replayAll();
		
		try {
			itemService.addItem(new ItemModel(), validUser());
		}catch(InvalidDataException ex) {
			assertTrue(ex.getErrors().size()>1);
		}
		
		verifyAll();
	}
	
	@Test
	public void addItemNoneAddres() throws  AuctionAppException {
		expect(subcategoriesRepoMock.findById(anyInt())).andReturn(Optional.of(new Subcategory())).anyTimes();
		replayAll();
		
		User user=validUser();
		user.setAddress(null);
		
		try {
			itemService.addItem(validItemModel(null), user);
		}catch(InvalidDataException ex) {
			assertTrue("Address must be defined for item or seller".equals(ex.getErrors().get("address")));
		}
		
		verifyAll();
	}
	
	@Test
	public void addItemNonexistentAddress()throws  AuctionAppException {
		expect(subcategoriesRepoMock.findById(anyInt())).andReturn(Optional.of(new Subcategory())).anyTimes();
		expect(addressesRepoMock.findById(anyInt())).andReturn(Optional.empty()).anyTimes();
		replayAll();
		ItemModel model=validItemModel(validAddressModel());
		model.getAddress().setId(1);
		try {
			itemService.addItem(model, validUser());
		}catch(InvalidDataException ex) {
			assertTrue("Nonexistent address".equals(ex.getErrors().get("address")));
		}
		
		verifyAll();
	}
	
	@Test
	public void addItemItemWriteError() throws AuctionAppException {
		ItemModel itemModel=validItemModel(validAddressModel());
		expect(subcategoriesRepoMock.findById(anyInt())).andReturn(Optional.of(new Subcategory())).anyTimes();
		expect(addressesRepoMock.save(anyObject())).andReturn(new Address()).anyTimes();
		expect(itemsRepoMock.save(anyObject())).andReturn(new Item()).anyTimes();
		addressesRepoMock.delete(anyObject());
		expectLastCall().atLeastOnce();
		replayAll();
		
		try {
			itemService.addItem(itemModel, validUser());
		}catch(InvalidDataException ex) {
			assertTrue("Cannot save data".equals(ex.getErrors().get("save")));
		}
		
		verifyAll();
	}
	
	@Test(expected = InsertFailedException.class)
	public void addItemImagePutError() throws AuctionAppException {
		ItemModel itemModel=validItemModel(validAddressModel());
		expect(subcategoriesRepoMock.findById(anyInt())).andReturn(Optional.of(new Subcategory())).anyTimes();
		expect(addressesRepoMock.save(anyObject())).andReturn(new Address()).anyTimes();
		Item item=new Item();
		item.setId(1);
		expect(itemsRepoMock.save(anyObject())).andReturn(item).anyTimes();
		expect(imageServiceMock.addImages(anyString(),anyObject())).andThrow(new ImageUploadException());
		itemsRepoMock.delete(anyObject());
		expectLastCall().atLeastOnce();
		addressesRepoMock.delete(anyObject());
		expectLastCall().atLeastOnce();
		replayAll();
		
		itemService.addItem(itemModel, validUser());
		
		verifyAll();
	}
	
	@Test
	public void addItemHappyFlowNewAddress() throws AuctionAppException {
		Item item=new Item();
		item.setId(1);
		item.setAddress(new Address());
		item.setSeller(new User());
		
		ItemModel itemModel=validItemModel(validAddressModel());
		expect(subcategoriesRepoMock.findById(anyInt())).andReturn(Optional.of(new Subcategory())).anyTimes();
		expect(addressesRepoMock.save(anyObject())).andReturn(new Address()).anyTimes();
		expect(itemsRepoMock.save(anyObject())).andReturn(item).anyTimes();
		expect(imageServiceMock.addImages(anyString(),anyObject())).andReturn(new ArrayList<>());
		replayAll();
		
		ItemModel resItemModel=itemService.addItem(itemModel, validUser());
		assertEquals(item.toModel().getId(), resItemModel.getId());
		assertEquals(item.toModel().getName(), resItemModel.getName());
		assertEquals(item.toModel().getSeller().getId(), resItemModel.getSeller().getId());
		
		verifyAll();
	}
	
	@Test
	public void addItemHappyFlowUsersAddress() throws AuctionAppException {
		Item item=new Item();
		item.setId(1);
		item.setAddress(new Address());
		item.setSeller(new User());
		
		ItemModel itemModel=validItemModel(null);
		User user=validUser();
		Address address=new Address();
		address.setId(1);
		user.setAddress(address);
		
		expect(subcategoriesRepoMock.findById(anyInt())).andReturn(Optional.of(new Subcategory())).anyTimes();
		expect(addressesRepoMock.save(anyObject())).andReturn(new Address()).anyTimes();
		expect(itemsRepoMock.save(anyObject())).andReturn(item).anyTimes();
		expect(imageServiceMock.addImages(anyString(),anyObject())).andReturn(new ArrayList<>());
		replayAll();
		
		ItemModel resItemModel=itemService.addItem(itemModel,user );
		assertEquals(item.toModel().getId(), resItemModel.getId());
		assertEquals(item.toModel().getName(), resItemModel.getName());
		assertEquals(item.toModel().getSeller().getId(), resItemModel.getSeller().getId());
		
		verifyAll();
	}
}

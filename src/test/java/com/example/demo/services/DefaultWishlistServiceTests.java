package com.example.demo.services;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.entities.Subcategory;
import com.example.demo.entities.User;
import com.example.demo.entities.Wish;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.ItemModel;
import com.example.demo.models.WishModel;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.repositories.WishRepository;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(EasyMockRunner.class)
public class DefaultWishlistServiceTests extends EasyMockSupport{

	@Mock
	WishRepository wishesRepoMock;
	@Mock
	ItemsRepository itemsRepoMock;
	@Mock
	UsersRepository usersRepoMock;
	
	@TestSubject
	DefaultWishlistService wishService=new DefaultWishlistService(wishesRepoMock,itemsRepoMock,usersRepoMock);
	
	@Test(expected = UnallowedOperationException.class)
	public void testCreateWishOwnItemShouldThrowException() throws AuctionAppException {
		WishModel wish=new WishModel();
		wish.setItem(new ItemModel());
		User user=new User();
		user.setWishes(new ArrayList<Wish>());
		List<Item> items=new ArrayList<Item>();
		items.add(new Item());
		user.setItems(items);
		expect(usersRepoMock.findById(anyInt())).andReturn(Optional.of(user));
		replayAll();
		
		wishService.createWish(wish, user);
		
		verifyAll();		
	}
	
	@Test(expected = InvalidDataException.class)
	public void testCreateWishNonexistenItemShouldThrowException() throws AuctionAppException {
		WishModel wish=new WishModel();
		wish.setItem(new ItemModel());
		User user=new User();
		user.setWishes(new ArrayList<Wish>());
		List<Item> items=new ArrayList<Item>();
		user.setItems(items);
		
		expect(usersRepoMock.findById(anyInt())).andReturn(Optional.of(user));
		expect(itemsRepoMock.findById(anyInt())).andReturn(Optional.empty());
		replayAll();
		
		wishService.createWish(wish, user);
		
		verifyAll();		
	}
	
	@Test
	public void testCreateWishHappyFlow() throws AuctionAppException {
		int userId=13;
		int itemId=13;
		
		WishModel wish=new WishModel();
		
		ItemModel wishItem=new ItemModel();
		wishItem.setId(itemId);
		wish.setItem(wishItem);
		
		User user=new User();
		user.setId(userId);
		user.setItems(new ArrayList<Item>());
		user.setWishes(new ArrayList<Wish>());
		
		Item dbItem=new Item();
		dbItem.setId(itemId);
		dbItem.setSeller(new User());
		Subcategory sub=new Subcategory();
		sub.setCategory(new Category());
		dbItem.setSubcategory(sub);

		expect(usersRepoMock.findById(userId)).andReturn(Optional.of(user));
		expect(itemsRepoMock.findById(itemId)).andReturn(Optional.of(dbItem));
		Capture<Wish> wishCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(wishesRepoMock.save(capture(wishCapture))).andAnswer(new IAnswer<Wish>(){
		    public Wish answer() throws Throwable {
		        return wishCapture.getValue();
		    }
		}).anyTimes();
		replayAll();
		
		WishModel model=wishService.createWish(wish, user);
		
		assertEquals(itemId,model.getItem().getId());
		
		verifyAll();		
	}
	
	@Test
	public void testDeleteWishHappyFlow() throws AuctionAppException {
		int userId=13;
		
		WishModel wish=new WishModel();
		wish.setId(13);
		wish.setItem(new ItemModel());
		
		User user=new User();
		user.setId(userId);
		List<Wish> wishes=new ArrayList<>();
		Wish usersWish=new Wish();
		usersWish.setId(13);
		wishes.add(usersWish);
		user.setWishes(wishes);

		expect(usersRepoMock.findById(userId)).andReturn(Optional.of(user));
		wishesRepoMock.delete(anyObject());
		expectLastCall().atLeastOnce();
		replayAll();
		
		WishModel model=wishService.deleteWish(wish, user);
		
		assertEquals(wish.getId(),model.getId());
		
		verifyAll();		
	}

	@Test(expected = UnallowedOperationException.class)
	public void testDeleteWishOthersWishShouldThrowException() throws AuctionAppException {
		int userId=13;
		WishModel wish=new WishModel();
		wish.setId(13);
		wish.setItem(new ItemModel());
		
		User user=new User();
		user.setId(userId);
		user.setWishes(new ArrayList<Wish>());

		expect(usersRepoMock.findById(userId)).andReturn(Optional.of(user));
		replayAll();
		
		wishService.deleteWish(wish, user);
		
		verifyAll();		
	}


	@Test
	public void testGetWishes() throws AuctionAppException {
		int userId=13;
		User user=new User();
		user.setId(userId);
		List<Wish> wishes=new ArrayList<>();
		Wish usersWish=new Wish();
		usersWish.setId(13);
		Item wishItem=new Item();
		wishItem.setId(13);
		wishItem.setSeller(new User());
		Subcategory sub=new Subcategory();
		sub.setCategory(new Category());
		wishItem.setSubcategory(sub);
		usersWish.setItem(wishItem);
		wishes.add(usersWish);
		user.setWishes(wishes);

		expect(usersRepoMock.findById(userId)).andReturn(Optional.of(user));
		replayAll();
		
		List<WishModel> results= wishService.getWishes(user);
		WishModel result=results.get(0);
		assertEquals(13, result.getId());
		assertEquals(13, result.getItem().getId());
		
		verifyAll();		
	}
}

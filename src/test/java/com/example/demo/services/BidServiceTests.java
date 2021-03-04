package com.example.demo.services;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Pageable;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;
import com.example.demo.entities.User;
import com.example.demo.exceptions.BidAmountLowException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.BidModel;
import com.example.demo.models.ItemModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.BidsRepository;
import com.example.demo.repositories.ItemsRepository;

@RunWith(EasyMockRunner.class)
public class BidServiceTests extends EasyMockSupport{
	@Mock
	BidsRepository bidsRepo;
	@Mock
	ItemsRepository itemsRepo;
	
	@TestSubject
	BidService bidService=new BidService(bidsRepo,itemsRepo);
	
	@Test(expected = InvalidDataException.class)
	public void testAddBidUserBidMismatchShouldThrowException() throws InvalidDataException, BidAmountLowException, NotFoundException {
		int idUser=13;
		int idBidder=11;
		User user=new User();
		user.setId(idUser);
		BidModel model=new BidModel();
		UserModel userModel=new UserModel();
		userModel.setId(idBidder);
		model.setBidder(userModel);
		
		bidService.addBid(model, user);
	}
	
	@Test(expected = BidAmountLowException.class)
	public void testAddBidBidLowerThanCurrentMaxShouldThrowException() throws InvalidDataException, BidAmountLowException, NotFoundException {
		User user=new User();
		user.setId(13);
		BidModel model=new BidModel();
		UserModel userModel=new UserModel();
		userModel.setId(13);
		model.setBidder(userModel);
		ItemModel itemModel=new ItemModel();
		itemModel.setId(3);
		model.setItem(itemModel);
		model.setAmount(new BigDecimal(150));
		
		Item item=new Item();
		item.setId(0);
		item.setStartingprice(new BigDecimal(100));
		List<Bid> bidList=new ArrayList<Bid>();
		Bid bid=new Bid();
		bid.setAmount(new BigDecimal(200));
		bidList.add(bid);
		item.setBids(bidList);
		
		expect(itemsRepo.getOne(anyInt())).andReturn(item);
		replayAll();
		
		bidService.addBid(model, user);
		
		verifyAll();
	}
	
	@Test(expected = BidAmountLowException.class)
	public void testAddBidBidLowerThanBasePriceShouldThrowException() throws InvalidDataException, BidAmountLowException, NotFoundException {
		User user=new User();
		user.setId(13);
		BidModel model=new BidModel();
		UserModel userModel=new UserModel();
		userModel.setId(13);
		model.setBidder(userModel);
		ItemModel itemModel=new ItemModel();
		itemModel.setId(3);
		model.setItem(itemModel);
		model.setAmount(new BigDecimal(50));
		
		Item item=new Item();
		item.setId(0);
		item.setStartingprice(new BigDecimal(100));
		item.setBids(new ArrayList<>());
		
		expect(itemsRepo.getOne(anyInt())).andReturn(item);
		replayAll();
		
		bidService.addBid(model, user);
		
		verifyAll();
	}
	
	@Test(expected = NotFoundException.class)
	public void testAddBidBidItemNotPresentShouldThrowException() throws InvalidDataException, BidAmountLowException, NotFoundException {
		User user=new User();
		user.setId(13);
		BidModel model=new BidModel();
		UserModel userModel=new UserModel();
		userModel.setId(13);
		model.setBidder(userModel);
		ItemModel itemModel=new ItemModel();
		itemModel.setId(3);
		model.setItem(itemModel);
		
		expect(itemsRepo.getOne(anyInt())).andThrow(new EntityNotFoundException());
		replayAll();
		
		bidService.addBid(model, user);
		
		verifyAll();
	}
	
	@Test
	public void testGetBidsPageableCreation(){
		Capture<Pageable> pageableCapture=EasyMock.newCapture(CaptureType.ALL);
		
		expect(bidsRepo.findByItemEqualsOrderByIdDesc(anyObject(), capture(pageableCapture))).andReturn(new ArrayList<Bid>()).anyTimes();
		replayAll();
		
		bidService.getBids(1, null);
		assertTrue(pageableCapture.getValue().isUnpaged());
		
		pageableCapture.reset();
		
		Integer len=new Integer(13);
		bidService.getBids(1, len);
		assertEquals(0, pageableCapture.getValue().getPageNumber());
		assertEquals(len.intValue(), pageableCapture.getValue().getPageSize());
		
		verifyAll();
	}
	
	@Test
	public void testGetBidsEntityToModelValid(){
		List<Bid> entities=new ArrayList<>();
		Bid entity=new Bid();
		entity.setId(1);
		entity.setAmount(new BigDecimal(13));
		User bidder=new User();
		bidder.setId(1);
		entity.setBidder(bidder);
		entity.setTime(new Timestamp(123456));
		entities.add(entity);
		
		expect(bidsRepo.findByItemEqualsOrderByIdDesc(anyObject(), anyObject())).andReturn(entities).anyTimes();
		replayAll();
		
		Collection<BidModel> models=bidService.getBids(1, null);
		BidModel model=(BidModel) models.toArray()[0];
		assertEquals(model.getId(),entity.getId());
		assertEquals(model.getAmount(),entity.getAmount());
		assertEquals(model.getBidder().getId(),entity.getBidder().getId());
		assertEquals(model.getTime(),entity.getTime());
		
		models=bidService.getBids(1, 1);
		model=(BidModel) models.toArray()[0];
		assertEquals(model.getId(),entity.getId());
		assertEquals(model.getAmount(),entity.getAmount());
		assertEquals(model.getBidder().getId(),entity.getBidder().getId());
		assertEquals(model.getTime(),entity.getTime());
		
		verifyAll();
	}
}
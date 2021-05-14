package com.example.demo.services;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;
import com.example.demo.entities.Notification;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.interfaces.PushNotificationsService;

@RunWith(EasyMockRunner.class)
public class ScheduledSoldItemsCheckTests extends EasyMockSupport{
	
	@Mock
	private ItemsRepository itemsRepo;
	@Mock
	private PushNotificationsService pushNotificationsService;
	
	@TestSubject
	private ScheduledSoldItemsCheckComponent scheduledCheck;
	
	@Before
	public void setup() throws AuctionAppException {
		scheduledCheck=new ScheduledSoldItemsCheckComponent(itemsRepo, pushNotificationsService);
	}
	
	@Test
	public void testCheckAndNotifyNoSoldItems() {
		expect(itemsRepo.findBySoldFalseAndEndtimeBefore(anyObject())).andReturn(new ArrayList<Item>()).once();
		replayAll();

		scheduledCheck.checkAndNotify();
		
		verifyAll();
	}
	
	@Test
	public void testCheckAndNotifyOneSoldAndOneNot() {
		List<Item> items=new ArrayList<>();
		
		User seller=new User();
		User byer=new User();
		
		Item i1=new Item();
		i1.setId(1);
		i1.setName("item1");
		i1.setSeller(seller);
		
		List<Bid> bids1=new ArrayList<>();
		
		Bid bid1=new Bid();
		bid1.setAmount(new BigDecimal(10));
		bid1.setBidder(byer);
		bids1.add(bid1);
		
		i1.setBids(bids1);
		
		Item i2=new Item();
		i2.setId(2);
		i2.setName("item2");
		i2.setSeller(seller);
		
		List<Bid> bids2=new ArrayList<>();
		i2.setBids(bids2);

		items.add(i1);
		items.add(i2);
		
		Capture<List<Notification>> notificationsCapture=EasyMock.newCapture(CaptureType.ALL);
		Capture<List<Item>> itemsCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(itemsRepo.findBySoldFalseAndEndtimeBefore(anyObject())).andReturn(items).once();
		expect(itemsRepo.saveAll(capture(itemsCapture))).andAnswer(()->itemsCapture.getValue()).once();
		pushNotificationsService.sendMultipleNotifications(capture(notificationsCapture));
		expectLastCall().once();
		replayAll();

		scheduledCheck.checkAndNotify();
		assertEquals(3,notificationsCapture.getValue().size());
		assertArrayEquals(items.toArray(), itemsCapture.getValue().toArray());
		
		verifyAll();
	}
	
	@Test
	public void testCheckAndNotifyOneSold() {
		List<Item> items=new ArrayList<>();
		
		User seller=new User();
		User byer=new User();
		
		Item i1=new Item();
		i1.setId(1);
		i1.setName("item1");
		i1.setSeller(seller);
		
		List<Bid> bids1=new ArrayList<>();
		
		Bid bid1=new Bid();
		bid1.setAmount(new BigDecimal(10));
		bid1.setBidder(byer);
		bids1.add(bid1);
		
		Bid bid2=new Bid();
		bid2.setAmount(new BigDecimal(15));
		bid2.setBidder(byer);
		bids1.add(bid2);
		
		i1.setBids(bids1);

		items.add(i1);
		
		Capture<List<Notification>> notificationsCapture=EasyMock.newCapture(CaptureType.ALL);
		Capture<List<Item>> itemsCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(itemsRepo.findBySoldFalseAndEndtimeBefore(anyObject())).andReturn(items).once();
		expect(itemsRepo.saveAll(capture(itemsCapture))).andAnswer(()->itemsCapture.getValue()).once();
		pushNotificationsService.sendMultipleNotifications(capture(notificationsCapture));
		expectLastCall().once();
		replayAll();

		scheduledCheck.checkAndNotify();
		assertEquals(2,notificationsCapture.getValue().size());
		assertArrayEquals(items.toArray(), itemsCapture.getValue().toArray());
		
		verifyAll();
	}
}

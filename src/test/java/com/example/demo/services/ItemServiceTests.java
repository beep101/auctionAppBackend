package com.example.demo.services;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.entities.User;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.ItemModel;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;

@RunWith(EasyMockRunner.class)
public class ItemServiceTests extends EasyMockSupport{
	
	@Mock
	ItemsRepository itemsRepoMock;
	@Mock
	CategoriesRepository categoriesRepo;
	
	@TestSubject
	ItemService itemService=new ItemService(itemsRepoMock, categoriesRepo);
	
	@Test
	public void TestPagebleCreationShouldCreateValidPageableAllMethods() throws InvalidDataException {
		int page=7;
		int count=17;
		Pageable captured;
		
		List<Category> categories=new ArrayList<>();
		expect(categoriesRepo.findAllById(anyObject())).andReturn(categories).anyTimes();
		
		Capture<Pageable> pageableCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(itemsRepoMock.findAll(capture(pageableCapture))).andReturn(new PageImpl<Item>(new ArrayList<Item>())).anyTimes();
		expect(itemsRepoMock.findByCategory(anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfter(anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndCategoryEquals(anyObject(),anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndCategoryIn(anyObject(),anyString(),anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(anyObject(),anyString(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		replayAll();
		
		itemService.getItems(page,count);
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);
		
		pageableCapture.reset();
		
		itemService.getNewArrivalItems(page,count);
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.getLastChanceItems(page,count);
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.getItemsByCategory(0,page,count);
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.getActiveItems(page,count);
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.getActiveItemsByCategory(0,page,count);
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.findItemsValidFilterCategories("","",page,count);
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);


		pageableCapture.reset();
		
		itemService.findItemsValidFilterCategories("","1 2 3",page,count);
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		
		verifyAll();
	}
	
	@Test
	public void TestCategoryCreationShouldCreateValidCategoryAllMethods() {
		int categoryId=27;
		Category captured;
		
		Capture<Category> categoryCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(itemsRepoMock.findByCategory(capture(categoryCapture),anyObject())).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndCategoryEquals(anyObject(),capture(categoryCapture),anyObject())).andReturn(new ArrayList<Item>()).anyTimes();
		replayAll();
		
		itemService.getItemsByCategory(categoryId,0,1);
		captured=categoryCapture.getValue();
		assertEquals(captured.getId(), categoryId);

		categoryCapture.reset();
		
		itemService.getActiveItemsByCategory(categoryId,0,1);
		captured=categoryCapture.getValue();
		assertEquals(captured.getId(), categoryId);
		
		verifyAll();
	}
	
	@Test
	public void TestEntityToModelShouldCreateValidModelAllMethods() throws NotFoundException, InvalidDataException {
		Item item=new Item();
		
		List<Bid> bids=new ArrayList<>();
		Bid bid=new Bid();
		bid.setId(1);
		bid.setBidder(new User());
		bids.add(bid);
		
		User seller=new User();
		seller.setId(1);

		item.setId(1);
		item.setName("name");
		item.setDescription("description");
		item.setStartingprice(new BigDecimal(10));
		item.setSold(false);
		item.setStarttime(new Timestamp(123456));
		item.setEndtime(new Timestamp(123456));
		item.setBids(bids);
		item.setSeller(seller);
		
		List<Item> items=new ArrayList<>();
		Collection<ItemModel> models;
		ItemModel model;
		items.add(item);
		
		List<Category> categories=new ArrayList<>();
		expect(categoriesRepo.findAllById(anyObject())).andReturn(categories).anyTimes();
		
		expect(itemsRepoMock.findAll(isA(Pageable.class))).andReturn(new PageImpl<Item>(items)).anyTimes();
		expect(itemsRepoMock.getOne(anyInt())).andReturn(item).anyTimes();
		expect(itemsRepoMock.findByCategory(anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfter(anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndCategoryEquals(anyObject(),anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndCategoryIn(anyObject(),anyString(),anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(anyObject(),anyString(),anyObject())).andReturn(items).anyTimes();
		replayAll();
		
		models=itemService.getItems(0,1);
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		model=itemService.getItem(item.getId());
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());

		
		models=itemService.getNewArrivalItems(0,1);
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getLastChanceItems(0,1);
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getItemsByCategory(0,0,1);
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getActiveItems(0,1);
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getActiveItemsByCategory(0,0,1);
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.findItemsValidFilterCategories("","",0,1);
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.findItemsValidFilterCategories("","1 2 3",0,1);
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getBids().get(0).getId(), item.getBids().get(0).getId());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		verifyAll();
	}
	
	@Test(expected = NotFoundException.class)
	public void getItemNonExistentShouldThrowException() throws NotFoundException {
		expect(itemsRepoMock.getOne(anyInt())).andThrow(new EntityNotFoundException());
		replayAll();
		
		itemService.getItem(1);
		
		verifyAll();
	}
	
	@Test
	public void findItemsValidFilterCategoriesParseCategoriesValidStringShouldCallItemsRepo() throws InvalidDataException {
		String cats1="4";
		List<Integer> catList1=new ArrayList<Integer>();
		catList1.add(4);
		
		String cats2="4 6 8";
		List<Integer> catList2=new ArrayList<Integer>();
		catList2.add(4);
		catList2.add(6);
		catList2.add(8);
		
		Capture<List<Integer>> catsIdsCapture=EasyMock.newCapture(CaptureType.ALL);
		
		List<Category> categories=new ArrayList<>();
		expect(categoriesRepo.findAllById(capture(catsIdsCapture))).andReturn(categories).anyTimes();
		
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndCategoryIn(anyObject(),anyString(),anyObject(),anyObject())).andReturn(new ArrayList<Item>()).anyTimes();
		replayAll();
		
		List<Integer> captured;
		
		itemService.findItemsValidFilterCategories("",cats1,0,1);
		captured=catsIdsCapture.getValue();
		assertEquals(captured, catList1);

		catsIdsCapture.reset();
		
		itemService.findItemsValidFilterCategories("",cats2,0,1);
		captured=catsIdsCapture.getValue();
		assertEquals(captured, catList2);
		
		verifyAll();
	}
	
	@Test
	public void findItemsValidFilterCategoriesParseCategoriesInvalidStringShouldThrowException() {
		String cats1="4.45 3.14 2,66";		
		String cats2="4.538,32";		
		String cats3="4,5,8,32";		
		String cats4="4 a 5 re 3 2";
			
		replayAll();
		
		assertThrows(InvalidDataException.class,()->{itemService.findItemsValidFilterCategories("",cats1,0,1);});
		assertThrows(InvalidDataException.class,()->{itemService.findItemsValidFilterCategories("",cats2,0,1);});
		assertThrows(InvalidDataException.class,()->{itemService.findItemsValidFilterCategories("",cats3,0,1);});
		assertThrows(InvalidDataException.class,()->{itemService.findItemsValidFilterCategories("",cats4,0,1);});

		verifyAll();		
	}
}

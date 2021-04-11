package com.example.demo.services;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.entities.PriceCountAggregateResult;
import com.example.demo.entities.Subcategory;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.HistogramResponseModel;
import com.example.demo.models.ItemModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.SubcategoriesRepository;
import com.example.demo.services.interfaces.IImageStorageService;
import com.example.demo.utils.ItemSorting;
import com.example.demo.utils.PaginationParams;
import com.example.demo.utils.SortingPaginationParams;
import com.example.demo.validations.FilterItemsRequest;

@RunWith(EasyMockRunner.class)
public class ItemServiceTests extends EasyMockSupport{
	
	@Mock
	ItemsRepository itemsRepoMock;
	@Mock
	CategoriesRepository categoriesRepo;
	@Mock
	IImageStorageService imageService;
	@Mock
	SubcategoriesRepository subcategoriesRepo;
	@Mock
	AddressesRepository addressesRepo;
	
	@TestSubject
	ItemService itemService=new ItemService(imageService,itemsRepoMock, categoriesRepo,subcategoriesRepo,addressesRepo);
	
	@Test
	public void testPagebleCreationShouldCreateValidPageableAllMethods() throws AuctionAppException {

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
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndSubcategoryCategoryIn(anyObject(),anyString(),anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(anyObject(),anyString(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(categoriesRepo.findAll()).andReturn(new ArrayList<Category>()).anyTimes();
		expect(subcategoriesRepo.findAll()).andReturn(new ArrayList<Subcategory>()).anyTimes();
		expect(subcategoriesRepo.findAllById(anyObject())).andReturn(new ArrayList<Subcategory>()).anyTimes();
		expect(itemsRepoMock.searchActiveByCatsAndSubsFilterMinAndMaxPrice(anyObject(),anyString(),anyObject(),anyObject(),anyObject(),anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.searchActiveByCatsAndSubsFilterMinPrice(anyObject(),anyString(),anyObject(),anyObject(),anyObject(),capture(pageableCapture))).andReturn(new ArrayList<Item>()).anyTimes();
		replayAll();
		
		itemService.getItems(new PaginationParams(page,count));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);
		
		pageableCapture.reset();
		
		itemService.getNewArrivalItems(new PaginationParams(page,count));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.getLastChanceItems(new PaginationParams(page,count));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.getItemsByCategory(0,new PaginationParams(page,count));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.getActiveItems(new PaginationParams(page,count));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.getActiveItemsByCategory(0,new PaginationParams(page,count));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);

		pageableCapture.reset();
		
		itemService.findItemsValidFilterCategories("",new ArrayList<Integer>(),new SortingPaginationParams(page,count,ItemSorting.DEFAULT));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);
		assertEquals(captured.getSort().get().collect(Collectors.toList()).get(0).getProperty(), "name");
		assertTrue(captured.getSort().get().collect(Collectors.toList()).get(0).isAscending());

		pageableCapture.reset();
		
		itemService.findItemsValidFilterCategories("",new ArrayList<Integer>(Arrays.asList(new Integer[]{1,2,3})),new SortingPaginationParams(page,count,ItemSorting.DEFAULT));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);
		assertEquals(captured.getSort().get().collect(Collectors.toList()).get(0).getProperty(), "name");
		assertTrue(captured.getSort().get().collect(Collectors.toList()).get(0).isAscending());

		pageableCapture.reset();
		
		itemService.findItemsValidFilterCategories("",new ArrayList<Integer>(Arrays.asList(new Integer[]{1,2,3})),new SortingPaginationParams(page,count,ItemSorting.NEWNESS));
		captured=pageableCapture.getValue();
		assertEquals(captured.getSort().get().collect(Collectors.toList()).get(0).getProperty(), "starttime");
		assertTrue(captured.getSort().get().collect(Collectors.toList()).get(0).isDescending());
		
		pageableCapture.reset();
		
		itemService.findItemsValidFilterCategories("",new ArrayList<Integer>(Arrays.asList(new Integer[]{1,2,3})),new SortingPaginationParams(page,count,ItemSorting.PRICEDESC));
		captured=pageableCapture.getValue();
		assertEquals(captured.getSort().get().collect(Collectors.toList()).get(0).getProperty(), "startingprice");
		assertTrue(captured.getSort().get().collect(Collectors.toList()).get(0).isDescending());
		
		pageableCapture.reset();
		
		itemService.findItemsValidFilterCategories("",new ArrayList<Integer>(Arrays.asList(new Integer[]{1,2,3})),new SortingPaginationParams(page,count,ItemSorting.PRICEASC));
		captured=pageableCapture.getValue();
		assertEquals(captured.getSort().get().collect(Collectors.toList()).get(0).getProperty(), "startingprice");
		assertTrue(captured.getSort().get().collect(Collectors.toList()).get(0).isAscending());
		
		pageableCapture.reset();
		
		FilterItemsRequest filter=new FilterItemsRequest("",new ArrayList<Integer>(),new ArrayList<Integer>(), null, null);
		itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(filter, new SortingPaginationParams(page,count,ItemSorting.PRICEASC));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);
		assertEquals(captured.getSort().get().collect(Collectors.toList()).get(0).getProperty(), "startingprice");
		assertTrue(captured.getSort().get().collect(Collectors.toList()).get(0).isAscending());
		
		pageableCapture.reset();
		
		filter=new FilterItemsRequest("",new ArrayList<Integer>(),new ArrayList<Integer>(), null, new BigDecimal("77.7"));
		itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(filter,new SortingPaginationParams(page,count,ItemSorting.PRICEASC));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);
		assertEquals(captured.getSort().get().collect(Collectors.toList()).get(0).getProperty(), "startingprice");
		assertTrue(captured.getSort().get().collect(Collectors.toList()).get(0).isAscending());

		pageableCapture.reset();
		
		List<Integer> ids=new ArrayList<>();
		ids.add(1);ids.add(2);ids.add(3);
		filter=new FilterItemsRequest("",ids,ids, null, new BigDecimal("77.7"));
		itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(filter,new SortingPaginationParams(page,count,ItemSorting.PRICEASC));
		captured=pageableCapture.getValue();
		assertEquals(captured.getPageNumber(), page);
		assertEquals(captured.getPageSize(), count);
		assertEquals(captured.getSort().get().collect(Collectors.toList()).get(0).getProperty(), "startingprice");
		assertTrue(captured.getSort().get().collect(Collectors.toList()).get(0).isAscending());
		
		verifyAll();
	}
	
	@Test
	public void testCategoryCreationShouldCreateValidCategoryAllMethods() {

		int categoryId=27;
		Category captured;
		
		Capture<Category> categoryCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(itemsRepoMock.findByCategory(capture(categoryCapture),anyObject())).andReturn(new ArrayList<Item>()).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndCategoryEquals(anyObject(),capture(categoryCapture),anyObject())).andReturn(new ArrayList<Item>()).anyTimes();
		replayAll();
		
		itemService.getItemsByCategory(categoryId,new PaginationParams(0,1));
		captured=categoryCapture.getValue();
		assertEquals(captured.getId(), categoryId);

		categoryCapture.reset();
		
		itemService.getActiveItemsByCategory(categoryId,new PaginationParams(0,1));
		captured=categoryCapture.getValue();
		assertEquals(captured.getId(), categoryId);
		
		verifyAll();
	}
	
	@Test
	public void testEntityToModelShouldCreateValidModelAllMethods() throws AuctionAppException{

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
		
		Optional<Item> itemOpt=Optional.of(item);
		
		List<Category> categories=new ArrayList<>();
		expect(categoriesRepo.findAllById(anyObject())).andReturn(categories).anyTimes();
		
		expect(itemsRepoMock.findAll(isA(Pageable.class))).andReturn(new PageImpl<Item>(items)).anyTimes();
		expect(itemsRepoMock.getOne(anyInt())).andReturn(item).anyTimes();
		expect(itemsRepoMock.findByCategory(anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfter(anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndCategoryEquals(anyObject(),anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndSubcategoryCategoryIn(anyObject(),anyString(),anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(anyObject(),anyString(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterRandom(anyObject())).andReturn(itemOpt).anyTimes();
		expect(categoriesRepo.findAll()).andReturn(new ArrayList<Category>()).anyTimes();
		expect(subcategoriesRepo.findAll()).andReturn(new ArrayList<Subcategory>()).anyTimes();
		expect(subcategoriesRepo.findAllById(anyObject())).andReturn(new ArrayList<Subcategory>()).anyTimes();
		expect(itemsRepoMock.searchActiveByCatsAndSubsFilterMinAndMaxPrice(anyObject(),anyString(),anyObject(),anyObject(),anyObject(),anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.searchActiveByCatsAndSubsFilterMinPrice(anyObject(),anyString(),anyObject(),anyObject(),anyObject(),anyObject())).andReturn(items).anyTimes();
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterAndSellerEquals(anyObject(),anyObject())).andReturn(items);
		expect(itemsRepoMock.findBySoldTrueOrEndtimeBeforeAndSellerEquals(anyObject(),anyObject())).andReturn(items);
		expect(itemsRepoMock.findAllBiddedItemsForUser(anyObject())).andReturn(items);
		replayAll();
		
		models=itemService.getItems(new PaginationParams(0,1));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		model=itemService.getItem(item.getId());
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());

		
		models=itemService.getNewArrivalItems(new PaginationParams(0,1));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getLastChanceItems(new PaginationParams(0,1));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getItemsByCategory(0,new PaginationParams(0,1));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getActiveItems(new PaginationParams(0,1));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getActiveItemsByCategory(0,new PaginationParams(0,1));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.findItemsValidFilterCategories("",new ArrayList<Integer>(),new SortingPaginationParams(0,1,ItemSorting.DEFAULT));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.findItemsValidFilterCategories("",new ArrayList<Integer>(Arrays.asList(new Integer[]{1,2,3})),new SortingPaginationParams(0,1,ItemSorting.DEFAULT));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		model=itemService.getItemFeatured();
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		FilterItemsRequest filter=new FilterItemsRequest("",new ArrayList<Integer>(),new ArrayList<Integer>(), null, null);
		itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(filter,new SortingPaginationParams(0,1,ItemSorting.DEFAULT));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		filter=new FilterItemsRequest("",new ArrayList<Integer>(),new ArrayList<Integer>(), null, new BigDecimal("77.7"));
		itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(filter,new SortingPaginationParams(0,1,ItemSorting.DEFAULT));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		filter=new FilterItemsRequest("",new ArrayList<Integer>(),new ArrayList<Integer>(), null, null);
		itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(filter,new SortingPaginationParams(0,1,ItemSorting.DEFAULT));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		List<Integer> ids=new ArrayList<>();
		ids.add(1);ids.add(2);ids.add(3);
		filter=new FilterItemsRequest("",ids,ids, null, new BigDecimal("77.7"));
		itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(filter,new SortingPaginationParams(0,1,ItemSorting.DEFAULT));
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getActiveItemsForUser(new User());
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getInactiveItemsForUser(new User());
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		
		models=itemService.getBiddedItemsForUser(new User());
		model=(ItemModel)models.toArray()[0];
		assertEquals(model.getId(), item.getId());
		assertEquals(model.getName(), item.getName());
		assertEquals(model.getDescription(), item.getDescription());
		assertEquals(model.getStartingprice(), item.getStartingprice());
		assertEquals(model.getSold(), item.getSold());
		assertEquals(model.getStarttime(), item.getStarttime());
		assertEquals(model.getEndtime(), item.getEndtime());
		assertEquals(model.getSeller().getId(), item.getSeller().getId());
		verifyAll();
	}
	
	@Test(expected = NotFoundException.class)
	public void getItemNonExistentShouldThrowException() throws AuctionAppException {
		expect(itemsRepoMock.getOne(anyInt())).andThrow(new EntityNotFoundException());
		replayAll();
		
		itemService.getItem(1);
		
		verifyAll();
	}
	
	@Test(expected = NotFoundException.class)
	public void getItemFeaturedNoItemShouldThrowException() throws AuctionAppException{
		expect(itemsRepoMock.findBySoldFalseAndEndtimeAfterRandom(anyObject())).andReturn(Optional.empty());
		replayAll();
		
		itemService.getItemFeatured();
		
		verifyAll();
	}

	@Test(expected = NotFoundException.class)
	public void pricesHistogramForItemsNoDataThrowsException() throws AuctionAppException{
		expect(itemsRepoMock.groupByPricesOrdered(anyObject())).andReturn(new ArrayList<>()).once();
		replayAll();
		
		itemService.pricesHistogramForItems();
		
		verifyAll();
	}
	
	@Test
	public void pricesHistogramForItemsCreateValidHistogram() throws AuctionAppException {
		List<PriceCountAggregateResult> data=new ArrayList<>();
		data.add(new PriceCountAggregateResult(new BigDecimal(2), 2));
		data.add(new PriceCountAggregateResult(new BigDecimal(4), 4));
		data.add(new PriceCountAggregateResult(new BigDecimal(6), 7));
		data.add(new PriceCountAggregateResult(new BigDecimal(8), 5));
		data.add(new PriceCountAggregateResult(new BigDecimal(10), 3));
		data.add(new PriceCountAggregateResult(new BigDecimal(12), 2));
		expect(itemsRepoMock.groupByPricesOrdered(anyObject())).andReturn(data).once();
		replayAll();
		
		HistogramResponseModel hr=itemService.pricesHistogramForItems();
		assertEquals(new BigDecimal(2), hr.getMin());
		assertEquals(new BigDecimal(12), hr.getMax());
		assertEquals(24, hr.getHistogram().size());
		
		verifyAll();
	}
	
	
}

package com.example.demo.controllers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.InsertFailedException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnauthenticatedException;
import com.example.demo.models.HistogramResponseModel;
import com.example.demo.models.ItemModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.SubcategoriesRepository;
import com.example.demo.services.ImageStorageS3;
import com.example.demo.services.ItemService;
import com.example.demo.services.interfaces.IImageStorageService;
import com.example.demo.services.interfaces.IItemService;
import com.example.demo.utils.AwsS3Adapter;
import com.example.demo.utils.ItemSorting;
import com.example.demo.utils.PaginationParams;
import com.example.demo.utils.SortingPaginationParams;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Items Controller", tags= {"Items Controller"}, description = "Enables users to fetch items, all endpoints require paging parameters, page number and items count")
@RestController
public class ItemController {
	
	@Value("${s3.id}")
	private String id;
	@Value("${s3.key}")
	private String key;
	@Value("${s3.itemImageBucketUrl}")
	private String imageBucketBaseUrl;
	@Value("${s3.bucketName}")
	private String bucketName;
	
	@Autowired
	private ItemsRepository itemsRepo;
	@Autowired
	private CategoriesRepository categoriesRepo;
	@Autowired
	private SubcategoriesRepository subcategoriesRepo;
	@Autowired
	private AddressesRepository addressesRepo;
	
	private IItemService itemService;
	private IImageStorageService imageService;
	
	@PostConstruct
	public void init() {
		imageService=new ImageStorageS3(bucketName,imageBucketBaseUrl,new AwsS3Adapter(id, key));
		itemService=new ItemService(imageService,itemsRepo,categoriesRepo,subcategoriesRepo,addressesRepo);
	}
	
	@ApiOperation(value = "Returns item specified by ID", notes = "Public access")
	@GetMapping("/api/items/{itemId}")
	public ItemModel getItemById(@PathVariable(name="itemId")int itemId) throws AuctionAppException{
		return imageService.loadImagesForItem(itemService.getItem(itemId));
	}
	
	@ApiOperation(value = "Returns random item", notes = "Public access")
	@GetMapping("/api/items/featured")
	public ItemModel getItemFeatured() throws AuctionAppException{
		return imageService.loadImagesForItem(itemService.getItemFeatured());
	}
	
	@ApiOperation(value = "Returns all active items", notes = "Public access")
	@GetMapping("/api/items")
	public Collection<ItemModel> getItems(@RequestParam int page,@RequestParam int count) {

		return imageService.loadImagesForItems(itemService.getActiveItems(new PaginationParams(page,count)));
	}
	
	@ApiOperation(value = "Returns items that will expire soon", notes = "Public access")
	@GetMapping("/api/items/lastChance")
	public Collection<ItemModel> getLstChance(@RequestParam int page,@RequestParam int count) {
		return imageService.loadImagesForItems(itemService.getLastChanceItems(new PaginationParams(page,count)));
	}
	
	@ApiOperation(value = "Return most recently added items", notes = "Public access")
	@GetMapping("/api/items/newArrivals")
	public Collection<ItemModel> getNewArrivals(@RequestParam int page,@RequestParam int count) {
		return imageService.loadImagesForItems(itemService.getNewArrivalItems(new PaginationParams(page,count)));
	}
	
	@ApiOperation(value = "Returns items from specified category", notes = "Pubic access")
	@GetMapping("/api/items/category/{categoryId}")
	public Collection<ItemModel> getByCategory(@PathVariable(name="categoryId")int categoryId,@RequestParam int page,@RequestParam int count) {
		return imageService.loadImagesForItems(itemService.getActiveItemsByCategory(categoryId,new PaginationParams(page,count)));
	}
	
	@ApiOperation(value = "Enables searching items by name and filtering by different parameters", notes = "Public access")
	@GetMapping("/api/items/search")
	public Collection<ItemModel> findItem(@RequestParam String term,@RequestParam List<Integer> categories, @RequestParam List<Integer> subcategories,
										  @RequestParam(required = false) BigDecimal minPrice,
										  @RequestParam(required = false) BigDecimal maxPrice,
										  @RequestParam int page,@RequestParam int count,
										  @RequestParam(required = false, defaultValue = "default") ItemSorting sort)throws AuctionAppException{
		return imageService.loadImagesForItems(itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(term,categories,subcategories,minPrice,maxPrice,new SortingPaginationParams(page,count,sort)));
	}
	
	@ApiOperation(value = "Creating new item for sale", notes = "Only authenticated users")
	@PostMapping("/api/items")
	public ItemModel addItem(@RequestBody ItemModel model)  throws AuctionAppException{
		User principal=null;
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		return imageService.loadImagesForItem(itemService.addItem(model,principal));
	}
	
	@ApiOperation(value = "Returns prices histogram histogram", notes = "Pubic access")
	@GetMapping("/api/items/priceHistogram")
	public HistogramResponseModel getPriceHistogram() throws AuctionAppException{
		return itemService.pricesHistogramForItems();
	}
	
}

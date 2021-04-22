package com.example.demo.controllers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AuthUser;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.UnauthenticatedException;
import com.example.demo.models.HistogramResponseModel;
import com.example.demo.models.ItemModel;
import com.example.demo.models.SearchModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.SubcategoriesRepository;
import com.example.demo.services.S3ImageStorageService;
import com.example.demo.services.DefaultItemService;
import com.example.demo.services.DefaultSearchSuggestionService;
import com.example.demo.services.interfaces.ImageStorageService;
import com.example.demo.services.interfaces.ItemService;
import com.example.demo.services.interfaces.SearchSuggestionService;
import com.example.demo.utils.AwsS3Adapter;
import com.example.demo.utils.ItemSorting;
import com.example.demo.utils.PaginationParams;
import com.example.demo.utils.SortingPaginationParams;
import com.example.demo.validations.FilterItemsRequest;

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
	
	private ItemService itemService;
	private ImageStorageService<ItemModel> imageService;
	private SearchSuggestionService searchService;
	
	@PostConstruct
	public void init() {
		imageService=new S3ImageStorageService<ItemModel>(bucketName,imageBucketBaseUrl,new AwsS3Adapter(id, key));
		searchService=new DefaultSearchSuggestionService(itemsRepo);
		itemService=new DefaultItemService(imageService,searchService,itemsRepo,categoriesRepo,subcategoriesRepo,addressesRepo);
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
	public SearchModel findItem(@RequestParam String term,
										  @RequestParam List<Integer> categories,
										  @RequestParam List<Integer> subcategories,
										  @RequestParam(required = false) BigDecimal minPrice,
										  @RequestParam(required = false) BigDecimal maxPrice,
										  @RequestParam int page,@RequestParam int count,
										  @RequestParam(required = false, defaultValue = "default") ItemSorting sort)throws AuctionAppException{
		FilterItemsRequest request=new FilterItemsRequest(term, categories, subcategories, minPrice, maxPrice);
		SearchModel model=itemService.findItemsValidFilterCategoriesSubcaetgoriesPrice(request,new SortingPaginationParams(page,count,sort));
		model.setItems(imageService.loadImagesForItems(model.getItems()));
		return model;
	}
	
	@ApiOperation(value = "Creating new item for sale", notes = "Only authenticated users")
	@PostMapping("/api/items")
	public ItemModel addItem(@RequestBody ItemModel model,@AuthUser User principal)  throws AuctionAppException{
		return imageService.loadImagesForItem(itemService.addItem(model,principal));
	}
	
	@ApiOperation(value = "Returns prices histogram histogram", notes = "Pubic access")
	@GetMapping("/api/items/priceHistogram")
	public HistogramResponseModel getPriceHistogram() throws AuctionAppException{
		return itemService.pricesHistogramForItems();
	}

	@ApiOperation(value = "Returns all active items of currently authenticated user", notes = "Only authenticated users")
	@GetMapping("/api/items/active")
	public Collection<ItemModel> getActiveItemsForUser(@AuthUser User principal) throws UnauthenticatedException{
		return imageService.loadImagesForItems(itemService.getActiveItemsForUser(principal));
	}

	@ApiOperation(value = "Returns all inactive items of currently authenticated user", notes = "Only authenticated users")
	@GetMapping("/api/items/inactive")
	public Collection<ItemModel> getInactiveItemsForUser(@AuthUser User principal) throws UnauthenticatedException{
		return imageService.loadImagesForItems(itemService.getInactiveItemsForUser(principal));
	}

	@ApiOperation(value = "Returns all bidded by currently authenticated user", notes = "Only authenticated users")
	@GetMapping("/api/items/bidded")
	public Collection<ItemModel> getBiddedItemsForUser(@AuthUser User principal) throws UnauthenticatedException{
		return imageService.loadImagesForItems(itemService.getBiddedItemsForUser(principal));
	}
}

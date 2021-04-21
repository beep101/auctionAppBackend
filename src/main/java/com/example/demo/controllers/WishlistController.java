package com.example.demo.controllers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.UnauthenticatedException;
import com.example.demo.models.ItemModel;
import com.example.demo.models.WishModel;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.repositories.WishRepository;
import com.example.demo.services.ImageStorageS3;
import com.example.demo.services.WishService;
import com.example.demo.services.interfaces.IImageStorageService;
import com.example.demo.services.interfaces.IWishService;
import com.example.demo.utils.AwsS3Adapter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Wishlist Controller", tags = { "Wishlist Controller" }, description = "Manages wishlist related data")
@RestController
public class WishlistController {
	
	@Value("${s3.id}")
	private String id;
	@Value("${s3.key}")
	private String key;
	@Value("${s3.itemImageBucketUrl}")
	private String imageBucketBaseUrl;
	@Value("${s3.bucketName}")
	private String bucketName;
	
	private IImageStorageService<ItemModel> imageService;
	
	@Autowired
	WishRepository wishesRepo;
	@Autowired
	ItemsRepository itemsRepo;
	@Autowired
	UsersRepository usersRepo;
	
	IWishService wishesService;
	
	@PostConstruct
	public void init() {
		imageService=new ImageStorageS3<>(bucketName, imageBucketBaseUrl, new AwsS3Adapter(id, key));
		wishesService=new WishService(wishesRepo, itemsRepo,usersRepo);
	}
	
	@ApiOperation(value = "Adds wish to user's wishlist", notes = "Only authenticated users")
	@PostMapping("/api/wishlist")
	public WishModel addWish( @RequestBody WishModel wish) throws AuctionAppException{
		User principal=null;
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		System.out.println(wish.getItem().getId());
		return wishesService.createWish(wish,principal);
	}
	
	@ApiOperation(value = "Removes wish from user's wishlist", notes = "Only authenticated users")
	@DeleteMapping("/api/wishlist")
	public WishModel removeWish( @RequestParam int wishId) throws AuctionAppException{
		User principal=null;
		WishModel wish=new WishModel();
		wish.setId(wishId);
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		return wishesService.deleteWish(wish,principal);
	}
	
	@ApiOperation(value = "Get all wishes for user", notes = "Only authenticated users")
	@GetMapping("/api/wishlist")
	public Collection<WishModel> getAllWishes() throws AuctionAppException{
		User principal=null;
		try {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(ClassCastException ex) {
			throw new UnauthenticatedException();
		}
		List<WishModel> wishes=wishesService.getWishes(principal);
		wishes=wishes.stream().map(x->{x.setItem(imageService.loadImagesForItem(x.getItem()));return x;}).collect(Collectors.toList());
		return wishes;
	}
	
}

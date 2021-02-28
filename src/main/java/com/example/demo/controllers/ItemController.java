package com.example.demo.controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ItemModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.ItemService;
import com.example.demo.services.interfaces.IItemService;

@RestController
public class ItemController {
	@Autowired
	ItemsRepository itemsRepo;
	
	IItemService itemService;
	
	@PostConstruct
	public void init() {
		itemService=new ItemService(itemsRepo);
	}
	
	@GetMapping("/api/items/{page}/{count}")
	public Collection<ItemModel> getItems(@PathVariable(name="page")int page,@PathVariable(name="count")int count) {
		return itemService.getActiveItems(page,count);
	}
	
	@GetMapping("/api/items/lastChance/{page}/{count}")
	public Collection<ItemModel> getLstChance(@PathVariable(name="page")int page,@PathVariable(name="count")int count) {
		return itemService.getLastChanceItems(page,count);
	}
	
	@GetMapping("/api/items/newArrivals/{page}/{count}")
	public Collection<ItemModel> getNewArrivals(@PathVariable(name="page")int page,@PathVariable(name="count")int count) {
		return itemService.getNewArrivalItems(page,count);
	}
	
	@GetMapping("/api/items/category/{categoryId}/{page}/{count}")
	public Collection<ItemModel> getByCategory(@PathVariable(name="categoryId")int categoryId,@PathVariable(name="page")int page,@PathVariable(name="count")int count) {
		return itemService.getActiveItemsByCategory(categoryId,page,count);
	}
}

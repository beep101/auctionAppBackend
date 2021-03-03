package com.example.demo.controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ItemModel;
import com.example.demo.models.UserModel;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.ItemService;
import com.example.demo.services.interfaces.IItemService;

@RestController
public class ItemController {
	@Autowired
	private ItemsRepository itemsRepo;
	
	private IItemService itemService;
	
	@PostConstruct
	public void init() {
		itemService=new ItemService(itemsRepo);
	}
	
	@GetMapping("/api/items/byId/{itemId}")
	public ItemModel getItemById(@PathVariable(name="itemId")int itemId) {
		return itemService.getItem(itemId);
	}
	
	@GetMapping("/api/items")
	public Collection<ItemModel> getItems(@RequestParam int page,@RequestParam int count) {

		return itemService.getActiveItems(page,count);
	}
	
	@GetMapping("/api/items/lastChance")
	public Collection<ItemModel> getLstChance(@RequestParam int page,@RequestParam int count) {
		return itemService.getLastChanceItems(page,count);
	}
	
	@GetMapping("/api/items/newArrivals")
	public Collection<ItemModel> getNewArrivals(@RequestParam int page,@RequestParam int count) {
		return itemService.getNewArrivalItems(page,count);
	}
	
	@GetMapping("/api/items/category/{categoryId}")
	public Collection<ItemModel> getByCategory(@PathVariable(name="categoryId")int categoryId,@RequestParam int page,@RequestParam int count) {
		return itemService.getActiveItemsByCategory(categoryId,page,count);
	}
}

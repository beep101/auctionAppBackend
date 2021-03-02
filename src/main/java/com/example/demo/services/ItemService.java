package com.example.demo.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.ItemModel;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.interfaces.IItemService;

public class ItemService implements IItemService {
	
	private ItemsRepository itemsRepo;
	private CategoriesRepository categoriesRepo;
	
	public ItemService(ItemsRepository itemsRepo,CategoriesRepository categoriesRepo) {
		this.itemsRepo=itemsRepo;
		this.categoriesRepo=categoriesRepo;
	}

	@Override
	public ItemModel getItem(int id) {
		try {
			return ItemModel.fromItemEntity(itemsRepo.getOne(id));
		}catch(EntityNotFoundException ex) {
			throw new NotFoundException();
		}
	}

	@Override
	public ItemModel addItem(ItemModel item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemModel modItem(ItemModel item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemModel delItem(ItemModel item) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<ItemModel> getItems(int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Collection<ItemModel> items=itemsRepo.findAll(pgbl).stream().map(x->ItemModel.fromItemEntity(x)).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> getNewArrivalItems(int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		List<Item> items=itemsRepo.findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(crr,pgbl);
		List<ItemModel> itemModels=items.stream().map(x->ItemModel.fromItemEntity(x)).collect(Collectors.toList());
		return itemModels;
	}

	@Override
	public Collection<ItemModel> getLastChanceItems(int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Timestamp crr=new Timestamp(System.currentTimeMillis()+3*60*1000);
		List<Item> items=itemsRepo.findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(crr,pgbl);
		List<ItemModel> itemModels=items.stream().map(x->ItemModel.fromItemEntity(x)).collect(Collectors.toList());
		return itemModels;
	}

	@Override
	public Collection<ItemModel> getItemsByCategory(int categoryId, int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Category category=new Category();
		category.setId(categoryId);
		Collection<ItemModel> items=itemsRepo.findByCategory(category,pgbl).stream().map(x->ItemModel.fromItemEntity(x)).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> getActiveItems(int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		Collection<ItemModel> items=itemsRepo.findBySoldFalseAndEndtimeAfter(crr,pgbl).stream().map(x->ItemModel.fromItemEntity(x)).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> getActiveItemsByCategory(int categoryId, int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Category category=new Category();
		category.setId(categoryId);
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		Collection<ItemModel> items=itemsRepo.findBySoldFalseAndEndtimeAfterAndCategoryEquals(crr,category,pgbl).stream().map(x->ItemModel.fromItemEntity(x)).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> findItemsValidFilterCategories(String term,String categories, int page, int count){
		Pageable pgbl=PageRequest.of(page, count);
		Timestamp crr=new Timestamp(System.currentTimeMillis());

		List<Category> categoriesList=null;
		try {
			if(!categories.equals("")) {
				List<Integer> categoriesIds=Arrays.stream(categories.split(" ")).map(x->Integer.parseInt(x)).collect(Collectors.toList());
				categoriesList=categoriesRepo.findAllById(categoriesIds);
			}
		}catch(NumberFormatException ex) {
			throw new InvalidDataException();
		}
		Collection<ItemModel> items;
		if(categoriesList!=null) {
			items=itemsRepo.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndCategoryIn(crr,term,categoriesList,pgbl)
					.stream().map(x->ItemModel.fromItemEntity(x)).collect(Collectors.toList());
		}else{
			items=itemsRepo.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(crr,term,pgbl)
					.stream().map(x->ItemModel.fromItemEntity(x)).collect(Collectors.toList());
		}
		return items;
	}
	
	

}

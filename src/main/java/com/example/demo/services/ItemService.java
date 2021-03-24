package com.example.demo.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.ItemModel;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.interfaces.IItemService;
import com.example.demo.utils.ItemSorting;
import com.example.demo.utils.PaginationParams;

public class ItemService implements IItemService {
	
	private ItemsRepository itemsRepo;
	private CategoriesRepository categoriesRepo;
	
	public ItemService(ItemsRepository itemsRepo,CategoriesRepository categoriesRepo) {
		this.itemsRepo=itemsRepo;
		this.categoriesRepo=categoriesRepo;
	}

	@Override
	public ItemModel getItem(int id) throws NotFoundException{
		try {
			return itemsRepo.getOne(id).toModel();
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
	public Collection<ItemModel> getItems(PaginationParams pgbl) {
		Collection<ItemModel> items=itemsRepo.findAll(pgbl.getPageable()).stream().map(x->x.toModel()).collect(Collectors.toList());
		return items;
	}
	
	@Override
	public ItemModel getItemFeatured() throws NotFoundException {
		Optional<Item> itemOpt=itemsRepo.findBySoldFalseAndEndtimeAfterRandom(new Timestamp(System.currentTimeMillis()));
		if(itemOpt.isPresent()) {
			return itemOpt.get().toModel();
		}
		throw new NotFoundException();
	}

	@Override
	public Collection<ItemModel> getNewArrivalItems(PaginationParams pgbl) {
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		List<Item> items=itemsRepo.findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(crr,pgbl.getPageable());
		List<ItemModel> itemModels=items.stream().map(x->x.toModel()).collect(Collectors.toList());
		return itemModels;
	}

	@Override
	public Collection<ItemModel> getLastChanceItems(PaginationParams pgbl) {
		Timestamp crr=new Timestamp(System.currentTimeMillis()+3*60*1000);
		List<Item> items=itemsRepo.findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(crr,pgbl.getPageable());
		List<ItemModel> itemModels=items.stream().map(x->x.toModel()).collect(Collectors.toList());
		return itemModels;
	}

	@Override
	public Collection<ItemModel> getItemsByCategory(int categoryId, PaginationParams pgbl) {
		Category category=new Category();
		category.setId(categoryId);
		Collection<ItemModel> items=itemsRepo.findByCategory(category,pgbl.getPageable()).stream().map(x->x.toModel()).collect(Collectors.toList());
		
		return items;
	}

	@Override
	public Collection<ItemModel> getActiveItems(PaginationParams pgbl) {
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		Collection<ItemModel> items=itemsRepo.findBySoldFalseAndEndtimeAfter(crr,pgbl.getPageable()).stream().map(x->x.toModel()).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> getActiveItemsByCategory(int categoryId, PaginationParams pgbl) {
		Category category=new Category();
		category.setId(categoryId);
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		Collection<ItemModel> items=itemsRepo.findBySoldFalseAndEndtimeAfterAndCategoryEquals(crr,category,pgbl.getPageable()).stream().map(x->x.toModel()).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> findItemsValidFilterCategories(String term,List<Integer> categories, PaginationParams pgbl) throws InvalidDataException{
		Timestamp crr=new Timestamp(System.currentTimeMillis());

		List<Category> categoriesList=null;
		try {
			if(categories.size()!=0) {
				categoriesList=categoriesRepo.findAllById(categories);
			}
		}catch(NumberFormatException ex) {
			throw new InvalidDataException();
		}
		
		
		Collection<ItemModel> items;
		if(categoriesList!=null) {
			items=itemsRepo.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndCategoryIn(crr,term,categoriesList,pgbl.getPageable())
					.stream().map(x->x.toModel()).collect(Collectors.toList());
		}else{
			items=itemsRepo.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(crr,term,pgbl.getPageable())
					.stream().map(x->x.toModel()).collect(Collectors.toList());
		}
		return items;
	}	
}

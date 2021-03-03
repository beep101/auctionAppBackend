package com.example.demo.services;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.ItemModel;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.interfaces.IItemService;

public class ItemService implements IItemService {
	
	private ItemsRepository itemsRepo;
	
	public ItemService(ItemsRepository itemsRepo) {
		this.itemsRepo=itemsRepo;
	}

	@Override
	public ItemModel getItem(int id) {
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
	public Collection<ItemModel> getItems(int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Collection<ItemModel> items=itemsRepo.findAll(pgbl).stream().map(x->x.toModel()).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> getNewArrivalItems(int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		List<Item> items=itemsRepo.findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(crr,pgbl);
		List<ItemModel> itemModels=items.stream().map(x->x.toModel()).collect(Collectors.toList());
		return itemModels;
	}

	@Override
	public Collection<ItemModel> getLastChanceItems(int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Timestamp crr=new Timestamp(System.currentTimeMillis()+3*60*1000);
		List<Item> items=itemsRepo.findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(crr,pgbl);
		List<ItemModel> itemModels=items.stream().map(x->x.toModel()).collect(Collectors.toList());
		return itemModels;
	}

	@Override
	public Collection<ItemModel> getItemsByCategory(int categoryId, int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Category category=new Category();
		category.setId(categoryId);
		Collection<ItemModel> items=itemsRepo.findByCategory(category,pgbl).stream().map(x->x.toModel()).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> getActiveItems(int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		Collection<ItemModel> items=itemsRepo.findBySoldFalseAndEndtimeAfter(crr,pgbl).stream().map(x->x.toModel()).collect(Collectors.toList());
		return items;
	}

	@Override
	public Collection<ItemModel> getActiveItemsByCategory(int categoryId, int page, int count) {
		Pageable pgbl=PageRequest.of(page, count);
		Category category=new Category();
		category.setId(categoryId);
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		Collection<ItemModel> items=itemsRepo.findBySoldFalseAndCategoryEqualsAndEndtimeAfter(category,crr,pgbl).stream().map(x->x.toModel()).collect(Collectors.toList());
		return items;
	}

}

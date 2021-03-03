package com.example.demo.services.interfaces;

import java.util.Collection;

import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.ItemModel;

public interface IItemService {
	ItemModel getItem(int id) throws NotFoundException;
	ItemModel addItem(ItemModel item);
	ItemModel modItem(ItemModel item);
	ItemModel delItem(ItemModel item);
	
	Collection<ItemModel> getItems(int page,int count);
	Collection<ItemModel> getActiveItems(int page,int count);
	Collection<ItemModel> getItemsByCategory(int categoryId, int page, int count);
	Collection<ItemModel> getActiveItemsByCategory(int categoryId, int page, int count);
	Collection<ItemModel> getNewArrivalItems(int page, int count);
	Collection<ItemModel> getLastChanceItems(int page, int count);
}

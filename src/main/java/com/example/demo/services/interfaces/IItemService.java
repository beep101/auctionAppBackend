package com.example.demo.services.interfaces;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.InsertFailedException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.HistogramResponseModel;
import com.example.demo.models.ItemModel;
import com.example.demo.utils.ItemSorting;
import com.example.demo.utils.PaginationParams;

public interface IItemService {
	ItemModel getItem(int id) throws AuctionAppException;
	ItemModel addItem(ItemModel item,User user) throws AuctionAppException;
	ItemModel modItem(ItemModel item);
	ItemModel delItem(ItemModel item);
	
	Collection<ItemModel> getItems(PaginationParams pgbl);
	Collection<ItemModel> getActiveItems(PaginationParams pgbl);
	Collection<ItemModel> getItemsByCategory(int categoryId, PaginationParams pgbl);
	Collection<ItemModel> getActiveItemsByCategory(int categoryId, PaginationParams pgbl);
	Collection<ItemModel> getNewArrivalItems(PaginationParams pgbl);
	Collection<ItemModel> getLastChanceItems(PaginationParams pgbl);
	Collection<ItemModel> findItemsValidFilterCategories(String term, List<Integer> categories, PaginationParams pgbl)throws AuctionAppException;
	Collection<ItemModel> findItemsValidFilterCategoriesSubcaetgoriesPrice(String term,List<Integer> categories, List<Integer> subcategories,
														 BigDecimal minPrice, BigDecimal maxPrice, PaginationParams pgbl) throws AuctionAppException;

	
	ItemModel getItemFeatured() throws AuctionAppException;
	HistogramResponseModel pricesHistogramForItems()throws AuctionAppException;
}

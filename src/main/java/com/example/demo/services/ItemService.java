package com.example.demo.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Address;
import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.entities.Subcategory;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ImageHashException;
import com.example.demo.exceptions.ImageUploadException;
import com.example.demo.exceptions.InsertFailedException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.ItemModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.SubcategoriesRepository;
import com.example.demo.services.interfaces.IImageStorageService;
import com.example.demo.services.interfaces.IItemService;
import com.example.demo.utils.PaginationParams;
import com.example.demo.validations.ItemRequest;

public class ItemService implements IItemService {
	
	private ItemsRepository itemsRepo;
	private CategoriesRepository categoriesRepo;
	private IImageStorageService imageService;
	private SubcategoriesRepository subcateogriesRepo;
	private AddressesRepository addressesRepo;
	
	public ItemService(IImageStorageService imageService,ItemsRepository itemsRepo, CategoriesRepository categoriesRepo,
			           SubcategoriesRepository subcateogriesRepo,AddressesRepository addressesRepo) {
		this.itemsRepo=itemsRepo;
		this.categoriesRepo=categoriesRepo;
		this.imageService=imageService;
		this.subcateogriesRepo=subcateogriesRepo;
		this.addressesRepo=addressesRepo;
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
	public ItemModel addItem(ItemModel itemModel,User user) throws InvalidDataException, InsertFailedException {
		Map<String,String> problems=(new ItemRequest(itemModel)).validate();
		if(!problems.isEmpty()) {
			throw new InvalidDataException(problems);
		}
		
		itemModel.setSold(false);
		itemModel.getSeller().setId(user.getId());
		Item item=new Item();
		item.populate(itemModel);
		
		Optional<Subcategory> subcatOpt=subcateogriesRepo.findById(item.getSubcategory().getId());
		if(subcatOpt.isEmpty()){
			problems.clear();
			problems.put("subcategory", "Nonexistent subcategory");
			throw new InvalidDataException(problems);
		}
		
		boolean newAddress=false;
		if(item.getAddress()==null) {
			if(user.getAddress()!=null) {
				item.setAddress(user.getAddress());
			}else {
				problems.clear();
				problems.put("address", "Address must be defined for item or seller");
				throw new InvalidDataException(problems);
			}
		}else if(item.getAddress().getId()!=0) {
			Optional<Address> addressOpt=addressesRepo.findById(item.getAddress().getId());
			if(addressOpt.isEmpty()) {
				problems.clear();
				problems.put("address", "Nonexistent address");
				throw new InvalidDataException(problems);
			}
			newAddress=true;
		}else {
			item.setAddress(addressesRepo.save(item.getAddress()));
		}
		
		item=itemsRepo.save(item);
		if(item.getId()==0) {
			problems.clear();
			problems.put("save","Cannot save data");
			if(newAddress) {
				addressesRepo.delete(item.getAddress());
			}
			throw new InvalidDataException(problems);
		}
		
		try {
			imageService.addImages(Integer.toString(item.getId()), itemModel.getImageFiles());
		} catch (ImageUploadException | ImageHashException e) {
			itemsRepo.delete(item);
			if(newAddress) {
				addressesRepo.delete(item.getAddress());
			}
			throw new InsertFailedException();
		}
		return item.toModel();
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
			items=itemsRepo.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndSubcategoryCategoryIn(crr,term,categoriesList,pgbl.getPageable())
					.stream().map(x->x.toModel()).collect(Collectors.toList());
		}else{
			items=itemsRepo.findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(crr,term,pgbl.getPageable())
					.stream().map(x->x.toModel()).collect(Collectors.toList());
		}
		return items;
	}	
}

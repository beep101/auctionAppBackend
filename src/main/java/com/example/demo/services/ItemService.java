package com.example.demo.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entities.Address;
import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.entities.PriceCountAggregateResult;
import com.example.demo.entities.Subcategory;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.ImageHashException;
import com.example.demo.exceptions.ImageUploadException;
import com.example.demo.exceptions.InsertFailedException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.HistogramResponseModel;
import com.example.demo.models.ItemModel;
import com.example.demo.repositories.AddressesRepository;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.SubcategoriesRepository;
import com.example.demo.services.interfaces.IImageStorageService;
import com.example.demo.services.interfaces.IItemService;
import com.example.demo.utils.PaginationParams;
import com.example.demo.validations.FilterItemsRequest;
import com.example.demo.validations.ItemRequest;

public class ItemService implements IItemService {
	
	private ItemsRepository itemsRepo;
	private CategoriesRepository categoriesRepo;
	private IImageStorageService imageService;
	private SubcategoriesRepository subcateogriesRepo;
	private AddressesRepository addressesRepo;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public ItemService(IImageStorageService imageService,ItemsRepository itemsRepo, CategoriesRepository categoriesRepo,
			           SubcategoriesRepository subcateogriesRepo,AddressesRepository addressesRepo) {
		this.itemsRepo=itemsRepo;
		this.categoriesRepo=categoriesRepo;
		this.imageService=imageService;
		this.subcateogriesRepo=subcateogriesRepo;
		this.addressesRepo=addressesRepo;
	}

	@Override
	public ItemModel getItem(int id) throws AuctionAppException{
		try {
			return itemsRepo.getOne(id).toModel();
		}catch(EntityNotFoundException ex) {
			throw new NotFoundException();
		}
	}

	@Override
	public ItemModel addItem(ItemModel itemModel,User user) throws AuctionAppException {
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
		}else {
			item.setAddress(addressesRepo.save(item.getAddress()));
			newAddress=true;
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
	public ItemModel getItemFeatured() throws AuctionAppException {
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
	public Collection<ItemModel> findItemsValidFilterCategories(String term,List<Integer> categories, PaginationParams pgbl) throws AuctionAppException{
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
	
	@Override
	public Collection<ItemModel> findItemsValidFilterCategoriesSubcaetgoriesPrice(FilterItemsRequest request, PaginationParams pgbl) throws AuctionAppException{
		Timestamp crr=new Timestamp(System.currentTimeMillis());
		
		List<Category> categoriesList=null;
		List<Subcategory> subcategoriesList=null;
		
		if(request.getCategories().size()==0&&request.getSubcategories().size()==0) {
			categoriesList=categoriesRepo.findAll();
			subcategoriesList=subcateogriesRepo.findAll();
		}else {
			categoriesList=categoriesRepo.findAllById(request.getCategories());
			subcategoriesList=subcateogriesRepo.findAllById(request.getSubcategories());
		}
		
		if(request.getMinPrice()==null)
			request.setMinPrice(new BigDecimal(0));
		if(request.getMaxPrice()!=null) {
			return itemsRepo.searchActiveByCatsAndSubsFilterMinAndMaxPrice(crr, request.getTerm(),categoriesList, subcategoriesList,request.getMinPrice(),request.getMaxPrice(), pgbl.getPageable())
					.stream().map(x->x.toModel()).collect(Collectors.toList());			
		}else {
			return itemsRepo.searchActiveByCatsAndSubsFilterMinPrice(crr, request.getTerm(),categoriesList, subcategoriesList,request.getMinPrice(), pgbl.getPageable())
					.stream().map(x->x.toModel()).collect(Collectors.toList());			
		}			
	}
	
	@Override
	public HistogramResponseModel pricesHistogramForItems()throws AuctionAppException{
		List<PriceCountAggregateResult> data=itemsRepo.groupByPricesOrdered(new Timestamp(System.currentTimeMillis()));
		if(data.isEmpty())
			throw new NotFoundException();
		
		HistogramResponseModel histogramModel=new HistogramResponseModel();
		histogramModel.setMin(data.get(0).getStartingprice());
		histogramModel.setMax(data.get(data.size()-1).getStartingprice());
		
		List<PriceCountAggregateResult> histogram=new ArrayList<>();
		BigDecimal step=histogramModel.getMax().add(new BigDecimal("1")).divide(new BigDecimal("24"),RoundingMode.UP);
		
		Iterator<PriceCountAggregateResult> iterator=data.iterator();
		PriceCountAggregateResult entry;
		
		for(int i=1;i<=24;i++)
			histogram.add(new PriceCountAggregateResult(step.multiply(new BigDecimal(i)), 0));
		
		while(iterator.hasNext()) {
			entry=iterator.next();
			int index=entry.getStartingprice().divide(step,RoundingMode.UP).setScale(0, RoundingMode.FLOOR).intValue();
			histogram.get(index).setCount(histogram.get(index).getCount()+entry.getCount());				
		}
		histogramModel.setHistogram(histogram.stream().map(x->x.toHistogramEntry()).collect(Collectors.toList()));
		return histogramModel;
	}

}

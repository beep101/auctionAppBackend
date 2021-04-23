package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.entities.Item;
import com.example.demo.entities.User;
import com.example.demo.entities.Wish;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.WishModel;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.repositories.UsersRepository;
import com.example.demo.repositories.WishRepository;
import com.example.demo.services.interfaces.WishService;

public class DefaultWishlistService implements WishService {
	
	WishRepository wishesRepo;
	ItemsRepository itemsRepo;
	UsersRepository usersRepo;
	
	public DefaultWishlistService(WishRepository wishesRepo, ItemsRepository itemsRepo, UsersRepository usersRepo) {
		this.wishesRepo=wishesRepo;
		this.itemsRepo=itemsRepo;
		this.usersRepo=usersRepo;
	}
	
	@Override
	public WishModel createWish(WishModel wishModel, User principal) throws AuctionAppException {
		principal=usersRepo.findById(principal.getId()).get();
		Wish wish=Wish.fromModel(wishModel);
		wish.setUser(principal);
		//check if user tries to add own item to wishlist
		//throws exception
		if(principal.getItems().stream().anyMatch(x->x.getId()==wishModel.getItem().getId()))
			throw new UnallowedOperationException();
		//check if user already has item in wishlist
		//returns existing wish
		if(principal.getWishes().stream().anyMatch(x->x.getItem().getId()==wishModel.getItem().getId()))
			return principal.getWishes().stream().filter(x->x.getItem().getId()==wishModel.getItem().getId()).findFirst().get().toModel();
		Optional<Item> itemOpt=itemsRepo.findById(wish.getItem().getId());
		if(itemOpt.isEmpty()) {
			throw new InvalidDataException();
		}
		wish.setItem(itemOpt.get());
		return wishesRepo.save(wish).toModel();
	}

	@Override
	public WishModel deleteWish(WishModel wishModel, User principal) throws AuctionAppException {
		principal=usersRepo.findById(principal.getId()).get();
		Wish wish=Wish.fromModel(wishModel);
		//check if user tries to delete wish that is not his own
		//throws exception
		if(!principal.getWishes().stream().anyMatch(x->x.getId()==wishModel.getId()))
			throw new UnallowedOperationException();
		wishesRepo.delete(wish);
		return wishModel;
	}

	@Override
	public List<WishModel> getWishes(User principal) throws AuctionAppException {
		principal=usersRepo.findById(principal.getId()).get();
		return principal.getWishes().stream().map(x->x.toModel()).collect(Collectors.toList());
	}

}

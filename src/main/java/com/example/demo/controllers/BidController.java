package com.example.demo.controllers;

import java.security.Principal;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.exceptions.BidAmountLowException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.BidModel;
import com.example.demo.repositories.BidsRepository;
import com.example.demo.repositories.ItemsRepository;
import com.example.demo.services.AccountService;
import com.example.demo.services.BidService;
import com.example.demo.services.interfaces.IBidService;

@RestController
public class BidController {
	@Autowired
	private BidsRepository bidsRepo;
	@Autowired
	private ItemsRepository itemsRepo;
	
	private IBidService bidService;
	
	@PostConstruct
	public void init() {
		bidService=new BidService(bidsRepo, itemsRepo);
	}
	
	@PostMapping("/api/bids")
	public BidModel addBid( @RequestBody BidModel bid) throws InvalidDataException, BidAmountLowException, NotFoundException {
		User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return bidService.addBid(bid,principal);
	}
	
	@GetMapping("/api/bids/byItem/{itemId}")
	public Collection<BidModel> getBidsForItem(@PathVariable(name="itemId")int itemId){
		return bidService.getBids(itemId);
		
	}
}

package com.example.demo.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;
import com.example.demo.entities.User;

public interface BidsRepository extends JpaRepository<Bid, Integer>{
	public List<Bid> findByItemEquals(Item item);
	public List<Bid> findByItemEqualsOrderByIdDesc(Item item,Pageable pgbl);
	
	public void deleteByBidder(User bidder);
}

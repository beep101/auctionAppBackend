package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;

public interface BidsRepository extends JpaRepository<Bid, Integer>{
	public List<Bid> findByItemEquals(Item item);
}

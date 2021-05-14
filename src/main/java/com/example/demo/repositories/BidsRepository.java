package com.example.demo.repositories;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Bid;
import com.example.demo.entities.Item;
import com.example.demo.entities.User;

public interface BidsRepository extends JpaRepository<Bid, Integer>{
	public List<Bid> findByItemEquals(Item item);
	public List<Bid> findByItemEqualsOrderByIdDesc(Item item,Pageable pgbl);
	
	@Modifying
	@Query("DELETE FROM Bid b WHERE b IN (SELECT b FROM Bid b WHERE  b.bidder=:bidder AND b.item.sold=false AND b.item.endtime>:now)")
	public void deleteActiveByBidder(User bidder,Timestamp now);
}

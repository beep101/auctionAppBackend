package com.example.demo.repositories;

import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Category;
import com.example.demo.entities.Item;

public interface ItemsRepository extends JpaRepository<Item, Integer>{
	List<Item> findByCategory(Category category,Pageable pageable);
	
	//expired and sold filtering
	List<Item> findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(Timestamp timestamp, Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(Timestamp timestamp, Pageable pageable);
	List<Item> findBySoldFalseAndCategoryEqualsAndEndtimeAfter(Category category,Timestamp timestamp,Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfter(Timestamp timestamp,Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(Timestamp timestamp,String term,Pageable pageable);
}

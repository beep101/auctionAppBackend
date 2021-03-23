package com.example.demo.repositories;

import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Category;
import com.example.demo.entities.Item;

public interface ItemsRepository extends JpaRepository<Item, Integer>{
	List<Item> findByCategory(Category category,Pageable pageable);
	
	//expired and sold filtering
	List<Item> findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(Timestamp timestamp, Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(Timestamp timestamp, Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfterAndCategoryEquals(Timestamp timestamp,Category category,Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfter(Timestamp timestamp,Pageable pageable);
	
	List<Item> findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(Timestamp timestamp,String term,Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndCategoryIn(Timestamp timestamp,String term,List<Category> categories, Pageable pageable);

	@Query(value = "SELECT * FROM items WHERE sold=false AND endtime>:timestamp ORDER BY RANDOM() LIMIT 1",nativeQuery = true)
	Optional<Item> findBySoldFalseAndEndtimeAfterRandom(Timestamp timestamp);
}

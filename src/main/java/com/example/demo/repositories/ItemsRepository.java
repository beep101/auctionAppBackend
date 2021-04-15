package com.example.demo.repositories;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Category;
import com.example.demo.entities.Item;
import com.example.demo.entities.PriceCountAggregateResult;
import com.example.demo.entities.Subcategory;
import com.example.demo.entities.User;

public interface ItemsRepository extends JpaRepository<Item, Integer>{
	List<Item> findByCategory(Category category,Pageable pageable);
	
	//expired and sold filtering
	List<Item> findBySoldFalseAndEndtimeAfterOrderByStarttimeDesc(Timestamp timestamp, Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfterOrderByEndtimeAsc(Timestamp timestamp, Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfterAndSubcategoryCategoryEquals(Timestamp timestamp,Category category,Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfter(Timestamp timestamp,Pageable pageable);
	
	List<Item> findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCase(Timestamp timestamp,String term,Pageable pageable);
	List<Item> findBySoldFalseAndEndtimeAfterAndNameIsContainingIgnoreCaseAndSubcategoryCategoryIn(Timestamp timestamp,String term,List<Category> categories, Pageable pageable);

	
	@Query("SELECT i FROM Item i WHERE i.sold=false AND i.endtime>:now AND i.starttime<:now AND lower(i.name) LIKE lower(concat('%',:term,'%')) AND i.startingprice>:minPrice AND (i.subcategory IN (:subcategories) OR i.subcategory.category IN (:categories))")
	List<Item> searchActiveByCatsAndSubsFilterMinPrice(Timestamp now,String term,List<Category> categories, List<Subcategory> subcategories, BigDecimal minPrice, Pageable pageable);
	@Query("SELECT i FROM Item i WHERE i.sold=false AND i.endtime>:now AND i.starttime<:now AND lower(i.name) LIKE lower(concat('%',:term,'%')) AND i.startingprice>:minPrice AND i.startingprice<:maxPrice AND (i.subcategory IN (:subcategories) OR i.subcategory.category IN (:categories))")
	List<Item> searchActiveByCatsAndSubsFilterMinAndMaxPrice(Timestamp now,String term,List<Category> categories, List<Subcategory> subcategories, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
	
	@Query(value = "SELECT * FROM items WHERE sold=false AND endtime>:timestamp ORDER BY RANDOM() LIMIT 1",nativeQuery = true)
	Optional<Item> findBySoldFalseAndEndtimeAfterRandom(Timestamp timestamp);
	
	@Query("SELECT new com.example.demo.entities.PriceCountAggregateResult(i.startingprice,count(i.id))"
			+ "FROM Item i WHERE i.sold=FALSE AND i.starttime<:now AND i.endtime>:now GROUP BY i.startingprice ORDER BY i.startingprice ASC ")
	List<PriceCountAggregateResult> groupByPricesOrdered(Timestamp now);
	

	List<Item> findBySoldFalseAndEndtimeAfterAndSellerEquals(Timestamp timestamp,User user);
	@Query("SELECT i FROM Item i WHERE (i.sold=true OR i.endtime<:timestamp) AND i.seller=:user")
	List<Item> findBySoldTrueOrEndtimeBeforeAndSellerEquals(Timestamp timestamp,User user);
	@Query("SELECT distinct b.item FROM Bid b WHERE b.bidder=:user")
	List<Item> findAllBiddedItemsForUser(User user);
	
	@Query("SELECT i.name FROM Item i WHERE i.sold=false AND i.endtime>:now AND i.starttime<:now")
	List<String> getAllNamesForActiveItems(Timestamp now);
}

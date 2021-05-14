package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Item;
import com.example.demo.entities.Order;

public interface OrdersRepository extends JpaRepository<Order, String>{
	public List<Order> findByItem(Item item);
}

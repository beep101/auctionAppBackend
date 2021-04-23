package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Wish;

public interface WishRepository extends JpaRepository<Wish,Integer>{

}

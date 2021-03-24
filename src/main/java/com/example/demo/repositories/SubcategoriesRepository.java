package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Subcategory;

public interface SubcategoriesRepository extends JpaRepository<Subcategory, Integer>{

}

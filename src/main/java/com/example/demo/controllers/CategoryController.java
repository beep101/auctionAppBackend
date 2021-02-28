package com.example.demo.controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CategoryModel;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.services.CategoryService;
import com.example.demo.services.interfaces.ICategoryService;

@RestController
public class CategoryController {
	
	@Autowired
	CategoriesRepository categoriesRepo;
	
	ICategoryService categoryService;
	
	@PostConstruct
	public void init() {
		this.categoryService=new CategoryService(categoriesRepo);
	}
	
	@GetMapping("/api/categories")
	public Collection<CategoryModel> getAllategories() {
		return categoryService.getAllCategories();
	}

}

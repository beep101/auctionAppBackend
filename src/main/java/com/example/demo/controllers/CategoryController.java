package com.example.demo.controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CategoryModel;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.services.DefaultCategoryService;
import com.example.demo.services.interfaces.CategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Category Controller", tags= {"Category Controller"}, description = "Manages category related data")
@RestController
public class CategoryController {
	
	@Autowired
	private CategoriesRepository categoriesRepo;
	
	private CategoryService categoryService;
	
	@PostConstruct
	public void init() {
		this.categoryService=new DefaultCategoryService(categoriesRepo);
	}
	
	@ApiOperation(value = "Lists all item categories", notes = "Public access")
	@GetMapping("/api/categories")
	public Collection<CategoryModel> getAllategories() {
		return categoryService.getAllCategories();
	}

}

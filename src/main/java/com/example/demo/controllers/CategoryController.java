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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Category Controller", tags= {"Category Controller"}, description = "Manages category related data")
@RestController
public class CategoryController {
	
	@Autowired
	private CategoriesRepository categoriesRepo;
	
	private ICategoryService categoryService;
	
	@PostConstruct
	public void init() {
		this.categoryService=new CategoryService(categoriesRepo);
	}
	
	@ApiOperation(value = "Lists all item categories", notes = "Public access")
	@GetMapping("/api/categories")
	public Collection<CategoryModel> getAllategories() {
		return categoryService.getAllCategories();
	}

}

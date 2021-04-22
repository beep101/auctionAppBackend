package com.example.demo.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.models.CategoryModel;
import com.example.demo.repositories.CategoriesRepository;
import com.example.demo.services.interfaces.CategoryService;

public class DefaultCategoryService implements CategoryService{
	
	private CategoriesRepository categoriesRepo;
	
	public DefaultCategoryService(CategoriesRepository categoriesRepo) {
		this.categoriesRepo=categoriesRepo;
	}
	
	@Override
	public Collection<CategoryModel> getAllCategories() {
		List<CategoryModel> data=categoriesRepo.findAll().stream().map(x->x.toModel()).collect(Collectors.toList());
		return data;
	}

}

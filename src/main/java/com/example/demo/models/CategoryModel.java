package com.example.demo.models;

import java.util.List;

public class CategoryModel {
	private int id;
	private String name;
	private List<SubcategoryModel> subcategories;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SubcategoryModel> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<SubcategoryModel> subcategories) {
		this.subcategories = subcategories;
	}
}

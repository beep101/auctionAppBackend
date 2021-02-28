package com.example.demo.models;

import com.example.demo.entities.Category;

public class CategoryModel {
	private int id;
	private String name;
	
	public static CategoryModel fromCAtegoryEntity(Category entity) {
		CategoryModel model=new CategoryModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		return model;
	}

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
	
	
}

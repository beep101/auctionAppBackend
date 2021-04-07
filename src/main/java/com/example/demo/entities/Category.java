package com.example.demo.entities;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.demo.models.CategoryModel;

@Entity
@Table(name="categories")
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="name")
	private String name;
	
	@OneToMany(mappedBy="category")
	private List<Subcategory> subcategories;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public List<Subcategory> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<Subcategory> subcategories) {
		this.subcategories = subcategories;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public CategoryModel toModelNoSubcategories() {
		CategoryModel model=new CategoryModel();
		model.setId(this.getId());
		model.setName(this.getName());
		return model;
	}
	
	public CategoryModel toModel() {
		CategoryModel model=toModelNoSubcategories();
		model.setSubcategories(this.subcategories.stream().map(x->x.toModel()).collect(Collectors.toList()));
		return model;
	}

}

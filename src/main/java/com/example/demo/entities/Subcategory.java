package com.example.demo.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.example.demo.models.SubcategoryModel;


/**
 * The persistent class for the "subcategories" database table.
 * 
 */
@Entity
@Table(name="subcategories")
@NamedQuery(name="Subcategory.findAll", query="SELECT s FROM Subcategory s")
public class Subcategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@ManyToOne
	@JoinColumn(name="category")
	private Category category;

	@Column(name="name")
	private String name;

	public Subcategory() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public SubcategoryModel toModel() {
		SubcategoryModel model=new SubcategoryModel();
		model.setId(this.getId());
		model.setName(this.getName());
		model.setCategory(this.getCategory().toModelNoSubcategories());
		return model;
	}
}
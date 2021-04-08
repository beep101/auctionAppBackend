package com.example.demo.validations;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

public class FilterItemsRequest {
	@NotNull
	private String term;
	@NotNull
	private List<Integer> categories;
	@NotNull
	private List<Integer> subcategories;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	
	public FilterItemsRequest(@NotNull String term, @NotNull List<Integer> categories,
			@NotNull List<Integer> subcategories, BigDecimal minPrice, BigDecimal maxPrice) {
		super();
		this.term = term;
		this.categories = categories;
		this.subcategories = subcategories;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public List<Integer> getCategories() {
		return categories;
	}
	public void setCategories(List<Integer> categories) {
		this.categories = categories;
	}
	public List<Integer> getSubcategories() {
		return subcategories;
	}
	public void setSubcategories(List<Integer> subcategories) {
		this.subcategories = subcategories;
	}
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

}

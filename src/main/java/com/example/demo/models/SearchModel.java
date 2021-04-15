package com.example.demo.models;

import java.util.Collection;

public class SearchModel {
	private Collection<ItemModel> items;
	private String alternative;
	
	public Collection<ItemModel> getItems() {
		return items;
	}
	public void setItems(Collection<ItemModel> items) {
		this.items = items;
	}
	public String getAlternative() {
		return alternative;
	}
	public void setAlternative(String alternative) {
		this.alternative = alternative;
	}	
}

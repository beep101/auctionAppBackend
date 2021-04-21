package com.example.demo.models;

public class WishModel {
	private int id;
	private UserModel user;
	private ItemModel item;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public ItemModel getItem() {
		return item;
	}
	public void setItem(ItemModel item) {
		this.item = item;
	}
}

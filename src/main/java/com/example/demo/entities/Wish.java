package com.example.demo.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.example.demo.models.WishModel;

@Entity
@Table(name="wishlist")
@NamedQuery(name="Wishlist.findAll", query="SELECT w FROM Wish w")
public class Wish implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="itemid")
	private Item item;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="userid")
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public WishModel toModel() {
		WishModel model=new WishModel();
		
		model.setId(this.getId());
		model.setItem(this.getItem().toModel());
		
		return model;
	}
	
	public static Wish fromModel(WishModel model) {
		Wish entity=new Wish();
		
		entity.setId(model.getId());
		if(model.getUser()!=null) {
			User user=new User();
			user.setId(model.getUser().getId());
			entity.setUser(user);
		}
		if(model.getItem()!=null) {
			Item item=new Item();
			item.setId(model.getItem().getId());
			entity.setItem(item);
		}
		return entity;
	}
}

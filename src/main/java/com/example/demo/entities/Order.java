package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="orders")
@NamedQuery(name="Order.findAll", query="SELECT o FROM Order o")
public class Order {
	@Id
	@Column(name="orderid")
	private String orderId;
	
	@ManyToOne
	@JoinColumn(name="item")
	private Item item;

	@Column(name="successeful")
	private boolean successeful;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public boolean isSuccesseful() {
		return successeful;
	}

	public void setSuccesseful(boolean successeful) {
		this.successeful = successeful;
	}
}

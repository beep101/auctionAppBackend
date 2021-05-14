package com.example.demo.entities;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.example.demo.models.paypal.OrderModel;

@Entity
@Table(name="orders")
@NamedQuery(name="Order.findAll", query="SELECT o FROM Order o")
public class Order {
	@Id
	@Column(name="orderid")
	private String orderId;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
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
	
	public OrderModel toModel() {
		BigDecimal amount=this.getItem().getBids().stream().max((x,b)->x.getAmount().compareTo(b.getAmount())).get().getAmount();
		return new OrderModel(this.getOrderId(), this.getItem().getSeller().getMerchantId(), amount, this.isSuccesseful());
	}
}

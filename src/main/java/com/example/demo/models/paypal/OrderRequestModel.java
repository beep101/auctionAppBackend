package com.example.demo.models.paypal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderRequestModel {
	private String intent="CAPTURE";
	private List<OrderUnitRequestModel> purchase_units=new ArrayList<>();
	
	public OrderRequestModel(int itemId,BigDecimal price,String merchantId) {
		purchase_units.add(new OrderUnitRequestModel(itemId,price,merchantId));
	}
	
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public List<OrderUnitRequestModel> getPurchase_units() {
		return purchase_units;
	}
	public void setPurchase_units(List<OrderUnitRequestModel> purchase_units) {
		this.purchase_units = purchase_units;
	}
	
	
}

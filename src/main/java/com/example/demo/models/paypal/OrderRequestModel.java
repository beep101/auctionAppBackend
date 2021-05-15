package com.example.demo.models.paypal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequestModel {
	private String intent="CAPTURE";
	@JsonProperty("purchase_units")
	private List<OrderUnitRequestModel> purchaseUnits=new ArrayList<>();
	private OrderPayerRequestModel payer;
	
	public OrderRequestModel(int itemId,BigDecimal price,String merchantId) {
		purchaseUnits.add(new OrderUnitRequestModel(itemId,price,merchantId));
	}
	
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public List<OrderUnitRequestModel> getPurchaseUnits() {
		return purchaseUnits;
	}
	public void setPurchaseUnits(List<OrderUnitRequestModel> purchaseUnits) {
		this.purchaseUnits = purchaseUnits;
	}

	public OrderPayerRequestModel getPayer() {
		return payer;
	}

	public void setPayer(OrderPayerRequestModel payer) {
		this.payer = payer;
	}
		
}

package com.example.demo.models.paypal;

import java.math.BigDecimal;

public class OrderModel {
	private String orderId;
	private String merchantId;
	private BigDecimal amount;
	private boolean successful;
	
	public OrderModel(String orderId,String merchantId, BigDecimal amount,boolean successful) {
		this.orderId=orderId;
		this.merchantId=merchantId;
		this.amount=amount;
		this.successful=successful;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
}

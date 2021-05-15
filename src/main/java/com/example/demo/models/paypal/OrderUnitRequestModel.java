package com.example.demo.models.paypal;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderUnitRequestModel {

	public class Amount {
		@JsonProperty("currency_code")
		private String currencyCode="USD";
		private String value;
		
		Amount(BigDecimal amount){
			this.value=amount.toPlainString();
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
	public class Payee {
		private String merchantId;
		
		Payee(String merchantId){
			this.merchantId=merchantId;
		}
		
		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}
		
	}
	public class PaymentInstruction{
		@JsonProperty("disbursement_mode")
		private String disbursementMode="INSTANT";

		public String getDisbursementMode() {
			return disbursementMode;
		}

		public void setDisbursementMode(String disbursementMode) {
			this.disbursementMode = disbursementMode;
		}
	}
	
	private int referenceId;
	private OrderUnitRequestModel.Amount amount;
	private OrderUnitRequestModel.Payee payee;
	private OrderUnitRequestModel.PaymentInstruction paymentInstruction;
	
	public OrderUnitRequestModel(int itemId,BigDecimal price,String merchantId) {
		this.referenceId=itemId;
		this.amount=new OrderUnitRequestModel.Amount(price);
		this.payee=new OrderUnitRequestModel.Payee(merchantId);
		this.paymentInstruction=new OrderUnitRequestModel.PaymentInstruction();
	}
	
	public int getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}
	public OrderUnitRequestModel.Amount getAmount() {
		return amount;
	}
	public void setAmount(OrderUnitRequestModel.Amount amount) {
		this.amount = amount;
	}
	public OrderUnitRequestModel.Payee getPayee() {
		return payee;
	}
	public void setPayee(OrderUnitRequestModel.Payee payee) {
		this.payee = payee;
	}
	public OrderUnitRequestModel.PaymentInstruction getPaymentInstruction() {
		return paymentInstruction;
	}
	public void setPaymentInstruction(OrderUnitRequestModel.PaymentInstruction paymentInstruction) {
		this.paymentInstruction = paymentInstruction;
	}
}

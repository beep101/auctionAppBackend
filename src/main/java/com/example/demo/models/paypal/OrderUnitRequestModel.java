package com.example.demo.models.paypal;

import java.math.BigDecimal;

public class OrderUnitRequestModel {

	public class Amount {
		private String currency_code="USD";
		private String value;
		
		Amount(BigDecimal amount){
			this.value=amount.toPlainString();
		}

		public String getCurrency_code() {
			return currency_code;
		}

		public void setCurrency_code(String currency_code) {
			this.currency_code = currency_code;
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
		private String disbursement_mode="INSTANT";

		public String getDisbursement_mode() {
			return disbursement_mode;
		}

		public void setDisbursement_mode(String disbursement_mode) {
			this.disbursement_mode = disbursement_mode;
		}
	}
	
	private int reference_id;
	private OrderUnitRequestModel.Amount amount;
	private OrderUnitRequestModel.Payee payee;
	private OrderUnitRequestModel.PaymentInstruction payment_instruction;
	
	public OrderUnitRequestModel(int itemId,BigDecimal price,String merchantId) {
		this.reference_id=itemId;
		this.amount=new OrderUnitRequestModel.Amount(price);
		this.payee=new OrderUnitRequestModel.Payee(merchantId);
		this.payment_instruction=new OrderUnitRequestModel.PaymentInstruction();
	}
	
	public int getReference_id() {
		return reference_id;
	}
	public void setReference_id(int reference_id) {
		this.reference_id = reference_id;
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
	public OrderUnitRequestModel.PaymentInstruction getPayment_instruction() {
		return payment_instruction;
	}
	public void setPayment_instruction(OrderUnitRequestModel.PaymentInstruction payment_instruction) {
		this.payment_instruction = payment_instruction;
	}
}

package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.example.demo.models.PayMethodModel;

@Entity
@Table(name="paymentdata")
@NamedQuery(name="PayMethod.findAll", query="SELECT p FROM PayMethod p")
public class PayMethod {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="oncardname")
	private String onCardName;
	
	@Column(name="cardnumber")
	private String cardNumber;
	
	@Column(name="cvccw")
	private String cvccw;
	
	@Column(name="expmonth")
	private int expMonth;
	
	@Column(name="expyear")
	private int expYear;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOnCardName() {
		return onCardName;
	}

	public void setOnCardName(String onCardName) {
		this.onCardName = onCardName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCvccw() {
		return cvccw;
	}

	public void setCvccw(String cvccw) {
		this.cvccw = cvccw;
	}

	public int getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(int expMonth) {
		this.expMonth = expMonth;
	}

	public int getExpYear() {
		return expYear;
	}

	public void setExpYear(int expYear) {
		this.expYear = expYear;
	}
	
	public PayMethodModel toModel() {
		PayMethodModel model=new PayMethodModel();
		
		model.setId(id);
		model.setCardNumber(cardNumber);
		model.setCvccw(cvccw);
		model.setExpMonth(expMonth);
		model.setExpYear(expYear);
		model.setOnCardName(onCardName);
		
		return model;
	}
	
	public void populate(PayMethodModel model) {
		this.setId(model.getId());
		this.setCardNumber(model.getCardNumber());
		this.setCvccw(model.getCvccw());
		this.setExpMonth(model.getExpMonth());
		this.setExpYear(model.getExpYear());
		this.setOnCardName(model.getOnCardName());
	}
}

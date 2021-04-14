package com.example.demo.validations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Path.Node;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.example.demo.models.PayMethodModel;

public class PayMethodRequest {
	@NotBlank(message="On card name cannot be blank")
	private String onCardName;
	@NotBlank(message="Card number cannot be blank")
	private String cardNumber;
	@NotBlank(message="CV/CCW number cannot be blank")
	private String cvccw;
	@Min(value=1, message="Month must be in range 1 to 12")
	@Max(value=12, message="Month must be in range 1 to 12")
	private int expMonth;
	@Min(value=1000, message="Year must be represented as 4 digit number")
	@Max(value=9999, message="Year must be represented as 4 digit number")
	private int expYear;
	
	public PayMethodRequest(PayMethodModel model) {
		this.onCardName=model.getOnCardName();
		this.cardNumber=model.getCardNumber();
		this.cvccw=model.getCvccw();
		this.expMonth=model.getExpMonth();
		this.expYear=model.getExpYear();
	}
	
	public Map<String,String> validate(){
		Map<String,String> problems=validateAnnotations();
		problems.putAll(validateData()); 
		return problems;
	}
	
	public Map<String,String> validateAnnotations(){
		Set<ConstraintViolation<PayMethodRequest>> violations=Validation.buildDefaultValidatorFactory().getValidator().validate(this);
		Map<String, String> problems=new HashMap<>();
		for(ConstraintViolation<PayMethodRequest> cv:violations) {
			String last="";
			Iterator<Node> iterator=cv.getPropertyPath().iterator();
			while(iterator.hasNext())
				last=iterator.next().getName();
			problems.put(last, cv.getMessage());
		}
		return problems;
	}
	
	public Map<String,String> validateData(){
		Map<String, String> problems=new HashMap<>();
		
		//validate card number is valid for some card type
		//validate cv ccw for that type
		//validate card not expired
		
		return problems;
	}
	
}

package com.example.demo.validations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Path.Node;
import javax.validation.constraints.NotBlank;

import com.example.demo.models.AddressModel;

public class AddressRequest {
	@NotBlank(message = "Address can't be blank")
	private String address;
	@NotBlank(message = "City name can't be blank")
	private String city;
	@NotBlank(message = "Zip code can't be blank")
	private String zip;
	@NotBlank(message = "Country name can't be blank")
	private String country;
	
	private String phone;
	
	private final String VALID_PHONE_NUMBERS_PATTERNS 
    = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" 
    + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$" 
    + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
	
	public AddressRequest(AddressModel model) {
		this.address=model.getAddress();
		this.city=model.getCity();
		this.country=model.getCountry();
		this.phone=model.getPhone();
		this.zip=model.getZip();
	}
	
	public Map<String,String> validate(){
		Map<String,String> problems=validateAnnotations();
		problems.putAll(validatePhoneNumber());
		return problems;
	}
	
	public Map<String,String> validateAnnotations(){
		Set<ConstraintViolation<AddressRequest>> violations=Validation.buildDefaultValidatorFactory().getValidator().validate(this);
		Map<String, String> problems=new HashMap<>();
		for(ConstraintViolation<AddressRequest> cv:violations) {
			String last="";
			Iterator<Node> iterator=cv.getPropertyPath().iterator();
			while(iterator.hasNext())
				last=iterator.next().getName();
			problems.put(last, cv.getMessage());
		}
		return problems;
	}
	
	public Map<String,String> validatePhoneNumber(){
		Map<String,String> problems=new HashMap<>();
		if(this.phone==null||this.phone.isBlank())
			return problems;
		else {
			Pattern pattern = Pattern.compile(VALID_PHONE_NUMBERS_PATTERNS);
			if(!pattern.matcher(this.phone).matches()) {
				problems.put("phone", "Phone number format is invalid");
			}
		}
		return problems;
	}
	
	public Map<String,String> validateRealAddress(){
		Map<String,String> problems=new HashMap<>();
		//validate address is real using external service
		return problems;
	}
}

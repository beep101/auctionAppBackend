package com.example.demo.validations;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Path.Node;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.models.UserModel;
import com.example.demo.utils.Gender;

public class UserRequest {

	@NotBlank(message = "First name can't be blank")
	private String firstName;
	@NotBlank(message = "Last name can't be blank")
	private String lastName;
	@Email(message = "Email address must be valid")
	@NotBlank(message = "Email can't be blank")
	private String email;
	@NotBlank(message = "Password can't be shorter than 6 characters")
	@Size(min = 6, message = "Password can't be shorter than 6 characters")
	private String password;
	
	private Gender gender;
	private Timestamp birthday;
	
	public UserRequest(UserModel model) {
		this.firstName=model.getFirstName();
		this.lastName=model.getLastName();
		this.email=model.getEmail();
		this.password=model.getPassword();
		this.gender=model.getGender();
		this.birthday=model.getBirthday();
	}
	
	public Map<String,String> validate(){
		Set<ConstraintViolation<UserRequest>> violations=Validation.buildDefaultValidatorFactory().getValidator().validate(this);
		Map<String, String> problems=new HashMap<>();
		for(ConstraintViolation<UserRequest> cv:violations) {
			String last="";
			Iterator<Node> iterator=cv.getPropertyPath().iterator();
			while(iterator.hasNext())
				last=iterator.next().getName();
			problems.put(last, cv.getMessage());
		}
		return problems;
	}
	
	public Map<String,String> validateIncludingGenderAndBirthday(){
		Map<String,String> problems=validate();
		Calendar lowerLimit=Calendar.getInstance();
		lowerLimit.add(Calendar.YEAR, -100);
		Calendar upperLimit=Calendar.getInstance();
		upperLimit.add(Calendar.YEAR, -13);
		if(birthday!=null)
			if(birthday.before(lowerLimit.getTime()))
				problems.put("birthday", "User cannot be older than 100 years");
			else if(birthday.after(upperLimit.getTime()))
				problems.put("birthday", "User cannot be younger than 13 years");
		return problems;
	}
}

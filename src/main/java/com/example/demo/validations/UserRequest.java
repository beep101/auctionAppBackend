package com.example.demo.validations;

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
	
	public UserRequest(UserModel model) {
		this.firstName=model.getFirstName();
		this.lastName=model.getLastName();
		this.email=model.getEmail();
		this.password=model.getPassword();
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
}

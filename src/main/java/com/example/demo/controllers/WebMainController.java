package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;

@Api(value = "Frontend Controller", tags= {"Frontend Controller"}, description = "Redirects requests to React frontend")
@Controller
public class WebMainController {
	@GetMapping(value={"/","/login","/register","/about","/termsandconditions","/privacypolicy",
					   "/shop","/account","/item","/search","/forgotPassword","/newPassword","/allCategories" })
	public String index() {
		return "index";
	}

}


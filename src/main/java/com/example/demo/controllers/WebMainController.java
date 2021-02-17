package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebMainController {
	@RequestMapping(value="/")
	public String index() {
		return "index";
	}

}

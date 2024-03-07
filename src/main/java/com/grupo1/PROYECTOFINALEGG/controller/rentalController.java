package com.grupo1.PROYECTOFINALEGG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.grupo1.PROYECTOFINALEGG.service.rentalService;


@Controller
@RequestMapping("/")
public class rentalController {

	@Autowired
	rentalService rSrv;
	
	@GetMapping("/")
	public String home() {
		return "index.html";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register.html";
	}
	
	
	
	

	
}

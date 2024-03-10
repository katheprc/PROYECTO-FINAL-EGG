package com.grupo1.PROYECTOFINALEGG.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo1.PROYECTOFINALEGG.Entity.Property;
import com.grupo1.PROYECTOFINALEGG.Entity.User;
import com.grupo1.PROYECTOFINALEGG.service.RentalService;
import com.grupo1.PROYECTOFINALEGG.service.UserService;

@RestController
@RequestMapping("/api")
public class rentalRestController {

	@Autowired 
	private UserService uSrv;
	
	@Autowired
	private RentalService rSrv;
	
	@GetMapping("/admin/users")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getUsers(){
		
		return uSrv.listarUsuarios();
		
	}
	
	@GetMapping("/properties")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public List<Property> getProperties(){
		
		return rSrv.getProperties();
		
	}

	
	
}

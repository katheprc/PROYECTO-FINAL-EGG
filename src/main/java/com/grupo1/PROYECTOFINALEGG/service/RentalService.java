package com.grupo1.PROYECTOFINALEGG.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo1.PROYECTOFINALEGG.Entity.Property;
import com.grupo1.PROYECTOFINALEGG.Repositories.PropertyRepository;

@Service
public class RentalService {

	@Autowired 
	PropertyRepository pRepo;
	
	public List<Property> getProperties(){
		
		return pRepo.findAll();
		
	}
	
}

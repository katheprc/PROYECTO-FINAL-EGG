package com.grupo1.PROYECTOFINALEGG.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Service {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String price;
        
    public Service() {
    	
    }

	public Service(String name, Double price) {
		
		this.name = name;
		this.price = String.valueOf(price);

	}
    
}

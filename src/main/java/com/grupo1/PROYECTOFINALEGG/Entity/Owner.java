package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Owner extends User {

	@ElementCollection
    @CollectionTable(name="listOfProperties")
	@OneToMany
    private  List<Property> properties = new ArrayList<>();
	
	
}

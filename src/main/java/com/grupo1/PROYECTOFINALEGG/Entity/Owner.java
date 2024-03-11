package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("OWNER")
public class Owner extends Client {

	@ElementCollection
    @CollectionTable(name="listOfProperties")
    List<String> properties = new ArrayList<>();

	public List<String> getProperties() {
		return properties;
	}

	public void addProperty(String prop) {
		this.properties.add(prop);
	}
	
	
	
	
}

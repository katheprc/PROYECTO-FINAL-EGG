package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
@DiscriminatorValue("OWNER")
public class Owner extends User {

	@ElementCollection
	@CollectionTable(name = "listOfProperties")
	@OneToMany
	List<Property> properties = new ArrayList<>();

	public void addProperty(Property prop) {
		this.properties.add(prop);
	}

	public void delProp(Property prop) {
		this.properties.remove(prop);
	}
}

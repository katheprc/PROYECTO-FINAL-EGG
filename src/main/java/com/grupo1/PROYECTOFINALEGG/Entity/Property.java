package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String details;
	
	private Double pricePerDay;

	@ElementCollection
	@CollectionTable(name = "listOfPropertyImages")
	private List<String> imgs = new ArrayList<>();

	private String owner;

	@ElementCollection
	@CollectionTable(name = "listOfServices")
	@OneToMany
	private List<Service> services = new ArrayList<>();

	public void addImg(String img) {
		this.imgs.add(img);
	}
	
	public void addSrv(Service srv) {
		this.services.add(srv);
	}

}

package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer rating;

	private String commentary;

	private String date;

	private String img;

	@OneToOne
	private Booking booking;

	@ManyToOne
	private Client client;

	@ManyToOne
	private Property property;

	public void addImg(String img) {
		this.img = (img);
	}
}

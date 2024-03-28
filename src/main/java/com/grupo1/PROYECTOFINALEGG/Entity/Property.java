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

	private String address;

	private String details;

	private Double pricePerDay;

	private Integer rating = 0;

	private Integer ownerId;

	@ElementCollection
	@CollectionTable(name = "listOfPropertyImages")
	private List<String> imgs = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "listOfServices")
	@OneToMany
	private List<Service> services = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "listOfPosts")
	@OneToMany
	private List<Post> posts;

	@ElementCollection
	@CollectionTable(name = "listOfPropBookings")
	@OneToMany
	private List<Booking> bookings;

	public void addImg(String img) {
		this.imgs.add(img);
	}

	public void addSrv(Service srv) {
		this.services.add(srv);
	}

	public void addPosts(Post post) {
		this.posts.add(post);
	}

	public void addBooking(Booking booking) {
		this.bookings.add(booking);
	}
}

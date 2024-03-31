package com.grupo1.PROYECTOFINALEGG.Entity;

import java.text.DecimalFormat;
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

	private Double rating = 0.0;

	private Integer ownerId;

	@ElementCollection
	@CollectionTable(name = "listOfPropertyImages")
	private List<String> imgs = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "listOfServices")
	@OneToMany
	private List<Service> services = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "listOfPropPosts")
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

	public void addPost(Post post) {
		this.posts.add(post);
	}

	public void addBooking(Booking booking) {
		this.bookings.add(booking);
	}

	public void addRating(Integer rating2) {

		Double ratingTotal = 0.0;

		for (Post post : posts) {
			ratingTotal = ratingTotal + post.getRating();
		}

		this.rating = ratingTotal / posts.size();

		DecimalFormat df = new DecimalFormat("#.##");
		this.rating = Double.parseDouble(df.format(this.rating));

	}
}
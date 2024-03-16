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
@DiscriminatorValue("CLIENT")
public class Client extends User {

	@ElementCollection
	@CollectionTable(name = "listOfPosts")
	@OneToMany
	private List<Post> posts;

	@ElementCollection
	@CollectionTable(name = "listOfBookings")
	@OneToMany
	private List<Booking> bookings = new ArrayList<>();

	public void addPosts(Post post) {
		this.posts.add(post);
	}

	public void addBookings(Booking booking) {
		this.bookings.add(booking);
	}

}

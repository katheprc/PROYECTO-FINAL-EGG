package com.grupo1.PROYECTOFINALEGG.Entity.DTOS;

import java.util.ArrayList;
import java.util.List;

import com.grupo1.PROYECTOFINALEGG.Entity.Booking;
import com.grupo1.PROYECTOFINALEGG.Entity.Post;

import lombok.Data;

@Data
public class ClientDTO extends UserDTO {

	List<Booking> bookings = new ArrayList<>();
	List<Post> posts;

	public ClientDTO(Integer id, String username, String lastname, String email, String type, String img,
			List<Booking> bookings, List<Post> posts) {
		super(id, username, lastname, email, type, img);
		this.bookings = bookings;
		this.posts = posts;

	}

}

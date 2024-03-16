package com.grupo1.PROYECTOFINALEGG.Entity.DTOS;

import java.util.ArrayList;
import java.util.List;

import com.grupo1.PROYECTOFINALEGG.Entity.Booking;
import com.grupo1.PROYECTOFINALEGG.Entity.Post;
import com.grupo1.PROYECTOFINALEGG.Entity.Property;

import lombok.Data;

@Data
public class OwnerDTO extends UserDTO {

	List<Property> properties = new ArrayList<>();

	public OwnerDTO(Integer id, String username, String lastname, String email, String type, String img,
			List<Property> properties) {
		super(id, username, lastname, email, type, img);
		this.properties = properties;
	}

}

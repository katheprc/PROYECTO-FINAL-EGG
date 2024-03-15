package com.grupo1.PROYECTOFINALEGG.Entity.DTOS;

import lombok.Data;

@Data
public abstract class UserDTO {

	Integer id;
	String username;
	String lastname;
	String email;
	String type;

	String img;

	public UserDTO(Integer id, String username, String lastname, String email, String type, String img) {
		this.id = id;
		this.username = username;
		this.lastname = lastname;
		this.email = email;
		this.type = type;
		this.img = img;
	}

}
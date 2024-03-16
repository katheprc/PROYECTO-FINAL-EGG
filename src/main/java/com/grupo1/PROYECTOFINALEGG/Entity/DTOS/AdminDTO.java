package com.grupo1.PROYECTOFINALEGG.Entity.DTOS;

import lombok.Data;

@Data
public class AdminDTO extends UserDTO {

	public AdminDTO(Integer id, String username, String lastname, String email, String type, String img) {
		super(id, username, lastname, email, type, img);
	}

}

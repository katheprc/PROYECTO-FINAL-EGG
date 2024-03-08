package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OwnerDTO extends UserDTO {
	private List<Property> properties = new ArrayList<>();
}

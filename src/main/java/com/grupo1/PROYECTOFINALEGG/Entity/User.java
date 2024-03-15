package com.grupo1.PROYECTOFINALEGG.Entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Formula;

import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String username;
	String lastname;
	String email;
	String password;
	@Enumerated(EnumType.STRING)
	Role role;
	String token;
	@Formula("dtype")
	String type;

	private String img;

	public User() {
	}

	public User(Integer id, String username, String lastname, String email, String password, Role role) {
		this.id = id;
		this.username = username;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public void addImg(String img) {
		this.img = img;
	}

}

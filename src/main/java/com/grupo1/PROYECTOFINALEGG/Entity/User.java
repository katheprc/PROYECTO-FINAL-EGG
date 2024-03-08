package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class User {
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
    
    @ElementCollection
    @CollectionTable(name="listOfBookings")
    @OneToMany
    private  List<Booking> bookings = new ArrayList<>();
    
    @OneToOne
    private Imagen image;
    
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


}

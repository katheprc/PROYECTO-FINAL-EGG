package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
Integer id;
    
    String username;
    String lastname;
    String email;

    private  List<Booking> bookings = new ArrayList<>();
    
    private Imagen image;
}

package com.grupo1.PROYECTOFINALEGG.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.Booking;
import com.grupo1.PROYECTOFINALEGG.Entity.Client;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>{

	
}

package com.grupo1.PROYECTOFINALEGG.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.Service;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>{
	
	
}
package com.grupo1.PROYECTOFINALEGG.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>{

	@Query("SELECT p FROM Service p where p = :service")
	public Service find(Service service);
	
	
}

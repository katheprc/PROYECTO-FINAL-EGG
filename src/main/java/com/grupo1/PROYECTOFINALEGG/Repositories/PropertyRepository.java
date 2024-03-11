package com.grupo1.PROYECTOFINALEGG.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.Property;


@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer>{

	@Query("SELECT p FROM Property p where p = :property")
	Property find(Property property);


}

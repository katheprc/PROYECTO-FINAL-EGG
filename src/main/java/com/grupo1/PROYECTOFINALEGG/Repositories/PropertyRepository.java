package com.grupo1.PROYECTOFINALEGG.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

	@Query("SELECT p FROM Property p where p = :property")
	Property find(Property property);

	@Query("SELECT p FROM Property p ORDER BY p.id ASC")
	public List<Property> findAllAsc();

	@Query("SELECT p FROM Property p ORDER BY p.id DESC")
	public List<Property> findAllDesc();

	@Query("SELECT p FROM Property p ORDER BY p.name ASC")
	public List<Property> findByNameAsc();

	@Query("SELECT p FROM Property p ORDER BY p.name DESC")
	public List<Property> findByNameDesc();

	@Query("SELECT p FROM Property p ORDER BY p.pricePerDay ASC")
	public List<Property> findByPriceAsc();

	@Query("SELECT p FROM Property p ORDER BY p.pricePerDay DESC")
	public List<Property> findByPriceDesc();

	@Query("SELECT p FROM Property p ORDER BY p.rating ASC")
	public List<Property> findByRatingAsc();

	@Query("SELECT p FROM Property p ORDER BY p.rating DESC")
	public List<Property> findByRatingDesc();

	@Query("SELECT p FROM Property p WHERE p.address LIKE %:search% ORDER BY p.pricePerDay ASC")
	public List<Property> findByAddressAndPriceASC(@Param("search") String search);

	@Query("SELECT p FROM Property p WHERE p.address LIKE %:search% ORDER BY p.pricePerDay DESC")
	public List<Property> findByAddressAndPriceDESC(@Param("search") String search);

	@Query("SELECT p FROM Property p WHERE p.address LIKE %:search%")
	public List<Property> findByAddress(@Param("search") String search);

}

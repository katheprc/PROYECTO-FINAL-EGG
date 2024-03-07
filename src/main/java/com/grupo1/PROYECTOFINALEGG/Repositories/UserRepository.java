package com.grupo1.PROYECTOFINALEGG.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	@Query("SELECT u FROM User u where u.email = :email")
	public User findByEmail(@Param("email") String email);

	@Query("SELECT u FROM User u where u.token = :token")
	public User findByToken(@Param("token") String token);
	
}

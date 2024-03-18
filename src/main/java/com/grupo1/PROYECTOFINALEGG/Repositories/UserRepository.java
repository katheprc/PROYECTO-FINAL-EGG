package com.grupo1.PROYECTOFINALEGG.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u where u.email = :email")
	public User findByEmail(@Param("email") String email);

	@Query("SELECT u FROM User u where u.token = :token")
	public User findByToken(@Param("token") String token);

	@Query("SELECT u FROM User u ORDER BY u.id ASC")
	public List<User> findAllAsc();

	@Query("SELECT u FROM User u ORDER BY u.id DESC")
	public List<User> findAllDesc();

	@Query("SELECT u FROM User u WHERE u.type LIKE :type ORDER BY u.id DESC")
	public List<User> findByTypeOrderDesc(@Param("type") String type);

	@Query("SELECT u FROM User u WHERE u.type LIKE :type ORDER BY u.id ASC")
	public List<User> findByTypeOrderAsc(@Param("type") String type);

}

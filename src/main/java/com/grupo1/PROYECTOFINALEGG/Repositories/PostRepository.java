package com.grupo1.PROYECTOFINALEGG.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("SELECT p FROM Post p ORDER BY p.rating ASC")
	public List<Post> findAllAsc();

	@Query("SELECT p FROM Post p ORDER BY p.rating DESC")
	public List<Post> findAllDesc();

	@Query("SELECT p FROM Post p WHERE p.rating = :rating")
	public List<Post> findByRating(@Param("rating") Integer rating);

}

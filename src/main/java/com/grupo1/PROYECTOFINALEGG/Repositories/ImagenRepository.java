package com.grupo1.PROYECTOFINALEGG.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grupo1.PROYECTOFINALEGG.Entity.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Integer> {

}

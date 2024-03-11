package com.grupo1.PROYECTOFINALEGG.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.PROYECTOFINALEGG.Entity.Imagen;
import com.grupo1.PROYECTOFINALEGG.Entity.Property;
import com.grupo1.PROYECTOFINALEGG.Repositories.ImagenRepository;
import com.grupo1.PROYECTOFINALEGG.Repositories.PropertyRepository;
import com.grupo1.PROYECTOFINALEGG.Repositories.ServiceRepository;

@Service
public class RentalService {

	@Autowired
	PropertyRepository pRepo;

	@Autowired
	ServiceRepository sRepo;

	@Autowired
	ImagenRepository iRepo;

	public List<Property> getProperties() {

		return pRepo.findAll();

	}

	public void saveProp(Property property) {
		pRepo.save(property);
	}

	public void saveServ(com.grupo1.PROYECTOFINALEGG.Entity.Service service) {

		sRepo.save(service);

	}

	public Integer subirImagen(MultipartFile archivo) {
		try {
			Imagen imagen = new Imagen();
			imagen.setNombre(archivo.getOriginalFilename());
			imagen.setContenido(archivo.getBytes());
			iRepo.save(imagen);
			return iRepo.find(imagen).getId();
		} catch (IOException e) {
			throw new RuntimeException("Error al subir la imagen", e);
		}
	}

	public Imagen obtenerImagenPorId(Integer id) throws NotFoundException {
		Imagen imagen = iRepo.findById(id).orElseThrow(() -> new NotFoundException());
		return imagen;
	}

	public Property getProp(Property property) {
		return pRepo.find(property);
	}

	public Optional<Property> getPropById(Integer id) {
		return pRepo.findById(id);
	}

	public com.grupo1.PROYECTOFINALEGG.Entity.Service getSrv(com.grupo1.PROYECTOFINALEGG.Entity.Service service) {
		return sRepo.find(service);
	}

	public Optional<com.grupo1.PROYECTOFINALEGG.Entity.Service> getSrvById(Integer id) {
		return sRepo.findById(id);
	}

}

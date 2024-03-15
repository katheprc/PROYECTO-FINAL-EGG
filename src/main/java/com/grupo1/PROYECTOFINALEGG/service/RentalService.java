package com.grupo1.PROYECTOFINALEGG.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.PROYECTOFINALEGG.Entity.Imagen;
import com.grupo1.PROYECTOFINALEGG.Entity.Post;
import com.grupo1.PROYECTOFINALEGG.Entity.Property;
import com.grupo1.PROYECTOFINALEGG.Repositories.ImagenRepository;
import com.grupo1.PROYECTOFINALEGG.Repositories.PostRepository;
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

	@Autowired
	PostRepository postRepo;

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

	public List<Post> getPosts(String order, String rating) {

		List<Post> listaPosts;

		if (rating.equals("0")) {

			if (order.equals("DESC")) {
				listaPosts = postRepo.findAllDesc();
			} else {
				listaPosts = postRepo.findAllAsc();
			}

		} else {
			listaPosts = postRepo.findByRating(Integer.valueOf(rating));
		}

		return listaPosts;

	}

	public List<Post> getAllPosts() {
		return postRepo.findAll();
	}

	public void deletePost(int id) {
		postRepo.deleteById(id);
	}

	public List<Property> getProperties(String type, String order) {

		List<Property> listaProperties;

		if (order.equals("DESC")) {

			if (type.equals("name")) {
				listaProperties = pRepo.findByNameDesc();
			} else if (type.equals("price")) {
				listaProperties = pRepo.findByPriceDesc();
			} else if (type.equals("rating")) {
				listaProperties = pRepo.findByRatingDesc();
			} else {
				listaProperties = pRepo.findAllDesc();
			}

		} else {

			if (type.equals("name")) {
				listaProperties = pRepo.findByNameAsc();
			} else if (type.equals("price")) {
				listaProperties = pRepo.findByPriceAsc();
			} else if (type.equals("rating")) {
				listaProperties = pRepo.findByRatingAsc();
			} else {
				listaProperties = pRepo.findAllAsc();
			}

		}

		return listaProperties;

	}

	public void deleteProperty(int id) {
		pRepo.deleteById(id);

	}

}

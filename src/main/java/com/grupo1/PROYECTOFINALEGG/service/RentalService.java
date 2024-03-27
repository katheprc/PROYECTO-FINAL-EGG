package com.grupo1.PROYECTOFINALEGG.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.PROYECTOFINALEGG.Entity.Booking;
import com.grupo1.PROYECTOFINALEGG.Entity.Client;
import com.grupo1.PROYECTOFINALEGG.Entity.Imagen;
import com.grupo1.PROYECTOFINALEGG.Entity.Owner;
import com.grupo1.PROYECTOFINALEGG.Entity.Post;
import com.grupo1.PROYECTOFINALEGG.Entity.Property;
import com.grupo1.PROYECTOFINALEGG.Repositories.BookingRepository;
import com.grupo1.PROYECTOFINALEGG.Repositories.ImagenRepository;
import com.grupo1.PROYECTOFINALEGG.Repositories.PostRepository;
import com.grupo1.PROYECTOFINALEGG.Repositories.PropertyRepository;
import com.grupo1.PROYECTOFINALEGG.Repositories.ServiceRepository;
import com.grupo1.PROYECTOFINALEGG.Repositories.UserRepository;

@Service
public class RentalService {

	@Autowired
	PropertyRepository pRepo;

	@Autowired
	UserRepository uRepo;

	@Autowired
	ServiceRepository sRepo;

	@Autowired
	ImagenRepository iRepo;

	@Autowired
	PostRepository postRepo;

	@Autowired
	BookingRepository bRepo;

	public List<Property> getProperties() {

		return pRepo.findAll();

	}

	public List<Property> get5Properties() {

		List<Property> random;

		random = pRepo.findByRatingDesc();

		List<Property> retorno = new ArrayList<>();

		for (int aux = 0; aux < random.size(); aux++) {
			if (aux < 5) {
				retorno.add(random.get(aux));
			} else {
				break;
			}
		}

		return retorno;

	}

	public Property saveProp(Property property) {
		return pRepo.save(property);
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

	public Property getProp(Integer id) {
		return pRepo.findById(id).get();
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
		List<com.grupo1.PROYECTOFINALEGG.Entity.User> listaUsers = uRepo.findAll();
		for (com.grupo1.PROYECTOFINALEGG.Entity.User user : listaUsers) {
			if (user instanceof Owner) {
				if (((Owner) user).getProperties().contains(pRepo.findById(id).get())) {
					((Owner) user).delProp(pRepo.findById(id).get());
					uRepo.save(user);
					break;
				}

			}
		}
		pRepo.deleteById(id);

	}

	public Double ingresosTotales() {

		List<Property> lista = pRepo.findAll();
		Double precioBase = 0.0;
		Double preciosSrv = 0.0;

		for (Property prop : lista) {
			precioBase = precioBase + prop.getPricePerDay();

			List<com.grupo1.PROYECTOFINALEGG.Entity.Service> listaSrv = prop.getServices();
			for (com.grupo1.PROYECTOFINALEGG.Entity.Service srv : listaSrv) {
				preciosSrv = preciosSrv + Double.parseDouble(srv.getPrice());
			}
		}

		return ((precioBase + preciosSrv) * 0.10);
	}

	public void deleteBooking(int id) {
		bRepo.deleteById(id);
	}

	public List<Booking> findByUserBooking(int id) {
		return ((Client) uRepo.findById(id).get()).getBookings();
	}

}

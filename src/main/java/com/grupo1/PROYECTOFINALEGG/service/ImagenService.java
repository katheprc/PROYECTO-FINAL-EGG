package com.grupo1.PROYECTOFINALEGG.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.PROYECTOFINALEGG.Entity.Imagen;
import com.grupo1.PROYECTOFINALEGG.Exceptions.MyException;
import com.grupo1.PROYECTOFINALEGG.Repositories.ImagenRepository;

@Service
public class ImagenService {

	@Autowired
	private ImagenRepository iRepo;

	public Imagen guardar(MultipartFile archivo) throws MyException {
		if (archivo != null) {
			try {

				Imagen imagen = new Imagen();

				imagen.setMime(archivo.getContentType());

				imagen.setNombre(archivo.getName());

				imagen.setContenido(archivo.getBytes());

				return iRepo.save(imagen);

			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return null;
	}

	public Imagen actualizar(MultipartFile archivo, Integer idImagen) throws MyException {
		if (archivo != null) {
			try {

				Imagen imagen = new Imagen();

				if (idImagen != null) {
					Optional<Imagen> respuesta = iRepo.findById(idImagen);

					if (respuesta.isPresent()) {
						imagen = respuesta.get();
					}
				}

				imagen.setMime(archivo.getContentType());

				imagen.setNombre(archivo.getName());

				imagen.setContenido(archivo.getBytes());

				return iRepo.save(imagen);

			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return null;

	}

}

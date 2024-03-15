package com.grupo1.PROYECTOFINALEGG.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.PROYECTOFINALEGG.Entity.Property;
import com.grupo1.PROYECTOFINALEGG.Entity.User;
import com.grupo1.PROYECTOFINALEGG.Entity.DTOS.UserDTO;
import com.grupo1.PROYECTOFINALEGG.service.RentalService;
import com.grupo1.PROYECTOFINALEGG.service.UserService;

@RestController
@RequestMapping("/api")
public class rentalRestController {

	@Autowired
	private UserService uSrv;

	@Autowired
	private RentalService rSrv;

	@GetMapping("/admin/users")
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserDTO> getUsers() {

		return uSrv.listarUsuarios();

	}

	@GetMapping("/properties")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public List<Property> getProperties() {

		return rSrv.getProperties();

	}

	@PostMapping("/subir")
	public ResponseEntity<String> subirImagen(@RequestParam("archivo") MultipartFile archivo) {
		rSrv.subirImagen(archivo);
		return new ResponseEntity<>("Imagen subida con éxito", HttpStatus.OK);
	}

	@GetMapping("/image/{id}")
	public ResponseEntity<byte[]> obtenerImagenPorId(@PathVariable Integer id) throws NotFoundException {
		byte[] imagen = rSrv.obtenerImagenPorId(id).getContenido();

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.IMAGE_JPEG);

		return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
	}

	@GetMapping("/property/{id}")
	public Optional<Property> obtenerPropertyPorId(@PathVariable Integer id) throws NotFoundException {
		return rSrv.getPropById(id);
	}

	@GetMapping("/user/{id}")
	public User obtenerUserPorId(@PathVariable Integer id) throws NotFoundException {
		
		User user = uSrv.getUserById(id).get();
		
		user.setPassword(null);
		user.setToken(null);
		user.setEmail(null);
		
		return user;
	}

}

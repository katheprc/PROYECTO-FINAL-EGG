package com.grupo1.PROYECTOFINALEGG.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.PROYECTOFINALEGG.Entity.Admin;
import com.grupo1.PROYECTOFINALEGG.Entity.Booking;
import com.grupo1.PROYECTOFINALEGG.Entity.Client;
import com.grupo1.PROYECTOFINALEGG.Entity.Owner;
import com.grupo1.PROYECTOFINALEGG.Entity.Property;
import com.grupo1.PROYECTOFINALEGG.Entity.Role;
import com.grupo1.PROYECTOFINALEGG.Entity.DTOS.AdminDTO;
import com.grupo1.PROYECTOFINALEGG.Entity.DTOS.ClientDTO;
import com.grupo1.PROYECTOFINALEGG.Entity.DTOS.OwnerDTO;
import com.grupo1.PROYECTOFINALEGG.Entity.DTOS.UserDTO;
import com.grupo1.PROYECTOFINALEGG.Exceptions.MyException;
import com.grupo1.PROYECTOFINALEGG.Repositories.UserRepository;
import com.grupo1.PROYECTOFINALEGG.Utilities.Utility;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository uRepo;

	@Autowired
	RentalService rSrv;

	public com.grupo1.PROYECTOFINALEGG.Entity.User find(String email) {

		com.grupo1.PROYECTOFINALEGG.Entity.User user = uRepo.findByEmail(email);
		return user;
	}

	@Transactional
	public void updateUserProfile(com.grupo1.PROYECTOFINALEGG.Entity.User user, String username, String lastname, MultipartFile imagen,
			HttpServletRequest request) throws MyException {

		if (username != null && !(username.isEmpty()) && !(username.equals(""))) {
			user.setUsername(username);
		}
		if (lastname != null && !(lastname.isEmpty()) && !(lastname.equals(""))) {
			user.setLastname(lastname);

		}

		if (imagen != null && !(imagen.isEmpty()) && (imagen.getSize() > 0)) {
			Integer num = rSrv.subirImagen(imagen);
			user.addImg(Utility.getSiteUrl(request) + "/api/image/" + num);
		}

		uRepo.save(user);

	}

	@Transactional
	public void registrar(String username, String apellido, String email, String password, String password2,
			String type, MultipartFile imagen, HttpServletRequest request) throws MyException {

		validar(username, apellido, email, password, password2);

		if (type.equals("OWNER")) {

			Owner user = new Owner();

			user.setUsername(username);
			user.setLastname(apellido);
			user.setEmail(email);
			user.setPassword(new BCryptPasswordEncoder().encode(password)); // codificado
			user.setRole(Role.USER);
			Integer num = rSrv.subirImagen(imagen);
			user.addImg(Utility.getSiteUrl(request) + "/api/image/" + num);

			uRepo.save(user);

		} else if (type.equals("CLIENT")) {

			Client user = new Client();

			user.setUsername(username);
			user.setLastname(apellido);
			user.setEmail(email);
			user.setPassword(new BCryptPasswordEncoder().encode(password)); // codificado
			user.setRole(Role.USER);

			Integer num = rSrv.subirImagen(imagen);
			user.addImg(Utility.getSiteUrl(request) + "/api/image/" + num);
			uRepo.save(user);
		} else if (type.equals("ADMIN")) {

			Admin user = new Admin();

			user.setUsername(username);
			user.setLastname(apellido);
			user.setEmail(email);
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode(password));

			Integer num = rSrv.subirImagen(imagen);
			user.addImg(Utility.getSiteUrl(request) + "/api/image/" + num);
			uRepo.save(user);

		} else {
			throw new MyException("ERROR EN SISTEMA, INTENTA NUEVAMENTE");
		}

	}

	@Transactional
	public void actualizar(Integer id, String username, String apellido, String email, String password,
			String password2) throws MyException {

		validar(username, apellido, email, password, password2);

		Optional<com.grupo1.PROYECTOFINALEGG.Entity.User> respuesta = uRepo.findById(id);
		if (respuesta.isPresent()) {

			com.grupo1.PROYECTOFINALEGG.Entity.User user = respuesta.get();
			user.setUsername(username);
			user.setEmail(email);

			user.setPassword(new BCryptPasswordEncoder().encode(password));

			user.setRole(Role.USER);

			uRepo.save(user);
		}

	}

	@SuppressWarnings("deprecation")
	public UserDTO getOne(Integer id) {
		return convertirUserDTO(uRepo.getOne(id));
	}

	public List<UserDTO> listarUsuarios() {

		List<UserDTO> usersdto = convertirListaDTO(uRepo.findAll());

		return usersdto;

	}

	@Transactional
	public void cambiarRol(Integer id, String type) {
		Optional<com.grupo1.PROYECTOFINALEGG.Entity.User> respuesta = uRepo.findById(id);

		if (respuesta.isPresent()) {

			com.grupo1.PROYECTOFINALEGG.Entity.User user = respuesta.get();

			Admin admin = new Admin();
			Owner owner = new Owner();
			Client client = new Client();

			if (type.equals("ADMIN")) {
				if ((user instanceof Owner) || (user instanceof Client)) {
					admin.setEmail(user.getEmail());
					admin.setImg(user.getImg());
					admin.setLastname(user.getLastname());
					admin.setPassword(user.getPassword());
					admin.setUsername(user.getUsername());
					admin.setToken(user.getToken());
					admin.setRole(Role.ADMIN);
					deleteUser(user.getId());
					uRepo.save(admin);
				}

			} else if (type.equals("OWNER")) {
				if ((user instanceof Admin) || (user instanceof Client)) {
					owner.setEmail(user.getEmail());
					owner.setImg(user.getImg());
					owner.setLastname(user.getLastname());
					owner.setPassword(user.getPassword());
					owner.setUsername(user.getUsername());
					owner.setToken(user.getToken());
					owner.setRole(Role.USER);
					deleteUser(user.getId());
					uRepo.save(owner);
				}

			} else if (type.equals("CLIENT")) {
				if ((user instanceof Admin) || (user instanceof Owner)) {
					client.setEmail(user.getEmail());
					client.setImg(user.getImg());
					client.setLastname(user.getLastname());
					client.setPassword(user.getPassword());
					client.setUsername(user.getUsername());
					client.setToken(user.getToken());
					client.setRole(Role.USER);
					deleteUser(user.getId());
					uRepo.save(client);
				}

			}

		}
	}

	private void validar(String nombre, String apellido, String email, String password, String password2)
			throws MyException {

		if (nombre.isEmpty() || nombre == null) {
			throw new MyException("El nombre no puede ser nulo o estar vacío");
		}
		if (email.isEmpty() || email == null) {
			throw new MyException("El email no puede ser nulo o estar vacio");
		}
		if (uRepo.findByEmail(email) != null) {
			throw new MyException("El email ya existe, Inicia sesion!");
		}
		if (password.isEmpty() || password == null || password.length() <= 5) {
			throw new MyException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
		}
		if (!password.equals(password2)) {
			throw new MyException("Las contraseñas ingresadas deben ser iguales");
		}
		if (apellido.isEmpty() || apellido == null) {
			throw new MyException("El apellido no puede ser nulo o estar vacío");
		}
		if (!(password.equals(password2))) {
			throw new MyException("Las contraseñas ingresadas deben ser iguales");
		}

	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		com.grupo1.PROYECTOFINALEGG.Entity.User usuario = uRepo.findByEmail(email);

		if (usuario != null) {

			List<GrantedAuthority> permisos = new ArrayList<>();

			GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRole().toString());

			permisos.add(p);

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes(); // casteo

			HttpSession session = attr.getRequest().getSession(true);

			session.setAttribute("usuariosession", usuario); // tipo de usuario

			return new User(usuario.getEmail(), usuario.getPassword(), permisos);
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
		}

	}

	public void updateResetPassword(String token, String email) {
		com.grupo1.PROYECTOFINALEGG.Entity.User user = uRepo.findByEmail(email);

		if (user != null) {
			user.setToken(token);
			uRepo.save(user);
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
		}
	}

	public com.grupo1.PROYECTOFINALEGG.Entity.User get(String token) {
		return uRepo.findByToken(token);
	}

	public void updatePassword(com.grupo1.PROYECTOFINALEGG.Entity.User user, String newPassword) {
		BCryptPasswordEncoder passEnc = new BCryptPasswordEncoder();
		String encodePassword = passEnc.encode(newPassword);

		user.setPassword(encodePassword);

		user.setToken(null);

		uRepo.save(user);
	}

	public void updateUser(com.grupo1.PROYECTOFINALEGG.Entity.User user) {
		uRepo.save(user);
	}

	public void updateUserProperty(Owner user, Property prop) {
		user.addProperty(rSrv.getProp(prop.getId()));
		updateUser(user);

	}

	public Optional<com.grupo1.PROYECTOFINALEGG.Entity.User> getUserById(Integer id) {
		return uRepo.findById(id);
	}

	public void deleteUser(int id) {
		try {
			uRepo.deleteById(id);
		} catch (Exception e) {
			e.fillInStackTrace();
		}

	}

	public List<UserDTO> busquedaPersonalizada(String type, String order) {
		List<com.grupo1.PROYECTOFINALEGG.Entity.User> users = new ArrayList<>();

		if (type.equals("CUALQUIERA")) {
			if (order.equals("ASC")) {
				users = uRepo.findAllAsc();
			} else {
				users = uRepo.findAllDesc();
			}
		} else {
			if (order.equals("ASC")) {
				users = uRepo.findByTypeOrderAsc(type);
			} else {
				users = uRepo.findByTypeOrderDesc(type);
			}
		}

		List<UserDTO> usersdto = convertirListaDTO(users);

		return usersdto;
	}

	public List<UserDTO> convertirListaDTO(List<com.grupo1.PROYECTOFINALEGG.Entity.User> users) {

		List<UserDTO> usersDTO = new ArrayList<>();

		for (com.grupo1.PROYECTOFINALEGG.Entity.User user : users) {

			if (user instanceof Client) {
				ClientDTO userdto = new ClientDTO(user.getId(), user.getUsername(), user.getLastname(), user.getEmail(),
						user.getType(), user.getImg(), ((Client) user).getBookings(), ((Client) user).getPosts());
				usersDTO.add(userdto);
			} else if (user instanceof Owner) {
				OwnerDTO userdto = new OwnerDTO(user.getId(), user.getUsername(), user.getLastname(), user.getEmail(),
						user.getType(), user.getImg(), ((Owner) user).getProperties());
				usersDTO.add(userdto);
			} else if (user instanceof Admin) {
				AdminDTO userdto = new AdminDTO(user.getId(), user.getUsername(), user.getLastname(), user.getEmail(),
						user.getType(), user.getImg());
				usersDTO.add(userdto);
			}

		}

		return usersDTO;

	}

	public UserDTO convertirUserDTO(com.grupo1.PROYECTOFINALEGG.Entity.User user) {

		if (user instanceof Client) {
			ClientDTO userdto = new ClientDTO(user.getId(), user.getUsername(), user.getLastname(), user.getEmail(),
					user.getType(), user.getImg(), ((Client) user).getBookings(), ((Client) user).getPosts());
			return userdto;
		} else if (user instanceof Owner) {
			OwnerDTO userdto = new OwnerDTO(user.getId(), user.getUsername(), user.getLastname(), user.getEmail(),
					user.getType(), user.getImg(), ((Owner) user).getProperties());
			return userdto;
		} else if (user instanceof Admin) {
			AdminDTO userdto = new AdminDTO(user.getId(), user.getUsername(), user.getLastname(), user.getEmail(),
					user.getType(), user.getImg());
			return userdto;
		}

		return null;

	}

	public List<com.grupo1.PROYECTOFINALEGG.Entity.User> ultimosRegistros() {

		List<com.grupo1.PROYECTOFINALEGG.Entity.User> lista = uRepo.findAllDesc();
		List<com.grupo1.PROYECTOFINALEGG.Entity.User> lista2 = new ArrayList<>();
		int cont = 0;
		for (com.grupo1.PROYECTOFINALEGG.Entity.User user : lista) {
			cont++;
			if (cont < 6) {
				lista2.add(user);
			}
		}
		return lista2;
	}

	public void updateUserBooking(Client user, Booking book) {
		user.addBookings(book);
		updateUser(user);
	}

}

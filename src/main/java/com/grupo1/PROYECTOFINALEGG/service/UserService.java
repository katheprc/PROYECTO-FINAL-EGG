package com.grupo1.PROYECTOFINALEGG.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.userdetails.User;

import com.grupo1.PROYECTOFINALEGG.Entity.Role;
import com.grupo1.PROYECTOFINALEGG.Exceptions.MyException;
import com.grupo1.PROYECTOFINALEGG.Repositories.UserRepository;

@Service
public class UserService implements UserDetailsService
	{

	@Autowired
	private UserRepository uRepo;



	@Transactional
	public void registrar(String username, String apellido, String email, String password, String password2)
			throws MyException {

		validar(username, apellido, email, password, password2);

		com.grupo1.PROYECTOFINALEGG.Entity.User user = new com.grupo1.PROYECTOFINALEGG.Entity.User();

		user.setUsername(username);
		user.setLastname(apellido);
		user.setEmail(email);

		user.setPassword(new BCryptPasswordEncoder().encode(password)); // codificado

		user.setRole(Role.USER);

		uRepo.save(user);
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
	public com.grupo1.PROYECTOFINALEGG.Entity.User getOne(Integer id) {
		return uRepo.getOne(id);
	}

	@Transactional(readOnly = true)
	public List<com.grupo1.PROYECTOFINALEGG.Entity.User> listarUsuarios() {

		List<com.grupo1.PROYECTOFINALEGG.Entity.User> users = new ArrayList<>();

		users = uRepo.findAll();

		return users;
	}

	@Transactional
	public void cambiarRol(Integer id) {
		Optional<com.grupo1.PROYECTOFINALEGG.Entity.User> respuesta = uRepo.findById(id);

		if (respuesta.isPresent()) {

			com.grupo1.PROYECTOFINALEGG.Entity.User user = respuesta.get();

			if (user.getRole().equals(Role.USER)) {

				user.setRole(Role.ADMIN);

			} else if (user.getRole().equals(Role.ADMIN)) {
				user.setRole(Role.USER);
			}
		}
	}

	private void validar(String nombre, String apellido, String email, String password, String password2) throws MyException {

		if (nombre.isEmpty() || nombre == null) {
			throw new MyException("El nombre no puede ser nulo o estar vacío");
		}
		if (email.isEmpty() || email == null ) {
			throw new MyException("El email no puede ser nulo o estar vacio");
		}
		if(uRepo.findByEmail(email) != null) {
			throw new MyException("El email ya existe, Inicia sesion!");
		}
		if (password.isEmpty() || password == null || password.length() <= 5) {
			throw new MyException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
		}
		if (!password.equals(password2)) {
			throw new MyException("Las contraseñas ingresadas deben ser iguales");
		}
		if(apellido.isEmpty() || apellido == null) {
			throw new MyException("El apellido no puede ser nulo o estar vacío");
		}

	}

	  @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        
		  com.grupo1.PROYECTOFINALEGG.Entity.User usuario = uRepo.findByEmail(email);
	        
	        if (usuario != null) {
	            
	            List<GrantedAuthority> permisos = new ArrayList<>();
	            
	            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ usuario.getRole().toString());
	            
	            permisos.add(p);
	   
	            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes(); //casteo
	            
	            HttpSession session = attr.getRequest().getSession(true);
	            
	            session.setAttribute("usuariosession", usuario); //tipo de usuario
	            
	            return new User(usuario.getEmail(), usuario.getPassword(),permisos);
	        }else{
	        	throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
	        }

	    } 
	  
	  public void updateResetPassword(String token, String email) {
		  com.grupo1.PROYECTOFINALEGG.Entity.User user = uRepo.findByEmail(email);
		
		  if(user != null) {
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
}

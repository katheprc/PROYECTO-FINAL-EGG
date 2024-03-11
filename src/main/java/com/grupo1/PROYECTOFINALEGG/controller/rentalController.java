package com.grupo1.PROYECTOFINALEGG.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.grupo1.PROYECTOFINALEGG.Entity.User;
import com.grupo1.PROYECTOFINALEGG.Exceptions.MyException;
import com.grupo1.PROYECTOFINALEGG.service.RentalService;
import com.grupo1.PROYECTOFINALEGG.service.UserService;


@Controller
@RequestMapping("/")
public class rentalController {

	@Autowired
	RentalService rSrv;
	
	@Autowired
	UserService uSrv;
	
	@GetMapping("/")
	public String home() {
		return "index.html";
	}
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "index.html";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register.html";
	}
	
	@PostMapping("/registerSuccess")
    public String registro(@RequestParam String username, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam String apellido, @RequestParam String type, ModelMap modelo){
        
        try {
            uSrv.registrar(username, apellido, email, password, password2, type);
            
            modelo.put("exito", "Usuario registrado con Exito! :D");
            return "index.html";
            
        } catch (MyException ex) {
            
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", username);
            modelo.put("email", email);
            
            return "register.html";
        }
    }   
	
	 @GetMapping("/inicio")
	    public String inicio(HttpSession session) {
	        //datos de la sesion usuarioservicio
	        User logueado = (User) session.getAttribute("usuariosession");
	        
	        //validar sea rol ADMIN
	        if (logueado.getRole().toString().equals("ADMIN")) {
	            return "redirect:/admin/dashboard";
	        }
	        
	           return "inicio.html";
	    }
	    
	    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	    @GetMapping("/perfil")
	    public String perfil (ModelMap modelo, HttpSession session){
	    	User usuario = (User) session.getAttribute("usuariosession");
	        modelo.put("usuario",usuario);
	        
	        return "usuario_modificar.html";
	    }
	    
	    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	    @PostMapping("/perfil/{id}")
	 
	    public String actualizar(@PathVariable Integer id, @RequestParam String username, @RequestParam String apellido, @RequestParam String email,
	            @RequestParam String password, @RequestParam String password2, ModelMap modelo)
	    {
	        try {
	        uSrv.actualizar(id, username, apellido, email, password, password2);
	        
	        modelo.put("exito","Usuario Actualizado correctamente!");
	        
	        return "inicio.html";
	        } catch (MyException ex){

	        modelo.put("error", ex.getMessage());
	        modelo.put("username", username);
	        //modelo.put("email", email);
	        
	        return "usuario_modificar.html";
	        }

	    }
	
	

	
}

package com.grupo1.PROYECTOFINALEGG.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.grupo1.PROYECTOFINALEGG.Entity.Owner;
import com.grupo1.PROYECTOFINALEGG.Entity.Property;
import com.grupo1.PROYECTOFINALEGG.Entity.Service;
import com.grupo1.PROYECTOFINALEGG.Entity.User;
import com.grupo1.PROYECTOFINALEGG.Exceptions.MyException;
import com.grupo1.PROYECTOFINALEGG.Utilities.Utility;
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
	public String dashboard(Model model) {

		model.addAttribute("userType", getUserType());
		return "index.html";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		model.addAttribute("user", getUser());
		model.addAttribute("userType", getUserType());
		return "profile.html";
	}

	@GetMapping("/login")
	public String login() {
		return "login.html";
	}

	@GetMapping("/register")
	public String register() {
		return "register.html";
	}

	@GetMapping("/error")
	public String error(Model model) {
		model.addAttribute("userType", getUserType());
		return "error.html";
	}

	@PostMapping("/registerSuccess")
	public String registro(@RequestParam String username, @RequestParam String email, @RequestParam String password,
			@RequestParam String password2, @RequestParam String apellido, @RequestParam String type,
			@RequestParam("imagen") MultipartFile imagen, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			uSrv.registrar(username, apellido, email, password, password2, type, imagen, request);

			model.put("exito", "Usuario registrado con Exito! :D");
			return "index.html";

		} catch (MyException ex) {

			model.put("error", ex.getMessage());
			model.put("nombre", username);
			model.put("email", email);

			return "register.html";
		}
	}

	@GetMapping("/register-property")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public String registerProperty(Model model) {
		model.addAttribute("userType", getUserType());

		return "registerProperty.html";
	}

	@PostMapping("/register-property")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public String registerProperty(@RequestParam String propertyName, @RequestParam String details,
			@RequestParam("imagenes") MultipartFile[] imagenes, @RequestParam Double pricePerDay,
			HttpServletRequest request, HttpServletResponse response, Model model)
			throws ServletException, IOException {

		String[] names = request.getParameterValues("nombre[]");
		String[] prices = request.getParameterValues("precio[]");

		Property property = new Property();

		List<Service> services = new ArrayList<>();

		if (names != null && prices != null && names.length == prices.length) {
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				double price = Double.parseDouble(prices[i]);
				services.add(new Service(name, price));
			}

		}

		property.setDetails(details);
		property.setName(propertyName);

		property.setPricePerDay(pricePerDay);

		property.setOwner(Utility.getSiteUrl(request) + "/api/user/" + ((Owner) getUser()).getId());

		for (Service srv : services) {
			rSrv.saveServ(srv);
			property.addSrv(rSrv.getSrv(srv));
		}

		for (MultipartFile img : imagenes) {
			Integer num = rSrv.subirImagen(img);
			property.addImg(Utility.getSiteUrl(request) + "/api/image/" + num);
		}

		rSrv.saveProp(property);

		uSrv.updateUserProperty((Owner) getUser(),
				Utility.getSiteUrl(request) + "/api/property/" + rSrv.getProp(property).getId());

		model.addAttribute("userType", getUserType());
		return "registerProperty.html";
	}

	/*
	 * @GetMapping("/inicio") public String inicio(HttpSession session) { // datos
	 * de la sesion usuarioservicio User logueado = (User)
	 * session.getAttribute("usuariosession");
	 * 
	 * // validar sea rol ADMIN if (logueado.getRole().toString().equals("ADMIN")) {
	 * return "redirect:/admin/dashboard"; }
	 * 
	 * return "inicio.html"; }
	 * 
	 * @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	 * 
	 * @GetMapping("/perfil") public String perfil(ModelMap modelo, HttpSession
	 * session) { User usuario = (User) session.getAttribute("usuariosession");
	 * modelo.put("usuario", usuario);
	 * 
	 * return "usuario_modificar.html"; }
	 * 
	 * @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	 * 
	 * @PostMapping("/perfil/{id}")
	 * 
	 * public String actualizar(@PathVariable Integer id, @RequestParam String
	 * username, @RequestParam String apellido,
	 * 
	 * @RequestParam String email, @RequestParam String password, @RequestParam
	 * String password2, ModelMap modelo) { try { uSrv.actualizar(id, username,
	 * apellido, email, password, password2);
	 * 
	 * modelo.put("exito", "Usuario Actualizado correctamente!");
	 * 
	 * return "inicio.html"; } catch (MyException ex) {
	 * 
	 * modelo.put("error", ex.getMessage()); modelo.put("username", username); //
	 * modelo.put("email", email);
	 * 
	 * return "usuario_modificar.html"; }
	 * 
	 * }
	 * 
	 */

	private String getUserType() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userDetails = authentication.getName();

		String userType = uSrv.find(userDetails).getType();

		return userType;
	}

	private User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userDetails = authentication.getName();

		return uSrv.find(userDetails);
	}

	@GetMapping("/property/{id}")
	public String propertyPage(@PathVariable Integer id, Model model) throws NotFoundException {

		Property property = rSrv.getPropById(id).get();

		model.addAttribute("propertyDetails", property);
		model.addAttribute("propertyImgs", property.getImgs());
		return "sigle-page-arlquileres.html";
	}

	@GetMapping("/admin/dashboard")
	public String dashboardAdmin(Model model) {
		model.addAttribute("listaUsuarios", uSrv.listarUsuarios());
		model.addAttribute("userType", getUserType());

		return "dashboardAdmin.html";
	}

	@PostMapping("/admin/dashboard/")
	public String editRole(@RequestParam("id") String id, @RequestParam("type") String type, Model model) {
		uSrv.cambiarRol(Integer.parseInt(id), type);
		model.addAttribute("listaUsuarios", uSrv.listarUsuarios());
		return "dashboardAdmin.html";
	}

	@PostMapping("/admin/dashboard/delete")
	public String deleteUser(@RequestParam("id") String id, Model model) {
		uSrv.deleteUser(Integer.parseInt(id));
		model.addAttribute("listaUsuarios", uSrv.listarUsuarios());
		model.addAttribute("userType", getUserType());
		return "dashboardAdmin.html";
	}

	@GetMapping("/admin/dashboard/buscar")
	public String buscarDashboardAdmin(@RequestParam("type") String type, @RequestParam("order") String order,
			Model model) {
		model.addAttribute("userType", getUserType());
		model.addAttribute("listaUsuarios", uSrv.busquedaPersonalizada(type, order));
		return "dashboardAdmin.html";
	}
}

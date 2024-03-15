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
import org.springframework.web.servlet.view.RedirectView;

import com.grupo1.PROYECTOFINALEGG.Entity.Owner;
import com.grupo1.PROYECTOFINALEGG.Entity.Property;
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
	public String dashboard() {
		return "dashboard.html";

	public RedirectView dashboard(Model model) {

		String type = getUserType();

		if (type.equals("ADMIN")) {
			return new RedirectView("/admin/dashboard/users", true);
		}

		model.addAttribute("userType", type);
		return new RedirectView("/dashboard", true);
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

			return "login.html";

		} catch (MyException ex) {

			model.put("error", ex.getMessage());

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
			@RequestParam String propertyAddress, @RequestParam String propertyLocalidad,
			@RequestParam String propertyProvincia, HttpServletRequest request, HttpServletResponse response,
			Model model) throws ServletException, IOException {

		String address = propertyAddress + ", " + propertyLocalidad + ", " + propertyProvincia;

		registerProperty(propertyName, address, details, imagenes, pricePerDay, request, response, model);

		model.addAttribute("userType", getUserType());
		return "registerProperty.html";
	}

	@GetMapping("/property/{id}")
	public String propertyPage(@PathVariable Integer id, Model model) throws NotFoundException {

		model.addAttribute("propertyDetails", getProperty(id));
		model.addAttribute("propertyImgs", getProperty(id).getImgs());
		return "sigle-page-arlquileres.html";

	}

	// ----------------------------------------------------ADMIN----------------------------------------------------------

	@GetMapping("/admin/dashboard")
	public RedirectView dashboardAdmin(Model model) {
		return new RedirectView("/admin/dashboard/users", true);
	}

	// -------------MAPPINGS USER-------------

	@GetMapping("/admin/dashboard/users")
	public String users(Model model) {
		model.addAttribute("listaUsuarios", uSrv.listarUsuarios());
		model.addAttribute("userType", getUserType());
		model.addAttribute("usersBoolean", true);
		model.addAttribute("postsBoolean", false);
		model.addAttribute("propertiesBoolean", false);
		return "dashboardAdmin.html";
	}

	@PostMapping("/admin/dashboard/users/edit")
	public RedirectView editRoleUsers(@RequestParam("id") String id, @RequestParam("type") String type, Model model) {
		uSrv.cambiarRol(Integer.parseInt(id), type);
		return new RedirectView("/admin/dashboard/users", true);
	}

	@PostMapping("/admin/dashboard/users/delete")
	public RedirectView deleteUsers(@RequestParam("id") String id, Model model) {
		uSrv.deleteUser(Integer.parseInt(id));
		return new RedirectView("/admin/dashboard/users", true);
	}

	@PostMapping("/admin/dashboard/users")
	public String buscarUsers(@RequestParam("type") String type, @RequestParam("order") String order, Model model) {
		model.addAttribute("userType", getUserType());
		model.addAttribute("listaUsuarios", uSrv.busquedaPersonalizada(type, order));
		model.addAttribute("usersBoolean", true);
		model.addAttribute("postsBoolean", false);
		model.addAttribute("propertiesBoolean", false);
		return "dashboardAdmin.html";
	}

	// -------------FIN MAPPINGS USER-------------
	// ------------------------------------------------
	// -------------MAPPINGS POSTS-------------
	@GetMapping("/admin/dashboard/posts")
	public String posts(Model model) {
		model.addAttribute("listaPosts", rSrv.getAllPosts());
		model.addAttribute("userType", getUserType());
		model.addAttribute("usersBoolean", false);
		model.addAttribute("postsBoolean", true);
		model.addAttribute("propertiesBoolean", false);
		return "dashboardAdmin.html";
	}

	@PostMapping("/admin/dashboard/posts/delete")
	public RedirectView deletePosts(@RequestParam("id") String id, Model model) {
		rSrv.deletePost(Integer.parseInt(id));
		return new RedirectView("/admin/dashboard/posts", true);
	}

	@PostMapping("/admin/dashboard/posts")
	public String buscarPosts(@RequestParam("rating") String rating, @RequestParam("order") String order, Model model) {
		model.addAttribute("userType", getUserType());
		model.addAttribute("listaPosts", rSrv.getPosts(order, rating));
		model.addAttribute("usersBoolean", false);
		model.addAttribute("postsBoolean", true);
		model.addAttribute("propertiesBoolean", false);
		return "dashboardAdmin.html";
	}

	// -------------FIN MAPPINGS POSTS-------------
	// ------------------------------------------------
	// -------------MAPPINGS PROPERTIES-------------
	@GetMapping("/admin/dashboard/properties")
	public String properties(Model model) {
		model.addAttribute("listaUsuarios", uSrv.listarUsuarios());
		model.addAttribute("userType", getUserType());
		model.addAttribute("usersBoolean", false);
		model.addAttribute("postsBoolean", false);
		model.addAttribute("propertiesBoolean", true);
		return "dashboardAdmin.html";
	}

	@PostMapping("/admin/dashboard/properties/delete")
	public RedirectView deleteProperties(@RequestParam("id") String id, Model model) {
		rSrv.deleteProperty(Integer.parseInt(id));
		return new RedirectView("/admin/dashboard/properties", true);
	}

	@PostMapping("/admin/dashboard/properties")
	public String buscarProperties(@RequestParam("type") String type, @RequestParam("order") String order,
			Model model) {
		model.addAttribute("userType", getUserType());
		model.addAttribute("listaProperties", rSrv.getProperties(type, order));
		model.addAttribute("usersBoolean", false);
		model.addAttribute("postsBoolean", false);
		model.addAttribute("propertiesBoolean", true);
		return "dashboardAdmin.html";
	}
	// -------------FIN MAPPINGS PROPERTIES-------------

	// ----------------------------------------------------FIN
	// ADMIN----------------------------------------------------------

	public String getUserType() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userDetails = authentication.getName();
		String userType = uSrv.find(userDetails).getType();
		return userType;
	}

	public User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userDetails = authentication.getName();
		return uSrv.find(userDetails);
	}

	public void registerProperty(String propertyName, String propertyAddress, String details, MultipartFile[] imagenes,
			Double pricePerDay, HttpServletRequest request, HttpServletResponse response, Model model)
			throws ServletException, IOException {
		String[] names = request.getParameterValues("nombre[]");
		String[] prices = request.getParameterValues("precio[]");

		Property property = new Property();

		List<com.grupo1.PROYECTOFINALEGG.Entity.Service> services = new ArrayList<>();

		if (names != null && prices != null && names.length == prices.length) {
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				double price = Double.parseDouble(prices[i]);
				services.add(new com.grupo1.PROYECTOFINALEGG.Entity.Service(name, price));
			}

		}
		property.setAddress(propertyAddress);
		property.setDetails(details);
		property.setName(propertyName);
		property.setPricePerDay(pricePerDay);

		for (com.grupo1.PROYECTOFINALEGG.Entity.Service srv : services) {
			rSrv.saveServ(srv);
			property.addSrv(rSrv.getSrv(srv));
		}

		for (MultipartFile img : imagenes) {
			Integer num = rSrv.subirImagen(img);
			property.addImg(Utility.getSiteUrl(request) + "/api/image/" + num);
		}

		rSrv.saveProp(property);

		uSrv.updateUserProperty((Owner) getUser(), rSrv.getProp(property));
	}

	public Property getProperty(Integer id) {
		return rSrv.getPropById(id).get();
	}

}

}

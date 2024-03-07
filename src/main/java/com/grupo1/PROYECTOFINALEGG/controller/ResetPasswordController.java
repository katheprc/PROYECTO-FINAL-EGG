package com.grupo1.PROYECTOFINALEGG.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.grupo1.PROYECTOFINALEGG.Entity.User;
import com.grupo1.PROYECTOFINALEGG.Utilities.Utility;
import com.grupo1.PROYECTOFINALEGG.service.UserService;

import net.bytebuddy.utility.RandomString;

@Controller
public class ResetPasswordController {

	@Autowired
	private UserService uSrv;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@GetMapping("/forgot-password")
	public String showForgotPasswordForm() {
		return "forgot-password.html";
	}

	@PostMapping("/forgot-password")
	public String processForgotPasswordForm(HttpServletRequest request, Model model) throws Exception {

		String email = request.getParameter("email");
		String token = RandomString.make(45);

		try {
			uSrv.updateResetPassword(token, email);

			String link = Utility.getSiteUrl(request) + "/reset-password?token=" + token;

			sendEmail(email, link);

			model.addAttribute("exito", "Mail envíado correctamente");

			// send email
		} catch (UsernameNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error mientras se enviaba el email.");
		}

		return "redirect:/login?resetPasswordSuccess";
	}

	@GetMapping("/reset-password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {

		User user = uSrv.get(token);

		if (user == null) {

			model.addAttribute("message", "Invalid Token");

			return "message";

		}

		model.addAttribute("token", token);

		return "reset-password";

	}

	@PostMapping("/reset-password")
	public String processResetPassword(HttpServletRequest request, Model model) {

		String token = request.getParameter("token");
		String password = request.getParameter("password");

		User user = uSrv.get(token);

		if (user == null) {

			model.addAttribute("message", "Invalid Token");

			return "message";

		} else {

			uSrv.updatePassword(user, password);
			model.addAttribute("message", "Contraseña modificada correctamente!");
		}

		return "index.html";
	}

	private void sendEmail(String email, String link) throws Exception {
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);

		helper.setFrom("contact@quinchosegg.com", "Soporte Alquiler Quinchos Egg");
		helper.setTo(email);

		String subject = "Solicitud de restablecimiento de contraseña";

		String content = "<p>Hola!</p>" + "<p>Has solicitado el restablecimiento de tu contraseña en nuestro sitio</p>"
				+ "<p>Has click en el link para cambiar tu contraseña: </p>" + "<p><b><a href=\"" + link
				+ "\">Cambiar la contraseña</a></b></p>"
				+ "<p>Ignora este mail si no solicitaste el restablecimiento o si recuerdas la contraseña</p>";

		helper.setSubject(subject);
		helper.setText(content, true);

		mailSender.send(msg);
	}
}

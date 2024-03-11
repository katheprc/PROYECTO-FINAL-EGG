package com.grupo1.PROYECTOFINALEGG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class ProyectoFinalEggApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalEggApplication.class, args);
	}

}

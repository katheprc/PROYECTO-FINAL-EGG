package com.grupo1.PROYECTOFINALEGG.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.grupo1.PROYECTOFINALEGG.service.UserService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public UserService uSrv;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(uSrv).passwordEncoder(new BCryptPasswordEncoder()).and().inMemoryAuthentication()
				.withUser("admin").password("{noop}admin").roles("ADMIN").and().withUser("usuario")
				.password("{noop}user").roles("USER");
		;
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().antMatchers("/api/admin/**", "/admin/**").hasRole("ADMIN")
				// .antMatchers("/api/**").authenticated()
				.antMatchers("/", "/login", "/register").permitAll().and().httpBasic().and()
				.formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/").usernameParameter("email")
						.passwordParameter("password"))
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login")).csrf(csrf -> {
					try {
						csrf.disable().exceptionHandling(handling -> handling.accessDeniedPage("/access-denied"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}
}

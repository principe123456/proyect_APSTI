package com.example.proyect_APSTI;

import com.example.proyect_APSTI.model.Usuario;
import com.example.proyect_APSTI.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
		"APP_LOGIN_USERNAME=admin-prueba@correo.com",
		"APP_LOGIN_PASSWORD=ClavePrueba123!",
		"spring.datasource.url=jdbc:h2:mem:apsti-test;DB_CLOSE_DELAY=-1"
})
@Transactional
class ProyectApstiApplicationTests {

	@Autowired
	private UsuarioRepository repositorio;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

	@Test
	void activeRegisteredUserCanAuthenticateWithStoredPassword() {
		Usuario usuario = new Usuario("Usuario Prueba", "usuario-prueba@correo.com", "USUARIO", "Activo");
		usuario.setContrasena(passwordEncoder.encode("ClavePrueba123!"));
		repositorio.save(usuario);

		UserDetails details = userDetailsService.loadUserByUsername(usuario.getCorreo());

		assertTrue(details.isEnabled());
		assertTrue(passwordEncoder.matches("ClavePrueba123!", details.getPassword()));
		repositorio.delete(usuario);
	}

	@Test
	void inactiveRegisteredUserCannotAuthenticate() {
		Usuario usuario = new Usuario("Usuario Inactivo", "usuario-inactivo@correo.com", "USUARIO", "Inactivo");
		usuario.setContrasena(passwordEncoder.encode("ClavePrueba123!"));
		repositorio.save(usuario);

		UserDetails details = userDetailsService.loadUserByUsername(usuario.getCorreo());

		assertFalse(details.isEnabled());
		repositorio.delete(usuario);
	}

}

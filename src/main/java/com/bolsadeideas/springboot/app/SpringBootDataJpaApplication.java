package com.bolsadeideas.springboot.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;
import com.bolsadeideas.springboot.app.models.services.IUploadFileService;
import com.bolsadeideas.springboot.app.models.services.IUsuarioService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	@Autowired
	IUploadFileService uploadFileService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IUsuarioService usuarioService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// uploadFileService.deleteAll();
		// uploadFileService.init();

		// Crea un nuevo usuario administrador y guárdalo en la base de datos
		List<Usuario> users = usuarioService.findAll();
		if (users.isEmpty()) {
			
			String startUsername = "admin";
			String startPassword = "12345";
			List<Role> startRoles = Arrays.asList(new Role("ROLE_ADMIN"), new Role("ROLE_USER"));
			
			Usuario firstUser = new Usuario();
			firstUser.setUsername(startUsername);
			firstUser.setPassword(passwordEncoder.encode(startPassword));
			firstUser.setRoles(startRoles);
			firstUser.setEnabled(true);
			usuarioService.save(firstUser);

			System.out.println("Usuario de Inicio: " + startUsername);
			System.out.println("Contraseña de Inicio: " + startPassword);

		}

	}

}

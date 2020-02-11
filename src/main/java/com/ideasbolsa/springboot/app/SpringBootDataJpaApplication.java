package com.ideasbolsa.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ideasbolsa.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	
	@Autowired
	IUploadFileService uploadFileService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		
		uploadFileService.deleteAll();
		uploadFileService.init();
		
		/*Crea las encrpitaciones para las contraseñas de el usuario ADMIN y el 
		 * usuario andres que son los perfiles autorizados para usar el sistema*/
		String password = "12345";
		for (int i = 0; i < 2; i++) {
			
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
	}

	
}

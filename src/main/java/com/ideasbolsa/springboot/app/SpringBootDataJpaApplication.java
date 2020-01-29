package com.ideasbolsa.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ideasbolsa.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {
/*CommandLineRunner: -->  Interfaz utilizada para indicar que un bean debe ejecutarse cuando está 
 * contenido en SpringApplication. 
 * Se pueden definir múltiples beans CommandLineRunner dentro del mismo contexto de aplicación 
 * y se pueden ordenar utilizando la anotación Orderedinterface o @Order.*/
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	/*Inyectando el servicio IUploadFileService para poder usar sus métodos */
	@Autowired
	IUploadFileService uploadFileService;
	
	/*Método implementado de la interfaz CommandLineRunner*/
	@Override
	public void run(String... args) throws Exception {
		
		uploadFileService.deleteAll();
		uploadFileService.init();
		
	}

	
}

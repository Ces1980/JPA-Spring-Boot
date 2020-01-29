package com.ideasbolsa.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	/*Método para poder cargar la imagen*/
	public Resource load(String filename) throws MalformedURLException;
	
	/*Método que obtiene el nombre del archivo y lo renombra dandole un nombre unico
	 * MultipartFile permite almacenar temporalmente un archivo cargado*/
	public String copy(MultipartFile file) throws IOException;
	
	public boolean delete(String filename);
}

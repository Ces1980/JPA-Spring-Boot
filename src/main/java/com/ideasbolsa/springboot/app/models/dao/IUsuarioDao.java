package com.ideasbolsa.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.ideasbolsa.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

	//Método que permite realizar una consulta a la base de datos usando el nombre del usuario
	public Usuario findByUserName(String username);
}

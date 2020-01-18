package com.ideasbolsa.springboot.app.models.dao;

import java.util.List;

import com.ideasbolsa.springboot.app.models.entity.Cliente;

public interface IClienteDao {
	//Método para listar
	public List<Cliente> findAll();
	//Método para guardar
	public void save(Cliente cliente);
	//Método para generar busqueda por id
	public Cliente findOne(Long id);
	//Método para elminar un registro
	public void delete(Long id);

}

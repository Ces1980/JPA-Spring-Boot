package com.ideasbolsa.springboot.app.models.dao;

import java.util.List;

import com.ideasbolsa.springboot.app.models.entity.Cliente;

public interface IClienteDao {
	
	public List<Cliente> findAll();

}

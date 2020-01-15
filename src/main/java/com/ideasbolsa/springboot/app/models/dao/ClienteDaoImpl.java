package com.ideasbolsa.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ideasbolsa.springboot.app.models.entity.Cliente;


@Repository
public class ClienteDaoImpl implements IClienteDao{

	
	@PersistenceContext
	private EntityManager em;
	
	/*@SuppressWarnings("unchecked") Quita una alerta de la instrucción
	 * em.createQuery("from Cliente").getResultList();
	 */
	@SuppressWarnings("unchecked")
	/*
	 * @Transactional(readOnly = true)Permite que se puedan leer 
	 * y/o escribir los datos de la consulta
	 * */
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> finAll() {
		//Método finAll --> método para listar
		return em.createQuery("from Cliente").getResultList();
	}

}

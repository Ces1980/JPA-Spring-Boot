package com.ideasbolsa.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ideasbolsa.springboot.app.models.entity.Cliente;


@Repository
public class ClienteDaoImpl implements IClienteDao{

	/**
	 * La API EntityManager puede actualizar, recuperar, 
	 * eliminar o aplicar la persistencia de objetos de una base de datos.
	 * 
	 *La API EntityManager y los metadatos de correlación relacional 
	 *de objetos manejan la mayor parte de las operaciones de base de datos 
	 *sin que sea necesario escribir código JDBC o SQL para mantener la persistencia.
	 *
	 *JPA proporciona un lenguaje de consulta, que amplía el lenguaje de consulta 
	 *EJB independiente, conocido también como JPQL, 
	 *el cual puede utilizar para recuperar objetos sin grabar 
	 *consultas SQL específicas en la base de datos con la que está trabajando.
	 *
	 */
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

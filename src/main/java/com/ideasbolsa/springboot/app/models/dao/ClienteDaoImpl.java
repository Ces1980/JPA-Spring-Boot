package com.ideasbolsa.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;


import com.ideasbolsa.springboot.app.models.entity.Cliente;

@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;

	/* Método crear */
	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> findAll() {
		return em.createQuery("from Cliente").getResultList();
	}

	/* Método buscar */
	@Override
	public Cliente findOne(Long id) {
		return em.find(Cliente.class, id);
	}

	/* Método guardar/insertar o editar */
	@Override
	public void save(Cliente cliente) {
		if (cliente.getId() != null && cliente.getId() > 0) {
			/* Método que busca */
			em.merge(cliente);
		} else {
			/* Método que guarda */
			em.persist(cliente);
		}
	}

	/* Método borrar */
	@Override
	public void delete(Long id) {
		em.remove(findOne(id));
	}

}
package com.ideasbolsa.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ideasbolsa.springboot.app.models.entity.Cliente;

@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		/*
		 * Se modifica el método save para que al guardar identifique si existe el id o
		 * que sea mayor de cero para modificar el objeto
		 */
		if (cliente.getId() != null && cliente.getId() > 0) {
			em.merge(cliente);
		} else {
			em.persist(cliente);
		}
	}

	/*
	 * Este metodo busca por id en la base de datos Recibe como parametro el tipo de
	 * dato y el nombre de dato (Long id) y retorna el método find del objeto
	 * declado del tipo EntityManager, como parametro recibe la clase del cliente y
	 * su id
	 */
	@Override
	public Cliente findOne(Long id) {
		return em.find(Cliente.class, id);
	}

}
package com.ideasbolsa.springboot.app.models.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ideasbolsa.springboot.app.models.dao.IClienteDao;
import com.ideasbolsa.springboot.app.models.dao.IFacturaDao;
import com.ideasbolsa.springboot.app.models.dao.IProductoDao;
import com.ideasbolsa.springboot.app.models.entity.Cliente;
import com.ideasbolsa.springboot.app.models.entity.Factura;
import com.ideasbolsa.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	@Autowired
	private IProductoDao productoDao;
	@Autowired
	private IFacturaDao facturaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	public List<Producto> findByNombre(String term) {
	
		return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
	}

	/*Método para salvar la factura*/
	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
	}
	
	/*Método para buscar un producto
	 * */
	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		// TODO Auto-generated method stub
		//En caso de no encontrar el producto la retorno regresara un null
		return productoDao.findById(id).orElse(null);
	}

}

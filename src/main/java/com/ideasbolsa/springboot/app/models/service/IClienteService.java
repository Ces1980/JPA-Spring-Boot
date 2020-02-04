package com.ideasbolsa.springboot.app.models.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.ideasbolsa.springboot.app.models.entity.Cliente;
import com.ideasbolsa.springboot.app.models.entity.Factura;
import com.ideasbolsa.springboot.app.models.entity.Producto;

public interface IClienteService {

	    //Método para listar
		public List <Cliente> findAll();
		//
		public Page <Cliente> findAll(Pageable pageable);
		//Método para guardar
		public void save(Cliente cliente);
		//Método para generar busqueda por id
		public Cliente findOne(Long id);
		//Método para generar una consulta mas limpia non jpa
		public Cliente fetchByWithFacturas(Long id);
		//Método para elminar un registro
		public void delete(Long id);
		//Método para listar un producto
		public List<Producto> findByNombre(String term);
		//Método para salvar factura
		public void saveFactura (Factura factura);
		//Método para buscar producto
		public Producto findProductoById(Long id);
		//Método para buscar facturas
		public Factura findFacturaById(Long id);
		//Método para borrar facturas
		public void deleteFactura(Long id);
		
		public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id);

}

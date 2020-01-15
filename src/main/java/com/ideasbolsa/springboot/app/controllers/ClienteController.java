package com.ideasbolsa.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ideasbolsa.springboot.app.models.dao.IClienteDao;


@Controller
public class ClienteController {

	/**
	 * Se inicializa un objeto de la interfaz IClienteDao
	 * para poder realizar las consultas
	 *
	 */
	@Autowired
	private IClienteDao clienteDao;
	
	/*Se usa requestMapping 
	 * para marcar la ruta , pero de igual forma se puede usar @GetMapping*/
	@RequestMapping(value="/listar", method =RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteDao.finAll());
		return "ver";
	}
}

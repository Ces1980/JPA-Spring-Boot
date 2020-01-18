package com.ideasbolsa.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.ideasbolsa.springboot.app.models.dao.IClienteDao;
import com.ideasbolsa.springboot.app.models.entity.Cliente;


@Controller
@SessionAttributes("cliente")
/*@SessionAttributes("cliente") --> indicamos que se va a guardar en los atributos de la session el objeto
 * que se ha mapeado al formulario, que en este caso es cliente*/
public class ClienteController {

	@Autowired
	private IClienteDao clienteDao;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteDao.findAll());
		return "listar";
	}
	
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		return "form";
	}

	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		Cliente cliente =null;
		if (id > 0) {
			cliente = clienteDao.findOne(id);
		} else {
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return"form";
	}
	
	/*En el método guardar se agrerga un parametro (SessionStatus status) para
	 *que se invoque el método setComplete() y así que el objeto cliente termine la session 
	 *y se ejecute la accion de los métodos, ya sea guardar o eliminar, editar 
	 * */
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente");
			return "form";
		}
		clienteDao.save(cliente);
		status.setComplete();
		return "redirect:listar";
	}
	
	/*
	 * */
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id) {
		if(id > 0) {
			clienteDao.delete(id);
		}
		return "redirect:/listar";
	}
}

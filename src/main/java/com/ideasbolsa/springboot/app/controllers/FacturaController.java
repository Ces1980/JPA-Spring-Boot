package com.ideasbolsa.springboot.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ideasbolsa.springboot.app.models.entity.Cliente;
import com.ideasbolsa.springboot.app.models.entity.Factura;
import com.ideasbolsa.springboot.app.models.service.IClienteService;

@Controller
@RequestMapping("/factura")
/* Permite que la sesión permanezca activa hasta que la factura
 * sea persistida en la base de datos*/
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService  clienteService;
	
	@GetMapping(value = "/form/{clienteId}")
	public String Crear(@PathVariable(value = "clienteId") 
			Long clienteId, Map<String, Object> model,
			RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(clienteId); 
		
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe a la base de datos..!");
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.put("factura", factura);
		model.put("titulo", "Crear Factura");
		
		return "factura/form";
		
	}
}

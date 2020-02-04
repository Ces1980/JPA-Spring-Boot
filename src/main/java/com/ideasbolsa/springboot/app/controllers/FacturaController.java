package com.ideasbolsa.springboot.app.controllers;


import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ideasbolsa.springboot.app.models.entity.Cliente;
import com.ideasbolsa.springboot.app.models.entity.Factura;
import com.ideasbolsa.springboot.app.models.entity.ItemFactura;
import com.ideasbolsa.springboot.app.models.entity.Producto;
import com.ideasbolsa.springboot.app.models.service.IClienteService;



@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;
	 
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/ver/{id}")
	public String ver (@PathVariable(value= "id") Long id,
						Model model, RedirectAttributes flash) {
		/*PAra obtener la factura por el id*/
		Factura factura = clienteService.findFacturaById(id);
		
		/*Para validar la factura*/
		if (factura==null) {
			flash.addFlashAttribute("error", "La factura no existe en la base de datos");
		}
		/*Para pasar la factura a la vista junto con una descripción de la factura*/
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));
		
		return "factura/ver";
	}

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flash) {

		Cliente cliente = clienteService.findOne(clienteId);

		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}

		Factura factura = new Factura();
		factura.setCliente(cliente);

		model.put("factura", factura);
		model.put("titulo", "Crear Factura");

		return "factura/form";
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByNombre(term);
	}
	
//-->Inicia el método guardar	
	
	/*
     *Se ha agregado la anotación @Valid para habilitar la validación en el objeto factura
     *BindingResult para comprobar si existen errores en la validación de la factura
     * */
	@PostMapping(value = "/form")
	public String guardar(@Valid Factura factura,
			              BindingResult result,
			              Model model,
			              @RequestParam(name = "item_id[]", required = false) 
							Long [] itemId, @RequestParam(name="cantidad[]", required = false)
							Integer[] cantidad, RedirectAttributes flash, SessionStatus status) {
		
		/*Condición que nos regresa a la vista factura/form en caso de haber algún error
		 * al llenar la factura*/
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear factura");
			return "factura/form";
		}
		
		/*Condición que indica que si el no hay item en las líneas de captura
		 * NO SE PUEDE ENVIAR LA FACTURA*/
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear factura");
			model.addAttribute("error", "Error: la factura No puede no tener líneas");
			return "factura/form";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			
			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
		}
		
		clienteService.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Factura creada con éxito!");
		/*Se manda a ver para mostrar el listado de las facturas*/
		return "redirect:/ver/" + factura.getCliente().getId();
	}

	//--> fin del método guardar
	/*Método eliminar*/
	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash ) {
		Factura factura = clienteService.findFacturaById(id);
		if (factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con exito");
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		
		flash.addFlashAttribute("error", "La factura no existe en la base de datos, no se pudo eliminar!");
		return "redirect:/listar";
	}
}

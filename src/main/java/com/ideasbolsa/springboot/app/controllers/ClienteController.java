package com.ideasbolsa.springboot.app.controllers;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ideasbolsa.springboot.app.models.entity.Cliente;
import com.ideasbolsa.springboot.app.models.service.IClienteService;
import com.ideasbolsa.springboot.app.util.paginator.PageRender;


@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		return "form";
	}

	/*RedirectAttributes: librería que permite mandar un mensaje cuando se realiza una acción de redireccionamiento  */
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente =null;
		
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", "El Id del cliente no existe en la base de datos!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El Id del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return"form";
	}
	
	/*@RequestParam("file") MultipartFile foto indica el tipo de archivo que resibe de la vista 
	 * y que es pasado como parametro*/
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid  Cliente cliente, BindingResult result, Model model, 
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente");
			return "form";
		}
		/*Cóndición en caso de no subir archivo*/
		if(!foto.isEmpty()) {
			/*Indica donde se va a guardar la foto que se va a subir*/
			Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
			/*Obtener el String del directorio para obtener la ubicación en directorio de la foto*/
			String rootPath = directorioRecursos.toFile().getAbsolutePath();
			try {
				/*Obtener los bytes de las fotos*/
				byte[] bytes = foto.getBytes();
				/*Obtener la ruta completa del archivo*/
				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
				/*Escribir los bytes de un archivo pasando como parametro 
				 * la ubicación completa de la imagen y el tamaño en bytes*/
				Files.write(rutaCompleta,bytes);
				/*Menasje al conbcluir la acción */
				flash.addFlashAttribute("info", "Has subido correctamente '" + foto.getOriginalFilename() + "'");
				/*pasar el nombre de la foto al clinete para que quede almacenada en la base de datos y quede 
				 * a disposición para almacenar al cliente*/
				cliente.setFoto(foto.getOriginalFilename());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		String mensajeFlash = (cliente.getId() != null)? "Cliente editado con éxito": "Cliente creado con éxito";
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash ) {
		if(id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("warning", "Cliente eliminado con éxito");
		}
		return "redirect:/listar";
	}
}

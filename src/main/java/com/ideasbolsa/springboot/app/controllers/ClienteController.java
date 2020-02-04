package com.ideasbolsa.springboot.app.controllers;




import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.ideasbolsa.springboot.app.models.service.IUploadFileService;
import com.ideasbolsa.springboot.app.util.paginator.PageRender;



@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	/*Inyectando el servicio IUploadFileService para poder usar sus métodos */
	@Autowired
	private IUploadFileService uploadFileService;
	
	/*Método verFoto*/
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		
		Resource recurso = null;
		
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.getStackTrace();
		}
		
		return ResponseEntity.ok()
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +recurso.getFilename()+"\"")
				.body(recurso);
	}
	
	
	/*Método ver*/
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map< String, Object > model, RedirectAttributes flash) {
		Cliente cliente = clienteService.fetchByWithFacturas(id);//clienteService.findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe ne la base de datos");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		return "ver";
	}
	
	
	/*Método listar*/
	@RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}
	
	
	/*Método crear*/
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		return "form";
	}

	
	/*Método editar**/
	//RedirectAttributes: librería que permite mandar un mensaje cuando se realiza una acción de redireccionamiento  */
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
	
	
	/*Método guardar*/
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid  Cliente cliente, BindingResult result, Model model, 
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente");
			return "form";
		}
		/*Condición para saber si existe una foto cargada en el perfil del usuario(cliente)*/
		if(!foto.isEmpty()) {
			/*Condición que permite saber si la foto esta en el perfil, si esta en el perfil con Id existente,
			 * que existe foto cargada en el perfíl, si la foto es mayor a 0.....
			 * Todas estas condiciones son las que existen para saber si hay una foto cargada en el perfíl*/
			if (cliente.getId() !=null && cliente.getId() > 0 && cliente.getFoto()!=null && cliente.getFoto().length() > 0) {
				
				uploadFileService.delete(cliente.getFoto());
			}
			
			String uniqueFilename = null;
			
			try {
				
			uniqueFilename = uploadFileService.copy(foto);

			} catch (IOException e) {

				e.printStackTrace();
			}			
			/*Menasje al conbcluir la acción */
			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
			/*pasar el nombre de la foto al clinete para que quede almacenada en la base de datos y quede 
			 * a disposición para almacenar al cliente*/
			cliente.setFoto(uniqueFilename);
			}

		String mensajeFlash = (cliente.getId() != null)? "Cliente editado con éxito": "Cliente creado con éxito";
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	

	/*Método eliminar*/
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash ) {
		if(id > 0) {
			
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("warning", "Cliente eliminado con éxito");
			
				if (uploadFileService.delete(cliente.getFoto())) {
					flash.addFlashAttribute("info","Foto: " + cliente.getFoto() + " eliminada con éxito!");
				}
		}
		return "redirect:/listar";
	}
}

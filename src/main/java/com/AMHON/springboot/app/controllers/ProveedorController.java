package com.AMHON.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.Proveedor;
import com.AMHON.springboot.app.models.entity.Role;
import com.AMHON.springboot.app.models.entity.Usuario;
import com.AMHON.springboot.app.models.service.IClienteService;
import com.AMHON.springboot.app.util.paginator.PageRender;

@Controller // anotación para representar un controllador
@RequestMapping(value="/proveedores") // la ruta que se pedira
@SessionAttributes("proveedor")
public class ProveedorController {

	@Autowired
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/listaProveedores" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarProveedores(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
			List<Proveedor> proveedores = clienteService.encontrarProveedores();

		logger.info("lo que tiene proveedores: ".concat(proveedores.toString()));
		
		
		model.addAttribute("titulo", "Listado de Proveedores"); //agregamos al modelo un titulo
		model.addAttribute("proveedor", proveedores); // agregamos al modelo el atributo proveedores
		return "proveedores/listaProveedores"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formProveedor") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String crearProveedor(Map<String, Object> model) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		Proveedor proveedor = new Proveedor();
		model.put("proveedor", proveedor);
		model.put("titulo", "Formulario de Creación de Proveedor");
		model.put("textoBoton", "Crear Proveedor");
		return "proveedores/formProveedor";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = {"/formProveedor"}, method = RequestMethod.POST)
	public String guardarProveedor(@Valid Proveedor proveedor, BindingResult result, Model model,
	 RedirectAttributes flash, SessionStatus status) {

		
		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Creación de Proveedor");
			model.addAttribute("textoBoton", "Crear Proveedor");
			return "proveedores/formProveedor"; // si tiene error va redirigir al formulario

		}


		String mensajeflash = (proveedor.getId_prov() != null) ? "Proveedor editado con éxito!" : "Proveedor "+proveedor.getNombre_prov()+" creado con éxito!";
		
		try { //Probamos guardar la entrada mediante un try catch para manejar la exepcion.
			clienteService.saveProveedor(proveedor); //Mediante la interfaz clienteService y el metodo saveArticulo guardamos la instancia del objeto Articulo articulo.
						} catch (Exception  e) {// si falla el guardado			
					        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
					       if(proveedor.getId_prov()!= null) {
					        model.addAttribute("proveedor", proveedor);
					        model.addAttribute("titulo", "Editar Proveedor");
							model.addAttribute("textoBoton", "Editar Proveedor");
							flash.addFlashAttribute("error", "El Nombre del Proveedor: "+proveedor.getNombre_prov()+" ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
					        return "redirect:/proveedores/formProveedor/"+proveedor.getId_prov();//redireccionamos de nuevo hacia el formulario.
					       }
					       
					       model.addAttribute("titulo", "Formulario de Creación de Proveedor");
							model.addAttribute("textoBoton", "Crear Proveedor");
					       model.addAttribute("error", "El Nombre del Proveedor: "+proveedor.getNombre_prov()+" ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
					       return "proveedores/formProveedor";
			}

		    
			status.setComplete(); // elimina el objeto de la sesión.
			flash.addFlashAttribute("success", mensajeflash);
			return "redirect:/proveedores/listaProveedores";
		
		
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formProveedor/{id}")
	public String editarProveedor(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {

		Proveedor proveedor = null;
		

		if (id > 0) {
	proveedor = clienteService.encontrarProveedor(id);
			
			if (proveedor == null) {
				flash.addFlashAttribute("error", "El ID del proveedor no existe en la Base de Datos!");
				logger.info("no existe hay id:");
				
				return "redirect:/proveedores/listaProveedores";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del proveedor no puede ser cero!");
			return "redirect:/proveedores/listaProveedores";
		}
		
		
		model.put("proveedor", proveedor);
		model.put("titulo", "Editar Proveedor");
		model.put("textoBoton", "Editar Proveedor");
		return "proveedores/formProveedor";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/eliminarProveedor/{id}")
	public String eliminarProveedor(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Proveedor proveedor = clienteService.encontrarProveedor(id);

			
			try { //Probamos eliminar el proveedor mediante un try catch para manejar la exepcion.
				clienteService.EliminarProv(id); // Mediante la interfaz clienteService se procede a llamar el metodo EliminarProv y le pasamos el parametro id para borrar el registro especifico con esa id. 
				
				} catch (Exception  e) {// si falla el borrado			
						        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
						       
						        flash.addFlashAttribute("error", "No se pudo borrar el registro porque esta ligado a otros datos");// agregamos el mensaje de error que indica que el registro no puede ser borrado.
						        return "redirect:/proveedores/listaProveedores";//redireccionamos de nuevo hacia el formulario.
		        
				}
			flash.addFlashAttribute("success", "Proveedor: "+proveedor.getNombre_prov()+ " eliminado con éxito!");

		} 

		return "redirect:/proveedores/listaProveedores";
	}
	
}

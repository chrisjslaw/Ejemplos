package com.AMHON.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.UnidadMedida;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller // anotación para representar un controllador
@RequestMapping(value="/unidadesMedida") // la ruta que se pedira
@SessionAttributes("unidad")
public class UnidadMedController {
	
	@Autowired
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/listaUnidadesMedida" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarUnidadesMedida(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
			List<UnidadMedida> unidades = clienteService.encontrarUM();

		logger.info("lo que tiene unidad: ".concat(unidades.toString()));
		
		
		model.addAttribute("titulo", "Listado de Unidades"); //agregamos al modelo un titulo
		model.addAttribute("unidades", unidades); // agregamos al modelo el atributo UnidadM
		return "unidadesMedida/listaUnidadesMedida"; // retornamos el nombre de la vista
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	  @RequestMapping(value = "/formUnidadMedida") // esta es la ruta, no se mete el parametro Get ya que este se encarga de 
	  // mostrar el form 
	  public String crearUnidadMedida(Map<String, Object> model) {// recibe el objeto model para  poder pasar datos a la vista o bien 
		  // un map de java util
	  
	  UnidadMedida unidad = new UnidadMedida(); 
	  model.put("unidad", unidad);
	  model.put("titulo", "Formulario de Creación de Unidad de Medida");
	  model.put("textoBoton", "Crear Unidad"); 
	  return "unidadesMedida/formUnidadMedida"; 
	  }
	 

	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = {"/formUnidadMedida"}, method = RequestMethod.POST)
	public String guardarUnidadMedida(@Valid UnidadMedida unidad, BindingResult result, Model model,
	 RedirectAttributes flash, SessionStatus status) {

		
		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Creación de Unidad de Medida");
			model.addAttribute("textoBoton", "Crear Unidad"); 
			return "unidadesMedida/formUnidadMedida"; // si tiene error va redirigir al formulario

		}

		String mensajeflash = (unidad.getId_unidad() != null) ? "Unidad de medida editada con éxito!" : "Unidad de Medida " +unidad.getNombre_unid()+ " creada con éxito!";
		
		    
		    
		    try { //Probamos guardar la entrada mediante un try catch para manejar la exepcion.
		    	clienteService.saveUM(unidad); //Mediante la interfaz clienteService y el metodo saveArticulo guardamos la instancia del objeto Articulo articulo.
							} catch (Exception  e) {// si falla el guardado			
						        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
						      
						        if(unidad.getId_unidad() != null) {
							        model.addAttribute("unidad", unidad);
							        model.addAttribute("titulo", "Editar Unidad de Medida");
									model.addAttribute("textoBoton", "Editar Unidad");
									flash.addFlashAttribute("error", "El Nombre de la Unidad de medida: "+unidad.getNombre_unid()+" ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
							        return "redirect:/unidadesMedida/formUnidadMedida/"+unidad.getId_unidad();//redireccionamos de nuevo hacia el formulario.
							       }
							       
						           model.addAttribute("titulo", "Formulario de Creación de Unidad de Medida");
								   model.addAttribute("textoBoton", "Crear Unidad"); 
							       model.addAttribute("error", "El Nombre de la Unidad: "+unidad.getNombre_unid()+" ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
							       return "unidadesMedida/formUnidadMedida";
						        
				}
		    
		   
			status.setComplete(); // elimina el objeto de la sesión.
			flash.addFlashAttribute("success", mensajeflash);
			return "redirect:/unidadesMedida/listaUnidadesMedida";
		
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formUnidadMedida/{id}")
	public String editarUnidadMedida(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {

		UnidadMedida unidad = null;
		

		if (id > 0) {
	unidad = clienteService.findUMById(id);
			if (unidad == null) {
				flash.addFlashAttribute("error", "El ID de la unidad no existe en la Base de Datos!");
				logger.info("no existe hay id:");
				
				return "redirect:/unidadesMedida/listaUnidadesMedida";
			}	logger.info("El id en metodo editar despues de llamarlo de la bd es:"+unidad.getId_unidad());

		} else {
			flash.addFlashAttribute("error", "El ID del proveedor no puede ser cero!");
			return "redirect:/unidadesMedida/listaUnidadesMedida";
		}
		
		logger.info("El id en metodo editar es:"+unidad.getId_unidad());
		model.put("unidad", unidad);
		logger.info("El id en metodo editar es:"+unidad.getId_unidad());
		model.put("titulo", "Editar Unidad de Medida");
		model.put("textoBoton", "Editar Unidad");
		return "unidadesMedida/formUnidadMedida";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/eliminarUnidadMedida/{id}")
	public String eliminarUnidadDeMedida(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			UnidadMedida unidad = clienteService.findUMById(id);

			
			
			try { //Probamos eliminar el proveedor mediante un try catch para manejar la exepcion.
				clienteService.EliminarUM(id); // Mediante la interfaz clienteService se procede a llamar el metodo EliminarUM y le pasamos el parametro id para borrar el registro especifico con esa id. 
				
				} catch (Exception  e) {// si falla el borrado			
						        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
						       
						        flash.addFlashAttribute("error", "No se pudo borrar el registro porque esta ligado a otros datos");// agregamos el mensaje de error que indica que el registro no puede ser borrado.
						        return "redirect:/unidadesMedida/listaUnidadesMedida";//redireccionamos de nuevo hacia el formulario.
		        
				}
			
			flash.addFlashAttribute("success", "Unidad de medida: " +unidad.getNombre_unid()+ " eliminada con éxito!");

		} 

		return "redirect:/unidadesMedida/listaUnidadesMedida";
	}
	
}

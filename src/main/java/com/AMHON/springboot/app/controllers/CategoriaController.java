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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.Categoria;
import com.AMHON.springboot.app.models.entity.Proveedor;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller // anotación para representar un controllador
@RequestMapping(value="/categoria") // la ruta que se pedira
@SessionAttributes("categoria")
public class CategoriaController {

	@Autowired
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/listaCategorias" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarCategorias(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
			List<Categoria> categorias = clienteService.encontrarCategorias();

		logger.info("lo que tiene Categoria: ".concat(categorias.toString()));
		
		
		model.addAttribute("titulo", "Listado de Categorias de Articulos"); //agregamos al modelo un titulo
		model.addAttribute("categoria", categorias); // agregamos al modelo el atributo usuarios
		return "categoria/listaCategorias"; // retornamos el nombre de la vista
	}
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formCategoria") // esta es la ruta, no se mete el parametro Get ya que este se encarga de mostrar el form
	public String crearCategoria(Map<String, Object> model) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		Categoria categoria = new Categoria();
		model.put("categoria", categoria);
		model.put("titulo", "Formulario de Creación de Categoria");
		model.put("textoBoton", "Crear Categoria");
		return "categoria/formCategoria";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = {"/formCategoria"}, method = RequestMethod.POST)
	public String guardarCategoria(@Valid Categoria categoria, BindingResult result, Model model,
	 RedirectAttributes flash, SessionStatus status) {

		
		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Categoria");
			model.addAttribute("textoBoton", "Guardar");
			return "categoria/formCategoria"; // si tiene error va redirigir al formulario

		}


		String mensajeflash = (categoria.getId_categ() != null) ? "Categoria editada con éxito!" : "Categoria "+categoria.getNombre()+" creada con éxito!";
		
		
		try { //Probamos eliminar la categoria mediante un try catch para manejar la exepcion.
			clienteService.saveCategoria(categoria); // Mediante la interfaz clienteService se procede a llamar el metodo saveCategoria y le pasamos el parametro id para borrar el registro especifico con esa id. 
			
			} catch (Exception  e) {// si falla el borrado		
				 logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
			     if(categoria.getId_categ()!= null) {
				        model.addAttribute("categoria", categoria);
				        model.addAttribute("titulo", "Editar Categoria");
						model.addAttribute("textoBoton", "Editar Categoria");
						flash.addFlashAttribute("error", "El Nombre de la Categoria: "+categoria.getNombre()+" ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
				        return "redirect:/categoria/formCategoria/"+categoria.getId_categ();//redireccionamos de nuevo hacia el formulario.
				       }
				       
			           model.addAttribute("titulo", "Formulario de Creación de Categoria");
					   model.addAttribute("textoBoton", "Crear Categoria");
				       model.addAttribute("error", "El Nombre de la Categoria: "+categoria.getNombre()+" ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
				       return "categoria/formCategoria";
			}
		    
			status.setComplete(); // elimina el objeto de la sesión.
			flash.addFlashAttribute("success", mensajeflash);
			return "redirect:/categoria/listaCategorias";
		
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formCategoria/{id}")
	public String editarCategoria(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {

		Categoria categoria = null;
		

		if (id > 0) {
	categoria = clienteService.encontrarCategoria(id);
			
			if (categoria == null) {
				flash.addFlashAttribute("error", "El ID de la Categoria no existe en la Base de Datos!");
				logger.info("no existe hay id:");
				
				return "redirect:/categoria/listaCategorias";
			}
		} else {
			flash.addFlashAttribute("error", "El ID de la Categoria no puede ser cero!");
			return "redirect:/categoria/listaCategorias";
		}
		
		
		model.put("categoria", categoria);
		model.put("titulo", "Editar Categoria");
		model.put("textoBoton", "Editar Categoria");
		return "categoria/formCategoria";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/eliminarCategoria/{id}")
	public String eliminarCategoria(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Categoria categoria = clienteService.encontrarCategoria(id);

			
			try { //Probamos eliminar la categoria mediante un try catch para manejar la exepcion.
				clienteService.EliminarCategoria(id); // Mediante la interfaz clienteService se procede a llamar el metodo EliminarCategoriay le pasamos el parametro id para borrar el registro especifico con esa id. 
				
				} catch (Exception  e) {// si falla el borrado		
						        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
						       
						        flash.addFlashAttribute("error", "No se pudo borrar el registro porque esta ligado a otros datos");// agregamos el mensaje de error que indicaque el registro no puede ser borrado.
						        return "redirect:/categoria/listaCategorias";//redireccionamos de nuevo hacia la lista.
		        
				}
			flash.addFlashAttribute("success", "Categoria: " + categoria.getNombre() + " eliminada con éxito!");

		} 

		return "redirect:/categoria/listaCategorias";
	}
}

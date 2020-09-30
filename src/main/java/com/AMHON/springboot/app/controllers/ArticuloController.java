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

import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.Categoria;
import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.Proveedor;
import com.AMHON.springboot.app.models.entity.UnidadMedida;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller // anotación para representar un controllador
@RequestMapping(value="/articulos") // la ruta que se pedira
@SessionAttributes(value= {"unidades", "categorias","articulo"})
public class ArticuloController {

	
	@Autowired
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/listaArticulos" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarArticulos(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
			List<Articulo> articulos = clienteService.encontrarArticulos();

		logger.info("lo que tiene articulos: ".concat(articulos.toString()));
		
		
		model.addAttribute("titulo", "Listado de Articulos"); //agregamos al modelo un titulo
		model.addAttribute("articulo", articulos); // agregamos al modelo el atributo articulos
		return "articulos/listaArticulos"; // retornamos el nombre de la vista
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formArticulo") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String crearArticulo(Map<String, Object> model) {// utilizamos el model para poder pasar datos a la vista o bien
													// un map de java util
		
		List<UnidadMedida> unidades = clienteService.encontrarUM();
		List<Categoria> categorias = clienteService.encontrarCategorias();
		
		Articulo articulo = new Articulo();
		model.put("articulo", articulo);
		model.put("unidades", unidades);
		model.put("categorias", categorias);
		model.put("titulo", "Formulario de Articulo");
		model.put("textoBoton", "Crear Articulo");
		return "articulos/formArticulo";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formArticulo", method = RequestMethod.POST)
	public String guardarArticulo(@Valid Articulo articulo, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Articulo");
			model.addAttribute("textoBoton", "Guardar");
			return "articulos/formArticulo"; // si tiene error va redirigir al formulario

		}


		String mensajeflash = (articulo.getId_artic() != null) ? "Articulo editado con éxito!" : "Articulo creado con éxito!";

		
		
		try { //Probamos guardar la entrada mediante un try catch para manejar la exepcion.
			clienteService.saveArticulo(articulo); //Mediante la interfaz clienteService y el metodo saveArticulo guardamos la instancia del objeto Articulo articulo.
						} catch (Exception  e) {// si falla el guardado			
					        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
					       
					        if(articulo.getId_artic()!= null) {
						        model.addAttribute("articulo", articulo);
						        model.addAttribute("titulo", "Editar Articulo");
								model.addAttribute("textoBoton", "Editar Articulo");
								flash.addFlashAttribute("error", "El Nombre del Artículo: "+articulo.getNombre()+" ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
						        return "redirect:/articulos/formArticulo/"+articulo.getId_artic();//redireccionamos de nuevo hacia el formulario.
						       }
						       
					        List<UnidadMedida> unidades = clienteService.encontrarUM();
							List<Categoria> categorias = clienteService.encontrarCategorias();
							
							
							model.addAttribute("articulo", articulo);
							model.addAttribute("unidades", unidades);
							model.addAttribute("categorias", categorias);
							model.addAttribute("titulo", "Formulario de Articulo");
							model.addAttribute("textoBoton", "Crear Articulo");
						       model.addAttribute("error", "El Nombre del Articulo: "+articulo.getNombre()+" ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
						       return "articulos/formArticulo";
					        
			}
		
		status.setComplete(); // elimina el objeto cliente de la sesión.
		flash.addFlashAttribute("success", mensajeflash);
		return "redirect:/articulos/listaArticulos";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formArticulo/{id}")
	public String editarArticulo(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {

		Articulo articulo = null;
		

		if (id > 0) {
	articulo = clienteService.findArticuloById(id);
			
			if (articulo == null) {
				flash.addFlashAttribute("error", "El ID del articulo no existe en la Base de Datos!");
				logger.info("no existe hay id:");
				
				return "redirect:/articulos/listaArticulos";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del articulo no puede ser cero!");
			return "redirect:/articulos/listaArticulos";
		}
		
		List<UnidadMedida> unidades = clienteService.encontrarUM();
		List<Categoria> categorias = clienteService.encontrarCategorias();
		
		model.put("articulo", articulo);
		model.put("unidades", unidades);
		model.put("categorias", categorias);
		model.put("titulo", "Editar Articulo");
		model.put("textoBoton", "Editar Articulo");
		return "articulos/formArticulo";
	}

	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/eliminarArticulo/{id}")
	public String eliminarArticulo(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Articulo articulo = clienteService.findArticuloById(id);

			if(articulo ==null) {
				flash.addFlashAttribute("success", "El articulo no existe!");
				return "redirect:/articulos/listaArticulos";
			}
			try { //Probamos eliminar la el articulo mediante un try catch para manejar la exepcion.
				clienteService.EliminarArtic(id); // Mediante la interfaz clienteService se procede a llamar el metodo EliminarArtic y le pasamos el parametro id para borrar el registro especifico con esa id. 
				
				} catch (Exception  e) {// si falla el borrado			
						        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
						       
						        flash.addFlashAttribute("error", "No se pudo borrar el registro porque esta ligado a otros datos");// agregamos el mensaje de error que indica que el registro no puede ser borrado.
						        return "redirect:/articulos/listaArticulos";//redireccionamos de nuevo hacia el formulario.
		        
				}
			flash.addFlashAttribute("success", "Articulo: "+articulo.getNombre()+ " eliminado con éxito!");

		} 

		return "redirect:/articulos/listaArticulos";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/InventarioKardex/{id}" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarInv(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
			List<DetalleEntrada> detmov = clienteService.findById_Artic(id);
			Articulo articulo = clienteService.findArticuloById(id);
			
			if (detmov == null) {
				flash.addFlashAttribute("error", "El Articulo no tiene movimientos en inventario");
				return "redirect:/articulos/listaArticulos";
			}

		logger.info("lo que tiene articulos: ".concat(detmov.toString()));
		
		model.put("articulo", articulo);
		model.put("titulo", "Kardex del Articulo: " + articulo.getNombre()); //agregamos al modelo un titulo
		model.put("detmov", detmov); // agregamos al modelo el atributo detalleMovimiento
		return "articulos/InventarioKardex"; // retornamos el nombre de la vista
	}
	
}

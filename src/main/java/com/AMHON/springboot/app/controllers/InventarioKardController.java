package com.AMHON.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.Kardex;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller // anotación para representar un controllador
@RequestMapping(value="/invKardex") // la ruta que se pedira
@SessionAttributes("kardex")
public class InventarioKardController {

	@Autowired
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/InventarioKardex/{id}" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarInv(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
	
		
			List<Kardex> kardex = clienteService.findByArticulo(id);
			
			Articulo articulo = clienteService.findArticuloById(id);
			
			if (kardex == null || articulo ==null) {
				flash.addFlashAttribute("error", "El Articulo no tiene movimientos en inventario o no existe");
				return "redirect:/invKardex/KardexGeneral";
			}

		
		model.put("articulo", articulo);
		model.put("titulo", "Kardex del Articulo: " + articulo.getNombre()); //agregamos al modelo un titulo
		model.put("kardex", kardex); // agregamos al modelo el atributo detalleMovimiento
		return "invKardex/InventarioKardex"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/KardexGeneral" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarKardexGeneral(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
			List<Kardex> kardex = clienteService.encontrarKardexList();
			List<Articulo> articulos = clienteService.encontrarArticulos();
		
		model.addAttribute("titulo", "Listado de Kardex General"); //agregamos al modelo un titulo
		model.addAttribute("articulos", articulos); // agregamos al modelo el atributo articulos
		model.addAttribute("kardex", kardex); // agregamos al modelo el atributo articulos
		return "invKardex/KardexGeneral"; // retornamos el nombre de la vista
	}
	
	
	
	
}

package com.AMHON.springboot.app.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.dao.IUsuarioDao;
import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.Isv;
import com.AMHON.springboot.app.models.entity.Kardex;
import com.AMHON.springboot.app.models.entity.Entrada;
import com.AMHON.springboot.app.models.entity.Proveedor;
import com.AMHON.springboot.app.models.entity.Usuario;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller
@RequestMapping(value="/entrada")
@SessionAttributes(value= {"entrada", "proveedores","isv","detmov"})
public class EntradaController {

	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IClienteService clienteService;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/editarEntrada/{id}")
	public String editarEntrada(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {

		Entrada entrada = null;
		

		if (id > 0) {
	entrada = clienteService.encontrarEntrada(id);
			
			if (entrada == null) {
				flash.addFlashAttribute("error", "La entrada no existe en la Base de Datos");
				return "redirect:/entrada/listaEntradas";
			}
		} else {
			flash.addFlashAttribute("error", "El ID de la entrada no puede ser cero!");
			return "redirect:/entrada/listaEntradas";
		}
		
		List<Proveedor> proveedores = clienteService.encontrarProveedores();
		model.put("entrada", entrada);
		model.put("proveedores", proveedores);
		model.put("textoBoton", "Editar");
		model.put("titulo", "Editar #Factura: ".concat(entrada.getNum_factura()));
		return "entrada/editarEntrada";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/editarEntrada", method = RequestMethod.POST)
	public String guardarEntradaEditada(@Valid Entrada entrada, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Editar de Entrada");
			model.addAttribute("textoBoton", "Editar");
			return "entrada/editarEntrada"; // si tiene error va redirigir al formulario

		}


		String mensajeflash = (entrada.getId_entrada() != null) ? "Entrada editada con éxito!" : "La entrada no se pudo encontrar!";

		
		try { //Probamos guardar la entrada mediante un try catch para manejar la exepcion.
			clienteService.saveMovimiento(entrada); //Mediante la interfaz clienteService y el metodo saveMovimiento guardamos la instancia Entrada entrada.
			} catch (Exception  e) {// si falla el guardado			
					        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
					        status.setComplete();//cerramos la session para evitar tener transacciones pendientes.
					        flash.addFlashAttribute("error", "El Número del la factura ya existe en la Base de Datos, por favor verifique que el número de factura sea correcto.");// agregamos el mensaje de error que indica que el nombre de la factura ya esxiste.
					        return "redirect:/entrada/editarEntrada/"+entrada.getId_entrada();//redireccionamos de nuevo hacia el formulario.
	        
			}
		
		
		status.setComplete(); // elimina el objeto cliente de la sesión.
		flash.addFlashAttribute("success", mensajeflash);
		return "redirect:/entrada/verDetalleEntrada/"+entrada.getId_entrada();
	}
	
	
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/listaEntradas" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarEntradas(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
			List<Entrada> entradas = clienteService.encontrarEntradas();

		logger.info("lo que tiene la entrada: ".concat(entradas.toString()));
		
		model.addAttribute("titulo", "Listado de Entradas"); //agregamos al modelo un titulo
		model.addAttribute("entrada", entradas); // agregamos al modelo el atributo movimientos
		return "entrada/listaEntradas"; // retornamos el nombre de la vista
	}
		
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@GetMapping(value = "/formEntrada") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String crearEntrada(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util
		String respon = authentication.getName();
		 
		Usuario usuario = usuarioDao.findByUsername(respon);
		List<Proveedor> proveedores = clienteService.encontrarProveedores();
		Long id = 1L;
		Isv isv = null;
		isv = clienteService.encontrarIsv(id);
		
		Entrada entrada = new Entrada();
		entrada.setResponsbl(usuario.getNombreCompleto());
		model.put("entrada", entrada);
		model.put("isv", isv);
		model.put("proveedores", proveedores);
		model.put("titulo", "Formulario de Entrada");
		model.put("textoBoton", "Crear Entrada");
		return "entrada/formEntrada";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@GetMapping(value="/cargar-articulos/{term}", produces= {"application/json"})
	public @ResponseBody List<Articulo> cargarArticulos(@PathVariable String term){
		return clienteService.findByNombreArt(term);
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@PostMapping(value = "/formEntrada")
	public String guardarEntrada(@Valid Entrada entrada, BindingResult result, Model model,
			@RequestParam(name="item_id[]", required=false) Long[] itemId,//para obtener las lineas de entrada
			@RequestParam(name="cantidad[]", required= false) Integer[] cantidad,//para obtener las lineas de entrada
			@RequestParam(name="precioIsv[]", required= false) Double[] precio,//para obtener las lineas de entrada
			@RequestParam(name="tipoMov[]", required= false) String tipoMov,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Entrada");
			model.addAttribute("textoBoton", "Guardar");
			return "entrada/formEntrada"; // si tiene error va redirigir al formulario

		}
		
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Formulario de Entrada");
			model.addAttribute("error", "Error: La Entrada NO puede no tener lÃ­neas!");
			return "entrada/formEntrada";
		}

		for(int i=0; i<itemId.length; i++) {
			Articulo articulo = clienteService.findArticuloById(itemId[i]);//obtener el producto por cada linea
			DetalleEntrada detmov = new DetalleEntrada();
			Kardex kardex = new Kardex();
			detmov.setTipo_mov(tipoMov);//se  pasa el tipo mov
			detmov.setCantidad(cantidad[i]);//se le pasa la cantidad
			detmov.setArticulo(articulo);//se  pasa el Articulo
			detmov.setPrecio(precio[i]);//se  pasa el Articulo 
			// guardado de kardex
			kardex.setArticulo(articulo);
			kardex.setCantidad(cantidad[i]);
			kardex.setPrecio(precio[i]);
			kardex.setTipo_mov(tipoMov);
			entrada.addItemMovimiento(detmov);//agregar la linea a Entrada
			kardex.setDet_mov(detmov);
			//Calculos antes de guardar los respectivos valores
			
			//cálculo y guardado de existencia 
			Integer existencia_actual = articulo.getExistencia();
			Integer act_exist = existencia_actual+cantidad[i];
			articulo.setExistencia(act_exist);
			
			//cálculo y guardado total costo en inventario
			Double total_movimi = precio[i]*cantidad[i];
			Double costo_tot = total_movimi+articulo.getCosto_total();
			articulo.setCosto_total(costo_tot);
			
			//cálculo y guardado de precio promedio
			Double precio_promedio = costo_tot/act_exist;
		    articulo.setPrecio_prom(precio_promedio);
		    articulo.setUltimo_precio(precio[i]);
			
			clienteService.saveArticulo(articulo);
			clienteService.saveKardex(kardex);
			logger.info("ID: "+itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
		}
		try { //Probamos guardar la entrada mediante un try catch para manejar la exepcion.
			clienteService.saveMovimiento(entrada); //Mediante la interfaz clienteService y el metodo saveMovimiento guardamos la instancia Entrada entrada.
			} catch (Exception  e) {// si falla el guardado			
					        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
					        status.setComplete();//cerramos la session para evitar tener transacciones pendientes.
					        flash.addFlashAttribute("error", "El Número del la factura ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre de la factura ya esxiste.
					        return "redirect:/entrada/formEntrada";//redireccionamos de nuevo hacia el formulario.
	        
			}
		
		status.setComplete();
		
		flash.addFlashAttribute("success", "Entrada creada con éxito!");
		return "redirect:/entrada/listaEntradas";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/eliminarEntrada/{id}")
	public String eliminarEntrada(@PathVariable(value = "id") Long id, RedirectAttributes flash, SessionStatus status) {

		if (id > 0) {
			Entrada entrada = clienteService.encontrarEntrada(id);
            List<DetalleEntrada> detentrad = clienteService.findByIdEntrada(entrada.getId_entrada());
  		  
  		  
  			for(DetalleEntrada detmov : detentrad) {

  				Articulo articulo = clienteService.findArticuloById(detmov.getArticulo().getId_artic());
  				//costo total
  				Double totmov_actual = detmov.getPrecio()*detmov.getCantidad(); //se calcula el total actual del movimiento
  				Double costo_tot = articulo.getCosto_total()-totmov_actual; // se resta al costo total actualizado el valor total actual del movimiento
  				articulo.setCosto_total(costo_tot);
  				
  				//existencia articulo
  				Integer cant_exist = articulo.getExistencia()-detmov.getCantidad();
  				articulo.setExistencia(cant_exist);
  				
  				if(costo_tot == 0 || cant_exist== 0) {
  					//cálculo y guardado de precio promedio cuando queda en 0
  					Double precio_promedio = 0.0;
  				    articulo.setPrecio_prom(precio_promedio);
  					logger.info("entro a costo total 0: "+precio_promedio);

  					
  				}else {
  					//cálculo y guardado de precio promedio
  					Double precio_promedio = costo_tot/cant_exist;
  				    articulo.setPrecio_prom(precio_promedio);
  				}
  				
  				clienteService.saveArticulo(articulo);
  			}
			
            clienteService.EliminarMovimiento(id); //eliminar entrada
           
			status.setComplete();
			flash.addFlashAttribute("success", "Entrada: eliminado con éxito!");

		} 

		return "redirect:/entrada/listaEntradas";
	}
	
	
	/* ----------------------------------------- Todos los métodos de detalle de entrada --------------------------------------------------------------- */
	 
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping("/verDetalleEntrada/{id}") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
	// entrar en accion cuando se utiliza dicha ruta dentro de la aplicación
	public String verDetalleEntrada(@PathVariable(value="id") Long id, 
			Model model, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
		// un map de java util

		Entrada entrada = clienteService.encontrarEntrada(id); //Creamos un objeto de tipo Entrada para obtener el registro de Entrada mediante con el id correspondiente mediante la interfaz clienteService que llama el método encontrarEntrada
		if(entrada == null) { // verificamos si el objeto entrada viene con parametro null
			flash.addFlashAttribute("error", "La entrada no existe en la Base de Datos"); // en dado caso que venga null agregamos un mensaje con dicho error
			return "redirect:/entrada/listaEntradas"; // retornamos a la lista de entradas ya que no se posee dicho registro en la base de datos
		}
		
		Date hoy = new Date();
		Date fecha = entrada.getFecha();
		 long diff = hoy.getTime() - fecha.getTime();
	    
		 int dias = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		 logger.info("articulo a dias"+dias);
		
		model.addAttribute("dias", dias); // en el caso que el objeto dias contenga información lo pasamos mediante model a la vista detalle entrada
		model.addAttribute("entrada", entrada); // en el caso que el objeto entrada contenga información lo pasamos mediante model a la vista detalle entrada
		model.addAttribute("titulo", "#Factura: ".concat(entrada.getNum_factura())); // agregamos el titulo de la vista al model 
		
		return "entrada/verDetalleEntrada";//retornamos  la vista VerDetalleEntrada para que pueda ser visualizada
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping("/verDetalleEntradaV/{id}") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
	// entrar en accion cuando se utiliza dicha ruta dentro de la aplicación
	public String verDetalleEntradaV(@PathVariable(value="id") Long id, 
			Model model, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
		// un map de java util

		Entrada entrada = clienteService.encontrarEntrada(id); //Creamos un objeto de tipo Entrada para obtener el registro de Entrada mediante con el id correspondiente mediante la interfaz clienteService que llama el método encontrarEntrada
		if(entrada == null) { // verificamos si el objeto entrada viene con parametro null
			flash.addFlashAttribute("error", "La entrada no existe en la Base de Datos"); // en dado caso que venga null agregamos un mensaje con dicho error
			return "redirect:/reportes/formReporteEntradaXfechas"; // retornamos a la lista de entradas ya que no se posee dicho registro en la base de datos
		}
		
		model.addAttribute("entrada", entrada); // en el caso que el objeto entrada contenga información lo pasamos mediante model a la vista detalle entrada
		model.addAttribute("titulo", "#Factura: ".concat(entrada.getNum_factura())); // agregamos el titulo de la vista al model 
		
		return "entrada/verDetalleEntradaV";//retornamos  la vista VerDetalleEntrada para que pueda ser visualizada
	}
	
		
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = {"/editarDetalleEntrada"}, method = RequestMethod.POST)
	public String actualizarDetalleEntrada(@Valid DetalleEntrada detmov, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

		logger.info("ID a editar"+detmov.getId_det_entr());
		logger.info("articulo a editar"+detmov.getArticulo());
		logger.info("Cantidad a editar"+detmov.getCantidad());
		logger.info("Precio a editar"+detmov.getPrecio());
		
		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Entrada");
			model.addAttribute("textoBoton", "Guardar");
			return "entrada/editarDetalleEntrada"; // si tiene error va redirigir al formulario

		}
		
		DetalleEntrada detmovant = clienteService.encontrarDetalleEntrada(detmov.getId_det_entr());
		Entrada entrada = clienteService.encontrarEntrada(detmovant.getEntrada().getId_entrada());
		logger.info("ID de entrada"+entrada.getId_entrada());
		Kardex kardex = clienteService.findByDetalleMov(detmovant.getId_det_entr());
		Articulo articulo = clienteService.findArticuloById(detmov.getArticulo().getId_artic());
		
		logger.info("detalle de movimiento id del articulo: "+detmov.getArticulo().getId_artic());
		logger.info("id de detalle: "+detmov.getId_det_entr());
		Integer cantidadant= detmovant.getCantidad();
		Double precio_ant = detmovant.getPrecio();
		
		logger.info("articulo precio ant"+precio_ant);
		logger.info("articulo a cantidad"+cantidadant);
		
		//actualizando kardex
		kardex.setCantidad(detmov.getCantidad());
		kardex.setPrecio(detmov.getPrecio());
		
		
		//actualizando articulo
			logger.info("entro a cantidadant mayor a detmov");
			Integer totExist_ant = articulo.getExistencia() - cantidadant;
			logger.info("totExist "+totExist_ant);
			Integer cantid= detmov.getCantidad();
			logger.info("cantid "+cantid);
			Integer exist = totExist_ant+cantid; 
			logger.info("exist "+exist);
			articulo.setExistencia(exist);
			
			//cálculo y guardado total costo en inventario
			Double totalmov_anterior = precio_ant*cantidadant; // se calcula el total del movimiento anterior
			Double total_actualizado = articulo.getCosto_total()-totalmov_anterior; //esta variable actualiza el costo total en inventario rebajando el valor total anterior al costo tota.
			Double totmov_actual = detmov.getPrecio()*detmov.getCantidad(); //se calcula el total actual del movimiento
			Double costo_tot = total_actualizado+totmov_actual; // se suma al costo total actualizado el valor total actual del movimiento
			articulo.setCosto_total(costo_tot);
			
			//cálculo y guardado de precio promedio
			Double precio_promedio = costo_tot/exist;
		    articulo.setPrecio_prom(precio_promedio);
		    detmovant.setCantidad(detmov.getCantidad());
		    detmovant.setPrecio(detmov.getPrecio());
		
			clienteService.saveArticulo(articulo);
			clienteService.saveDetalleEntrada(detmovant);
			clienteService.saveKardex(kardex);
			
		status.setComplete();
		
		flash.addFlashAttribute("success", "Detalle de entrada editada con éxito!");
		return "redirect:/entrada/verDetalleEntrada/"+detmovant.getEntrada().getId_entrada();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@GetMapping("/editarDetalleEntrada/{id}")
	public String editarDetalleEntrada(@PathVariable(value="id") Long id, 
			Map<String, Object> model, RedirectAttributes flash) {
		
		DetalleEntrada detmov = null;
		if (id > 0) {
			 detmov = clienteService.encontrarDetalleEntrada(id);
			if(detmov == null) {
				flash.addFlashAttribute("error", "El detalle de entrada no existe en la Base de Datos");
				return "redirect:/entrada/listaEntradas";
				} }else {
					flash.addFlashAttribute("error", "El ID del detalle de entrada no puede ser cero!");
					return "redirect:/entrada/listaEntradas";
				}
		
		
		Long idis = 1L;
		Isv isv = null;
		isv = clienteService.encontrarIsv(idis);
		logger.info("ID a editar"+detmov.getId_det_entr());
		model.put("isv", isv);
		model.put("textoBoton", "Editar");
		model.put("detmov", detmov);
		model.put("titulo", "Editar Detalle de Entrada");
		
		return "entrada/editarDetalleEntrada";
	}
	
	
	//probar
	@PreAuthorize("isFullyAuthenticated()")
	@GetMapping("/eliminarDetalleEntrada/{id}")
	public String eliminarDetalleEntrada(@PathVariable(value="id")Long id,
			RedirectAttributes flash, SessionStatus status) {
		DetalleEntrada  detmov = clienteService.encontrarDetalleEntrada(id);
		
		
		if(detmov != null) {
			
			Articulo articulo = clienteService.findArticuloById(detmov.getArticulo().getId_artic());
			//costo total
			Double totmov_actual = detmov.getPrecio()*detmov.getCantidad(); //se calcula el total actual del movimiento
			Double costo_tot = articulo.getCosto_total()-totmov_actual; // se resta al costo total actualizado el valor total actual del movimiento
			articulo.setCosto_total(costo_tot);
			
			//existencia articulo
			Integer cant_exist = articulo.getExistencia()-detmov.getCantidad();
			articulo.setExistencia(cant_exist);
			
			if(costo_tot == 0 || cant_exist== 0) {
				//cálculo y guardado de precio promedio cuando queda en 0


				
			}else {
				//cálculo y guardado de precio promedio
				Double precio_promedio = costo_tot/cant_exist;
			    articulo.setPrecio_prom(precio_promedio);
			}
			
			
			clienteService.saveArticulo(articulo);
			clienteService.deleteDetalleEntrada(id);
			status.setComplete();
			flash.addFlashAttribute("success", "Detalle de Entrada eliminado con éxito!");
			return "redirect:/entrada/verDetalleEntrada/"+detmov.getEntrada().getId_entrada();
		}
		flash.addFlashAttribute("error", "El detalle de Entrada no existe en la base de datos, no se pudo eliminar");
		return "redirect:/listarEntradas";
	}
	
	
	
}

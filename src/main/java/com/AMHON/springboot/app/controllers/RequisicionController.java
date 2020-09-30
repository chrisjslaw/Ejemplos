package com.AMHON.springboot.app.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.dao.IUsuarioDao;
import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.Categoria;
import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.DetalleRequisicion;
import com.AMHON.springboot.app.models.entity.Entrada;
import com.AMHON.springboot.app.models.entity.Kardex;
import com.AMHON.springboot.app.models.entity.Proveedor;
import com.AMHON.springboot.app.models.entity.Requisicion;
import com.AMHON.springboot.app.models.entity.UnidadMedida;
import com.AMHON.springboot.app.models.entity.Usuario;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller
@RequestMapping(value="/requisicion")
@SessionAttributes(value= {"requisicion","detreq"})
public class RequisicionController {

	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IClienteService clienteService;
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	
	@PreAuthorize("isFullyAuthenticated()")
	@RequestMapping(value = { "/misRequisiciones" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarMisRequisiciones(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		String respon = authentication.getName();
		Usuario usuario = usuarioDao.findByUsername(respon);
		Long id_us = usuario.getId();
			List<Requisicion> requisicion = clienteService.findByFechaTresMeses(id_us);

		
		model.addAttribute("titulo", "Mis Requisiciones"); //agregamos al modelo un titulo
		model.addAttribute("requisicion", requisicion); // agregamos al modelo el atributo movimientos
		return "requisicion/misRequisiciones"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/listaRequisiciones" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarRequisiciones(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
			List<Requisicion> requisicion = clienteService.encontrarRequisiciones();

		
		model.addAttribute("titulo", "Listado de Requisiciones"); //agregamos al modelo un titulo
		model.addAttribute("requisicion", requisicion); // agregamos al modelo el atributo movimientos
		return "requisicion/listaRequisiciones"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_Gerente_Financiero')")
	@RequestMapping(value = { "/listaRequisicionesNoAprob" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarRequisicionesNoAprob(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		String term = "Pendiente";
			List<Requisicion> requisicion = clienteService.findByEstado(term);

		
		model.addAttribute("titulo", "Listado de Requisiciones No aprobadas"); //agregamos al modelo un titulo
		model.addAttribute("requisicion", requisicion); // agregamos al modelo el atributo movimientos
		return "requisicion/listaRequisicionesNoAprob"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/listaRequisicionesAEntregar" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarRequisicionesAEntregar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		String term = "Aprobado";
			List<Requisicion> requisicion = clienteService.findByEstado(term);

		
		model.addAttribute("titulo", "Listado de Requisiciones por entregar"); //agregamos al modelo un titulo
		model.addAttribute("requisicion", requisicion); // agregamos al modelo el atributo movimientos
		return "requisicion/listaRequisicionesAEntregar"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("isFullyAuthenticated()")
	@RequestMapping(value = "/formRequisicion") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String crearRequisicion(Map<String, Object> model, Authentication authentication) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util
		String respon = authentication.getName();
		Usuario usuario = usuarioDao.findByUsername(respon);
		
		Requisicion requisicion = new Requisicion();
		requisicion.setSolicitante(usuario.getNombreCompleto());
		requisicion.setCargo(usuario.getCargo());
		requisicion.setDepartamento(usuario.getDepto().getNombre_depto());
		requisicion.setUsuario(usuario);
		model.put("requisicion", requisicion);
		model.put("titulo", "Formulario de Requisicion");
		model.put("textoBoton", "Crear Requisicion");
		return "requisicion/formRequisicion";
	}
	
	@GetMapping(value="/cargar-articulos/{term}", produces= {"application/json"})
	public @ResponseBody List<Articulo> cargarArticulos(@PathVariable String term){
		return clienteService.findByNombreArtExist(term);
	}
	
	
	@PreAuthorize("isFullyAuthenticated()")
	@RequestMapping(value = "/formRequisicion", method = RequestMethod.POST)
	public String guardarRequisicion(@Valid Requisicion requisicion, BindingResult result, Model model,
			@RequestParam(name="item_id[]", required=false) Long[] itemId,//para obtener las lineas de requisicion
			@RequestParam(name="cantidad[]", required= false) Integer[] cantidad,//para obtener las lineas de requisicion
			@RequestParam(name="tipoMov[]", required= false) String tipoMov,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Requisicion");
			model.addAttribute("textoBoton", "Crear Requisicion Especial");
			return "requisicion/formRequisicion"; // si tiene error va redirigir al formulario

		}
		
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Formulario de Requisicion");
			model.addAttribute("error", "Error: La Requisición NO puede no tener artículos!");
			model.addAttribute("textoBoton", "Crear Requisicion Especial");
			return "requisicion/formRequisicion";
		}

		for(int i=0; i<itemId.length; i++) {
			Articulo articulo = clienteService.findArticuloById(itemId[i]);//obtener el producto por cada linea
			DetalleRequisicion detreq = new DetalleRequisicion();
			Kardex kardex = new Kardex();
			Integer existencia_actual = articulo.getExistencia();
			if(articulo.getReq_max() < cantidad[i]) {
				model.addAttribute("titulo", "Formulario de Requisicion");
				model.addAttribute("error", "Error: La cantidad de: "+articulo.getNombre()+" maxima para pedir es de: "+articulo.getReq_max()+ " , por favor pida una cantidad menor.");
				model.addAttribute("textoBoton", "Crear Requisicion Especial");
				return "requisicion/formRequisicion";
			}else if(existencia_actual-cantidad[i]<0) {
				model.addAttribute("titulo", "Formulario de Requisicion");
				model.addAttribute("error", "Aviso: Unicamente hay "+existencia_actual+" en existencia del articulo "+articulo.getNombre()+" , por favor pida una cantidad menor.");
				model.addAttribute("textoBoton", "Crear Requisicion Especial");
				return "requisicion/formRequisicion"; 
			}
			detreq.setTipo_mov(tipoMov);//se  pasa el tipo mov
			detreq.setCantidad(cantidad[i]);//se le pasa la cantidad
			detreq.setArticulo(articulo);//se  pasa el Articulo
			detreq.setPrecio(articulo.getPrecio_prom());
			// guardado de kardex
//			kardex.setArticulo(articulo);
//			kardex.setCantidad(cantidad[i]);
//			kardex.setPrecio(articulo.getPrecio_prom());
//			kardex.setTipo_mov(tipoMov); 
		//	requisicion.setResponsbl("Andrés Bejarano");
			String estado="Pendiente";
			String motivo="Ninguno.";
			requisicion.setMotivo(motivo);
			requisicion.setEstado(estado);
			requisicion.addItemRequisicion(detreq);//agregar la linea a Entrada
//			kardex.setDet_req(detreq);
			//Calculos antes de guardar los respectivos valores
			
			
			
//			clienteService.saveKardex(kardex);
			logger.info("ID: "+itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
		}

		clienteService.saveRequisicion(requisicion);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Requisición creada con éxito!");
		return "redirect:/requisicion/misRequisiciones";
	}
	
	
	//---------------------- Requisicion especial------------------------
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formRequisicionEspecial") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String crearRequisicionEspecial(Map<String, Object> model, Authentication authentication) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util
		String respon = authentication.getName();
		Usuario usuario = usuarioDao.findByUsername(respon);
		
		Requisicion requisicion = new Requisicion();
		requisicion.setSolicitante(usuario.getNombreCompleto());
		requisicion.setCargo(usuario.getCargo());
		requisicion.setDepartamento(usuario.getDepto().getNombre_depto());
		requisicion.setUsuario(usuario);
		model.put("requisicion", requisicion);
		model.put("titulo", "Formulario de Requisicion Especial");
		model.put("textoBoton", "Crear Requisicion Especial");
		return "requisicion/formRequisicionEspecial";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = "/formRequisicionEspecial", method = RequestMethod.POST)
	public String guardarRequisicionEspecial(@Valid Requisicion requisicion, BindingResult result, Model model,
			@RequestParam(name="item_id[]", required=false) Long[] itemId,//para obtener las lineas de requisicion
			@RequestParam(name="cantidad[]", required= false) Integer[] cantidad,//para obtener las lineas de requisicion
			@RequestParam(name="tipoMov[]", required= false) String tipoMov,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Requisicion Especial");
			model.addAttribute("textoBoton", "Guardar");
			return "requisicion/formRequisicionEspecial"; // si tiene error va redirigir al formulario

		}
		
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Formulario de Requisicion");
			model.addAttribute("error", "Error: La Requisición NO puede no tener artículos!");
			return "requisicion/formRequisicionEspecial";
		}

		for(int i=0; i<itemId.length; i++) {
			Articulo articulo = clienteService.findArticuloById(itemId[i]);//obtener el producto por cada linea
			DetalleRequisicion detreq = new DetalleRequisicion();
			Kardex kardex = new Kardex();
			Integer existencia_actual = articulo.getExistencia();
			if(existencia_actual-cantidad[i]<0) {
				model.addAttribute("titulo", "Formulario de Requisicion Especial");
				model.addAttribute("error", "Aviso: Unicamente hay "+existencia_actual+" en existencia del articulo "+articulo.getNombre()+" , por favor pida una cantidad menor.");
				model.addAttribute("textoBoton", "Guardar");
				return "requisicion/formRequisicionEspecial"; 
			}
			//creando detalle requisicion
			detreq.setTipo_mov(tipoMov);//se  pasa el tipo mov
			detreq.setCantidad(cantidad[i]);//se le pasa la cantidad
			detreq.setArticulo(articulo);//se  pasa el Articulo
			detreq.setPrecio(articulo.getPrecio_prom());
			
		/*	//cálculo y guardado de existencia 
			Integer act_exist = existencia_actual-cantidad[i];
			Integer exist_act_salida= articulo.getTot_salidas()+cantidad[i];//sumamos el total de salidas anterior a la cantidad que va de salida
			articulo.setExistencia(act_exist);//guardamos 
			articulo.setTot_salidas(exist_act_salida);//guardamos la existencia actual de salidas
			
			
			//cálculo y guardado total costo en inventario
			Double total_movimi = articulo.getPrecio_prom()*cantidad[i];
			Double costo_tot = articulo.getCosto_total()-total_movimi;
			articulo.setCosto_total(costo_tot);
			
			//Cálculo y guardado del costo total de salidas
			Double total_salid = total_movimi+articulo.getCosto_totsalida();//calculamos el costo total de las salidas la cual es: total_movi de la requisición actual más el costo_totsalida anterior
			articulo.setCosto_totsalida(total_salid);  //guardamos el costo total de la salida
			*/
			// guardado de kardex
			/*
			kardex.setArticulo(articulo);
			kardex.setCantidad(cantidad[i]);
			kardex.setPrecio(articulo.getPrecio_prom());
			kardex.setTipo_mov(tipoMov); */
		//	requisicion.setResponsbl("Andrés Bejarano");
			String estado="Pendiente";
			requisicion.setEstado(estado);
			requisicion.addItemRequisicion(detreq);//agregar la linea a Entrada
			//kardex.setDet_req(detreq);
			//Calculos antes de guardar los respectivos valores
			
			
			
			//clienteService.saveKardex(kardex);
			
		}

		clienteService.saveRequisicion(requisicion);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Requisición creada con éxito!");
		return "redirect:/requisicion/misRequisiciones";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_Gerente_Financiero')")
	@RequestMapping(value = "/aprobarRequisicion/{id}")
	public String aprobarRequisicion(@PathVariable(value = "id") Long id, RedirectAttributes flash,
			 Model model, Authentication authentication ) {

		String respon = authentication.getName();
		Usuario usuario = usuarioDao.findByUsername(respon);
		if (id > 0) {
			Requisicion requisicion = clienteService.encontrarRequisicion(id);
			
			String estado ="Aprobado";
			
			requisicion.setFecha_aprob(new Date());
			requisicion.setEntregado_por(usuario.getNombreCompleto());
			requisicion.setEstado(estado);
			clienteService.saveRequisicion(requisicion);
			flash.addFlashAttribute("success", "Requisición de: " +requisicion.getSolicitante()+ " aprobada con éxito!");

		} else {
			flash.addFlashAttribute("error", "Requisición no existe en la bd");
		}

		return "redirect:/requisicion/listaRequisicionesNoAprob";
	}
	
	
	  @PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	  @RequestMapping(value = "/entregarRequisicion/{id}") 
	  public String entregarRequisicion(@PathVariable(value = "id") Long id, RedirectAttributes
	  flash, Model model, Authentication authentication) {
		  Requisicion requisicion = clienteService.encontrarRequisicion(id);
		  List<DetalleRequisicion> detreq =  clienteService.findById_Req(requisicion.getId_requi());
		  
		  
			for(DetalleRequisicion detrequisitos : detreq) {
				Kardex kardex = new Kardex();
				Articulo articulo = clienteService.findArticuloById(detrequisitos.getArticulo().getId_artic());
				Integer cantidad = detrequisitos.getCantidad();
				//cálculo y guardado de existencia 
				Integer existencia_actual = articulo.getExistencia();
				if(existencia_actual-cantidad>=0 || articulo.getReq_max()>= cantidad) {
				Integer act_exist = existencia_actual-cantidad;
				Integer exist_act_salida= articulo.getTot_salidas()+cantidad;//sumamos el total de salidas anterior a la cantidad que va de salida
				articulo.setExistencia(act_exist);//guardamos 
				articulo.setTot_salidas(exist_act_salida);//guardamos la existencia actual de salidas
				}else {
					model.addAttribute("titulo", "Formulario de Requisicion");
					model.addAttribute("error", "Error: Un articulo excede la cantidad en inventario, no se puede entregar la requisición");
					return "requisicion/formRequisicion"; 
				}
				
				
				//cálculo y guardado total costo en inventario
				Double total_movimi = articulo.getPrecio_prom()*cantidad;
				Double costo_tot = articulo.getCosto_total()-total_movimi;
				articulo.setCosto_total(costo_tot);
				
				//Cálculo y guardado del costo total de salidas
				Double total_salid = total_movimi+articulo.getCosto_totsalida();//calculamos el costo total de las salidas la cual es: total_movi de la requisición actual más el costo_totsalida anterior
				articulo.setCosto_totsalida(total_salid);  //guardamos el costo total de la salida
				
				// guardado de kardex
				kardex.setArticulo(articulo);
				kardex.setCantidad(cantidad);
				kardex.setPrecio(articulo.getPrecio_prom());
				kardex.setTipo_mov(detrequisitos.getTipo_mov()); 
				
				kardex.setDet_req(detrequisitos);
				//Calculos antes de guardar los respectivos valores
				
				
				clienteService.saveKardex(kardex);
				
				clienteService.saveArticulo(articulo);
				String estado ="Entregado";
				requisicion.setEstado(estado);
				clienteService.saveRequisicion(requisicion);
				flash.addFlashAttribute("success", "Requisición de: " +requisicion.getSolicitante()+ " Entregada con éxito!");
			}
	  List<Articulo> articulo = clienteService.encontrarArticulos();
	  for(Articulo articulos : articulo) {
			//calcular stock minimo
		  List<String> messages = new ArrayList<String>();
		  
			Integer exist =articulos.getExistencia();
			Integer min= articulos.getStock_min();
			if(exist<=min) {
				messages.add(articulos.getNombre());
				//flash.addFlashAttribute("warning", "Atención: Hay articulos que llegarón a la cantidad de stock mínima o estan por debajo, por favor verifique el inventario para no quedarse sin existencias.");
				
				logger.info("Evaluando inventario de: "+articulos.getNombre());
			}
	  }
	  return "redirect:/requisicion/listaRequisicionesAEntregar";
	  }
	  

		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_Gerente_Financiero','ROLE_ADMIN')")
		@RequestMapping(value = "/rechazarRequisicion/{id}")
		public String motivoRechazoRequisicion(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

			Requisicion requisicion = null;

			if (id > 0) {
				requisicion = clienteService.encontrarRequisicion(id);
				
			
				if (requisicion == null) {
					flash.addFlashAttribute("error", "El ID de la requisicion no existe en la Base de Datos!");
					return "redirect:/requisicion/listaRequisicionesNoAprob";
				}
			} else {
				flash.addFlashAttribute("error", "El ID de la requisicion no puede ser cero!");
				return "redirect:/requisicion/listaRequisicionesNoAprob";
			}
			model.put("requisicion", requisicion);
			model.put("titulo", "Motivo de rechazo");
			model.put("textoBoton", "Rechazar");
			return "requisicion/rechazarRequisicion";
			
		}
	  
		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN','ROLE_Gerente_Financiero')")
		@RequestMapping(value = {"/rechazarRequisicion"}, method = RequestMethod.POST)
		public String guardarRechazoRequisicion(@Valid Requisicion requisicion, BindingResult result, Model model,
		 RedirectAttributes flash, SessionStatus status) {

			requisicion.setEstado("Rechazado");
			    clienteService.saveRequisicion(requisicion);
				
				status.setComplete(); // elimina el objeto de la sesión.
				flash.addFlashAttribute("success", "Requisicion rechazada exitosamente");
				return "redirect:/requisicion/listaRequisicionesNoAprob";
			
			
		}
		
	/* Todos los métodos de detalle Requisición */
	  
	  @PreAuthorize("isFullyAuthenticated()")
		@GetMapping("/verDetalleRequisicion/{id}")
		public String verDetalleRequisicion(@PathVariable(value="id") Long id, 
				Model model, RedirectAttributes flash) {
			   
			Requisicion requisicion = clienteService.encontrarRequisicion(id);
			if(requisicion == null) {
				flash.addFlashAttribute("error", "La Requisicion no existe en la Base de Datos");
				return "redirect:/requisicion/listaRequisiciones";
			}
			
			model.addAttribute("requisicion", requisicion);
			model.addAttribute("titulo", "Requisicion de: "+requisicion.getSolicitante()+",  Cargo: "+requisicion.getCargo());
			
			return "requisicion/verDetalleRequisicion";
		}
	  
	  
	  @PreAuthorize("hasAnyRole('ROLE_Super_ADMIN', 'ROLE_ADMIN','ROLE_Gerente_Financiero')")
			@GetMapping("/verDetalleRequisicionVG/{id}")
			public String verDetalleRequisicionVG(@PathVariable(value="id") Long id, 
					Model model, RedirectAttributes flash) {
				   
				Requisicion requisicion = clienteService.encontrarRequisicion(id);
				if(requisicion == null) {
					flash.addFlashAttribute("error", "La Requisicion no existe en la Base de Datos");
					return "redirect:/requisicion/listaRequisicionesNoAprob";
				}
				
				model.addAttribute("requisicion", requisicion);
				model.addAttribute("titulo", "Requisicion de: "+requisicion.getSolicitante()+",  Cargo: "+requisicion.getCargo());
				
				return "requisicion/verDetalleRequisicionVG";
			}
	  
	  @PreAuthorize("hasAnyRole('ROLE_Super_ADMIN', 'ROLE_ADMIN','ROLE_Gerente_Financiero')")
		@GetMapping("/verDetalleRequisicionV/{id}")
		public String verDetalleRequisicionV(@PathVariable(value="id") Long id, 
				Model model, RedirectAttributes flash) {
			   
			Requisicion requisicion = clienteService.encontrarRequisicion(id);
			if(requisicion == null) {
				flash.addFlashAttribute("error", "La Requisicion no existe en la Base de Datos");
				return "redirect:/reportes/formReporteRequisicionXfechas";
			}
			
			model.addAttribute("requisicion", requisicion);
			model.addAttribute("titulo", "Requisicion de: "+requisicion.getSolicitante()+",  Cargo: "+requisicion.getCargo());
			
			return "requisicion/verDetalleRequisicionV";
		}
	  
	 
	//guardar de editar
		@PreAuthorize("isFullyAuthenticated()")
		@RequestMapping(value = "/formEditarDetalleRequisicion", method = RequestMethod.POST)
		public String guardarRequisicionEditada(@Valid DetalleRequisicion detreq, BindingResult result, Model model,
				RedirectAttributes flash, SessionStatus status) {

			if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
										// que valide
				model.addAttribute("titulo", "Formulario de Requisicion");
				model.addAttribute("textoBoton", "Editar");
				model.addAttribute("error", "Error:");

				return "requisicion/formEditarDetalleRequisicion"; // si tiene error va redirigir al formulario

			}
			Long id_art = detreq.getArticulo().getId_artic();
				Articulo articulo = clienteService.findArticuloById(id_art);//obtener el producto por cada linea
				DetalleRequisicion detrequi= clienteService.encontrarDetalleReq(detreq.getId_det_req());
				Requisicion requisicion = clienteService.encontrarRequisicion(detrequi.getRequisicion().getId_requi());
				Integer existencia_actual = articulo.getExistencia();
				
				
				if(articulo.getReq_max() < detreq.getCantidad()) {
					model.addAttribute("titulo", "Formulario de Requisicion");
					model.addAttribute("error", "Error: La cantidad de: "+articulo.getNombre()+" maxima para pedir es de: "+articulo.getReq_max()+ " , por favor pida una cantidad menor.");
					model.addAttribute("textoBoton", "Editar");
					return "requisicion/formEditarDetalleRequisicion";
				}else if(existencia_actual-detreq.getCantidad()<0) {
					model.addAttribute("titulo", "Formulario de Requisicion");
					model.addAttribute("error", "Aviso: Unicamente hay "+existencia_actual+" en existencia del articulo "+articulo.getNombre()+" , por favor pida una cantidad menor.");
					model.addAttribute("textoBoton", "Editar");
					return "requisicion/formEditarDetalleRequisicion"; 
				}
				
				detrequi.setCantidad(detreq.getCantidad());
				
			//	requisicion.setResponsbl("Andrés Bejarano");
				
				
				
				clienteService.saveDetalleRequisicion(detrequi);


			status.setComplete();
			
			flash.addFlashAttribute("success", "Detalle de requisicion editada con éxito!");
			return "redirect:/requisicion/verDetalleRequisicion/"+detrequi.getRequisicion().getId_requi();
				
				
	}//nuevo guardar
		
		@PreAuthorize("isFullyAuthenticated()")
		@RequestMapping(value = "/formEditarDetalleRequisicion/{id}")
		public String editarDetalleRequisicion(@PathVariable(value = "id") Long id, Map<String, Object> model,
				RedirectAttributes flash) {

			DetalleRequisicion detreq = null;
			logger.info(" id:"+id);

			if (id > 0) {
		detreq = clienteService.encontrarDetalleReq(id);
		
				
				if (detreq == null) {
					flash.addFlashAttribute("error", "El ID del detalle de requisicion no existe en la Base de Datos!");
					logger.info("no existe hay id:");
					
					return "redirect:/requisicion/misRequisiciones";
				}
			} else {
				flash.addFlashAttribute("error", "El ID del Detalle de requisicion no puede ser cero!");
				return "redirect:/requisicion/misRequisiciones";
			}
			
			model.put("detreq", detreq);
			model.put("titulo", "Editar Detalle de requisicion");
			model.put("textoBoton", "Editar");
			return "requisicion/formEditarDetalleRequisicion";
		}
		
		//probar
		@PreAuthorize("isFullyAuthenticated()")
		@GetMapping("/eliminarDetalleRequisicion/{id}")
		public String eliminarDetalleRequisicion(@PathVariable(value="id")Long id,
				RedirectAttributes flash) {
			DetalleRequisicion detreq = clienteService.encontrarDetalleReq(id);
			
			
			Requisicion requisicion = clienteService.encontrarRequisicion(detreq.getRequisicion().getId_requi());
			logger.info("id: "+detreq.getId_det_req());
			logger.info("id: "+requisicion);
			logger.info("id: "+requisicion.getEstado());
			logger.info("id: "+detreq.getRequisicion().getId_requi());
			
			
			if(detreq != null) {
				String estado = requisicion.getEstado();
				if(estado.equals("Pendiente")) {
					
				clienteService.deleteDetalleRequisicion(id);
				
                List<DetalleRequisicion> detreqcomp =  clienteService.findById_Req(requisicion.getId_requi());
				
				if(detreqcomp.isEmpty()) {
					clienteService.EliminarRequisicion(requisicion.getId_requi());
					flash.addFlashAttribute("info", "Se borró la Requisición debido a que ya no contenia artículos");
					return "redirect:/requisicion/misRequisiciones";
				}
				
				flash.addFlashAttribute("success", "Detalle de Requisición eliminado con éxito!");
				return "redirect:/requisicion/verDetalleRequisicion/"+detreq.getRequisicion().getId_requi();
			}
				
			}
			flash.addFlashAttribute("error", "El detalle de requisición no existe en la base de datos o cambio su estado, no se pudo eliminar");
			return "redirect:/requisicion/misRequisiciones";
		}
		
		
		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
		@RequestMapping(value = "/anularRequisicion/{id}")
		public String motivoAnulacionRequisicion(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

			Requisicion requisicion = null;

			if (id > 0) {
				requisicion = clienteService.encontrarRequisicion(id);
				
			
				if (requisicion == null) {
					flash.addFlashAttribute("error", "El ID de la requisicion no existe en la Base de Datos!");
					return "redirect:/requisicion/listaRequisiciones";
				}
			} else {
				flash.addFlashAttribute("error", "El ID de la requisicion no puede ser cero!");
				return "redirect:/requisicion/listaRequisiciones";
			}
			model.put("requisicion", requisicion);
			model.put("titulo", "Motivo de Anulación");
			model.put("textoBoton", "Anular");
			return "requisicion/anularRequisicion";
			
		}
		
		
		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
		  @RequestMapping(value = "/anularRequisicion") 
		  public String guardarAnulacionRequisicion(@Valid Requisicion requisicion, BindingResult result, Model model,
					 RedirectAttributes flash, SessionStatus status) {
			  List<DetalleRequisicion> detreq =  clienteService.findById_Req(requisicion.getId_requi());
			  
			  
				for(DetalleRequisicion detrequisitos : detreq) {
					String estado = requisicion.getEstado();
					if(estado.equals("Entregado")) {
					try { //Probamos guardar la entrada mediante un try catch para manejar la exepcion.
						clienteService.deleteByDetalleReq(detrequisitos.getId_det_req()); //Mediante la interfaz clienteService y el metodo EliminarKardex eliminamos cada linea de la requisicion en el kardex.
						} catch (Exception  e) {// si falla el guardado			
								        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
								           flash.addFlashAttribute("error", "Ocurrio un error al eliminar los detalles de requisición del kardex. Por favor contacte al administrador del sistema!");// agregamos el mensaje de error que indica que el nombre de la factura ya esxiste.
								           return "redirect:/requisicion/listaRequisiciones";//redireccionamos de nuevo hacia el listado.
				        
						}
					
					Articulo articulo = clienteService.findArticuloById(detrequisitos.getArticulo().getId_artic());
					Integer cantidad = detrequisitos.getCantidad();
					//cálculo y guardado de existencia 
					
	  				//costo total
	  				Double totmov_actual = detrequisitos.getPrecio()*detrequisitos.getCantidad(); //se calcula el total actual del movimiento
	  				Double costo_tot = articulo.getCosto_total()+totmov_actual; // se resta al costo total actualizado el valor total actual del movimiento
	  				articulo.setCosto_total(costo_tot);
	  				
	  				//existencia articulo
	  				Integer cant_exist = articulo.getExistencia()+detrequisitos.getCantidad();
	  				articulo.setExistencia(cant_exist);
	  				
	  				Double precio_promedio = costo_tot/cant_exist;
	  			    articulo.setPrecio_prom(precio_promedio);
	  			    
					//Cálculo y guardado del costo total de salidas
					Double total_salid = articulo.getCosto_totsalida()-totmov_actual;//calculamos el costo total de las salidas la cual es: total_movi de la requisición actual más el costo_totsalida anterior
					articulo.setCosto_totsalida(total_salid);  //guardamos el costo total de la salida
					
					Integer exist_act_salida= articulo.getTot_salidas()-cantidad;//sumamos el total de salidas anterior a la cantidad que va de salida
					articulo.setTot_salidas(exist_act_salida);//guardamos la existencia actual de salidas
					
					clienteService.saveArticulo(articulo);
					
					}
					else if(estado.equals("Anulada")) {
						flash.addFlashAttribute("success", "La Requisición #: " +requisicion.getId_requi()+ " ya fue anulada!");
						
						 
						  return "redirect:/requisicion/listaRequisiciones";
					}
					}
				String estadoset ="Anulada";
				requisicion.setEstado(estadoset);
				clienteService.saveRequisicion(requisicion);
				flash.addFlashAttribute("success", "Requisición #: " +requisicion.getId_requi()+ " Anulada con éxito!");
			
		 
		  return "redirect:/requisicion/listaRequisiciones";
		  }
}

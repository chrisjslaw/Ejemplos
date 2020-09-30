package com.AMHON.springboot.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.DetalleRequisicion;
import com.AMHON.springboot.app.models.entity.Entrada;
import com.AMHON.springboot.app.models.entity.Requisicion;
import com.AMHON.springboot.app.models.service.IClienteService;
import com.fasterxml.jackson.annotation.JsonView;

@Controller //Especificamos que el controllador sera de tipo rest
@RequestMapping(value="/graficas") // la ruta que utilizará para mandar peticiones a este controllador y a sus métodos.
public class GraficasController {

	@Autowired
	private IClienteService clienteService;
	
	private final Log logger = LogFactory.getLog(this.getClass());  // codigo para implementar la función de log en la consola
	
	String pattern = "yyyy-MM-dd"; //definimos una variable para guardar el patron a utiliza para formatear las fechas.
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern); //utilizamos la clase SimpleDateFormat para darle formato a las fechas recibidas pasando el patron que definimos anteriormente

	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/listadoGraficas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String listadoGraficas(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		model.put("titulo", "Listado de Graficas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		return "graficas/listadoGraficas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	// metodo de cargar datos Grafica entradasxfechas
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@GetMapping(value="/cargar-entradas/{term}/{term2}", produces= {"application/json"})
	public @ResponseBody List<Entrada> cargarEntradas(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date term, 
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date term2) {
		logger.info("lo que tiene fecha inicial: "+term); 
		logger.info("lo que tiene fecha final: "+term2);
	
		return clienteService.findByFechasEntrada(term, term2);
	}
	
	
	//Grafica entradasxfechas
	

	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/formGraficaEntradaXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
	// entrar en accion cuando se utiliza dicha ruta dentro de la aplicación
	public String BuscarEntradaXFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		model.put("titulo", "Filtrar Entradas por fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "graficas/formGraficaEntradaXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	//vista
	
		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
		@RequestMapping(value = "/formGraficaEntradaXfechas" , method = RequestMethod.POST) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
		public String MostrarGraficaEntradas(Model model, @RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // se habilita el model para pasar información a la vista.
				@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
				RedirectAttributes flash, SessionStatus status) {

			   model.addAttribute("fechain", simpleDateFormat.format(fechaIn)); //agregamos al modelo la variable de fecha inicial
			    model.addAttribute("fechafin", simpleDateFormat.format(fechaFin)); //agregamos al modelo la variable de fecha final
		model.addAttribute("titulo", "Gráfico de Entradas desde: "+simpleDateFormat.format(fechaIn)+" Hasta el: "+simpleDateFormat.format(fechaFin)+"."); //agregamos al modelo un titulo
			return "graficas/graficaEntrada"; // retornamos el nombre de la vista
		}
	
	
		
		
		// metodo de cargar datos Grafica requisxDeptxfechas
		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
		@GetMapping(value="/cargar-requi/{term}/{term2}", produces= {"application/json"})
		public @ResponseBody List<DetalleRequisicion> cargarRequisicionesxDep(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date term, 
				@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date term2) {
			logger.info("lo que tiene fecha inicial: "+term); 
			logger.info("lo que tiene fecha final: "+term2);
		
			return clienteService.GastosxRequisicionxdepartamentoxFechamodifc(term, term2);
		}
		
		
		//Grafica RequisicionesxDepartxfechas
		

		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
		@GetMapping(value = "/formGraficaRequisicionesxDepXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
		// entrar en accion cuando se utiliza dicha ruta dentro de la aplicación
		public String BuscarRequisicionesXFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
														// un map de java util

			model.put("titulo", "Filtrar Requisiciones por departamentos por rango de fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
			model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
			return "graficas/formGraficaRequisicionesxDepXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
		}
		
		//vista
		
			@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
			@RequestMapping(value = "/formGraficaRequisicionesxDepXfechas" , method = RequestMethod.POST) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
			public String MostrarGraficaRequisiciones(Model model, @RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // se habilita el model para pasar información a la vista.
					@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
					RedirectAttributes flash, SessionStatus status) {

				   model.addAttribute("fechain", simpleDateFormat.format(fechaIn)); //agregamos al modelo la variable de fecha inicial
				    model.addAttribute("fechafin", simpleDateFormat.format(fechaFin)); //agregamos al modelo la variable de fecha final
			model.addAttribute("titulo", "Gráfico de Requisiciones desde: "+simpleDateFormat.format(fechaIn)+" Hasta el: "+simpleDateFormat.format(fechaFin)+"."); //agregamos al modelo un titulo
				return "graficas/graficaRequixDeptos"; // retornamos el nombre de la vista
			}
			
			
		
				
				
				
				// metodo de cargar datos Grafica entradasxfechas
				@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
				@GetMapping(value="/cargar-entradas-prov/{term}/{term2}", produces= {"application/json"})
				public @ResponseBody List<DetalleEntrada> cargarEntradasxProv(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date term, 
						@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date term2) {
					logger.info("lo que tiene fecha inicial: "+term); 
					logger.info("lo que tiene fecha final: "+term2);
				
					return clienteService.GastosxEntradaxproveedorxFecha(term, term2); //Retornamos los datos que obtenemos desde la interfaza clienteService del método GastosxEntradaxproveedorxFecha al pasarle los rangos de fecha que recibe el controlador a través de una consulta ajax
				}
				
				
				//Grafica EntradasxProvxFecha
				

				@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
				@GetMapping(value = "/formGraficaEntradasXfechasxProv") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
				// entrar en accion cuando se utiliza dicha ruta dentro de la aplicación
				public String BuscarEntradasxProvXFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
																// un map de java util

					model.put("titulo", "Filtrar entradas de proveedores en un rango de fechas fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
					model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
					return "graficas/formGraficaEntradasXfechasxProv"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
				}
				
				//vista
				
					@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
					@RequestMapping(value = "/formGraficaEntradasXfechasxProv" , method = RequestMethod.POST) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
					public String MostrarGraficaEntradasxProv(Model model, @RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // se habilita el model para pasar información a la vista.
							@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
							RedirectAttributes flash, SessionStatus status) {

						   model.addAttribute("fechain", simpleDateFormat.format(fechaIn)); //agregamos al modelo la variable de fecha inicial
						    model.addAttribute("fechafin", simpleDateFormat.format(fechaFin)); //agregamos al modelo la variable de fecha final
					model.addAttribute("titulo", "Gráfico de Entradas por proveedor desde: "+simpleDateFormat.format(fechaIn)+" Hasta el: "+simpleDateFormat.format(fechaFin)+"."); //agregamos al modelo un titulo
						return "graficas/graficaEntrxProv"; // retornamos el nombre de la vista
					}
					
					
					
					// metodo de cargar datos Grafica entradasxArticulosxfechas
					@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
					@GetMapping(value="/cargar-entradas-artic/{term}/{term2}", produces= {"application/json"})
					public @ResponseBody List<Entrada> cargarEntradasxArticulos(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date term, 
							@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date term2) {
						logger.info("lo que tiene fecha inicial: "+term); 
						logger.info("lo que tiene fecha final: "+term2);
					
						return clienteService.findByCantidadxArticulosEntrantexfechasGrafic(term, term2); //Retornamos los datos que obtenemos desde la interfaza clienteService del método GastosxEntradaxproveedorxFecha al pasarle los rangos de fecha que recibe el controlador a través de una consulta ajax
					}
					
					
					
					//Grafica EntradasxArticulosxFecha
					
					@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
					@GetMapping(value = "/formGraficaEntradasxArticXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
					// entrar en accion cuando se utiliza dicha ruta dentro de la aplicación
					public String BuscarEntradasxArticXFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
																	// un map de java util

						model.put("titulo", "Filtrar entradas de articulos por rango de fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
						model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
						return "graficas/formGraficaEntradasxArticXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
					}
					
					//vista
					
						@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
						@RequestMapping(value = "/formGraficaEntradasxArticXfechas" , method = RequestMethod.POST) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
						public String MostrarGraficaEntradasxArtic(Model model, @RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // se habilita el model para pasar información a la vista.
								@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
								RedirectAttributes flash, SessionStatus status) {
							

							   model.addAttribute("fechain", simpleDateFormat.format(fechaIn)); //agregamos al modelo la variable de fecha inicial
							    model.addAttribute("fechafin", simpleDateFormat.format(fechaFin)); //agregamos al modelo la variable de fecha final
						model.addAttribute("titulo", "Gráficos: Total Entradas de Artículos desde: "+simpleDateFormat.format(fechaIn)+" Hasta el: "+simpleDateFormat.format(fechaFin)+"."); //agregamos al modelo un titulo
							return "graficas/graficaEntrxArtic"; // retornamos el nombre de la vista
						}
					
				
						// metodo de cargar datos Grafica salidasxArticulosxfechas
						@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
						@GetMapping(value="/cargar-salid-artic/{term}/{term2}", produces= {"application/json"})
						public @ResponseBody List<Requisicion> cargarSalidasxArticulos(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date term, 
								@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date term2) {
							logger.info("lo que tiene fecha inicial: "+term); 
							logger.info("lo que tiene fecha final: "+term2);
						
							return clienteService.findByFechasRequisicionesEntregadasGrafic(term, term2); //Retornamos los datos que obtenemos desde la interfaza clienteService del método GastosxEntradaxproveedorxFecha al pasarle los rangos de fecha que recibe el controlador a través de una consulta ajax
						}
						
						
						
						//Grafica SalidasxArticulosxFecha
						
						@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
						@GetMapping(value = "/formGraficaSalidasxArticXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
						// entrar en accion cuando se utiliza dicha ruta dentro de la aplicación
						public String BuscarSalidasxArticXFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
																		// un map de java util

							model.put("titulo", "Filtrar Salidas de articulos por rango de fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
							model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
							return "graficas/formGraficaSalidasxArticXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
						}
						
						//vista
						
							@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
							@RequestMapping(value = "/formGraficaSalidasxArticXfechas" , method = RequestMethod.POST) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
							public String MostrarGraficaSalidasxArtic(Model model, @RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // se habilita el model para pasar información a la vista.
									@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
									RedirectAttributes flash, SessionStatus status) {
								
								
								   model.addAttribute("fechain", simpleDateFormat.format(fechaIn)); //agregamos al modelo la variable de fecha inicial
								    model.addAttribute("fechafin", simpleDateFormat.format(fechaFin)); //agregamos al modelo la variable de fecha final
							model.addAttribute("titulo", "Gráficos: Total Salidas de Artículos desde: "+simpleDateFormat.format(fechaIn)+" Hasta el: "+simpleDateFormat.format(fechaFin)+"."); //agregamos al modelo un titulo
								return "graficas/graficaSalidxArtic"; // retornamos el nombre de la vista
							}
						
}

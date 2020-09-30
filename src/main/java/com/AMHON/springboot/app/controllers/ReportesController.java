package com.AMHON.springboot.app.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.Departamento;
import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.DetalleRequisicion;
import com.AMHON.springboot.app.models.entity.Entrada;
import com.AMHON.springboot.app.models.entity.Kardex;
import com.AMHON.springboot.app.models.entity.Proveedor;
import com.AMHON.springboot.app.models.entity.Requisicion;
import com.AMHON.springboot.app.models.entity.Usuario;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller // anotación para representar un controllador
@RequestMapping(value="/reportes") // la ruta que utilizará para mandar peticiones a este controllador y a sus métodos.
@SessionAttributes(value= {"entrada","Requisicion","kardex", "detmov", "articulos","detreq", "usuarios", "depto", "usuario"}) //nombre del o de los atributos a utilizar en los model en donde se guardara en la sessión al pasar los atributos en el model con este nombre quedaran guardados en sesión hasta que dicha sesión se cierre.
public class ReportesController {

	@Autowired
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos que interactuan con la base de datos.
	
	private final Log logger = LogFactory.getLog(this.getClass());  // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/listadoReportes") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String listadoReportes(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		model.put("titulo", "Listado de reportes"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/listadoReportes"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	//reporte de entradas por rango de fecha
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/formReporteEntradaXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
	// entrar en accion cuando se utiliza dicha ruta dentro de la aplicación
	public String BuscarEntradaXFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		model.put("titulo", "Filtrar Entradas por fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/formReporteEntradaXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	@RequestMapping(value = "/formReporteEntradaXfechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
	public String GenerarReporteEntrada( Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
			@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
			RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

        logger.info("Fecha inicio: "+ fechaIn); //se realiza un log en la consola indicando la exepción.
        logger.info("Fecha fin: "+ fechaFin); 
       if(fechaIn.after(fechaFin)) {
    	   
       }
        if(fechaIn==null ||fechaFin==null) {
        	fechaIn= new Date();
	      fechaFin= new Date();
        }
		List<Entrada> entrada = clienteService.findByFechasEntrada(fechaIn, fechaFin);
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		
		
		model.addAttribute("entrada", entrada); // agregamos al modelo el nombre y el valor del atributo depto el cual se guarda en la sesión.
		model.addAttribute("titulo", "Reporte: Entradas desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/ReporteEntradaXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	
	//requisicion
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/formReporteRequisicionXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String BuscarRequisicionXFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		model.put("titulo", "Filtrar Requisiciones por fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/formReporteRequisicionXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	@RequestMapping(value = "/formReporteRequisicionXfechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
	public String GenerarReporteRequisicion( Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
			@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
			RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

        logger.info("Fecha inicio: "+ fechaIn); //se realiza un log en la consola indicando la exepción.
        logger.info("Fecha fin: "+ fechaFin); 
       
        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
	      
        }else if(fechaFin==null){
        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
        }
        
        if(fechaFin.before(fechaIn)) {
        	
        }
		List<Requisicion> requisicion = clienteService.findByFechasRequisicion(fechaIn, fechaFin);
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		
		
		model.addAttribute("Requisicion", requisicion); // agregamos al modelo el nombre y el valor del atributo depto el cual se guarda en la sesión.
		model.addAttribute("titulo", "Reporte: Requisiciones desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/ReporteRequisicionXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	//Kardex filtrado por rango de fecha

	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/formReporteKardexXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String BuscarKardexXFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		model.put("titulo", "Reporte:Inventario Kardex filtrado por fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/formReporteKardexXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	@RequestMapping(value = "/formReporteKardexXfechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
	public String GenerarReporteKardex( Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
			@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
			RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

        logger.info("Fecha inicio: "+ fechaIn); //se realiza un log en la consola indicando la exepción.
        logger.info("Fecha fin: "+ fechaFin); 
       
        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
	      
        }else if(fechaFin==null){
        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
        }
        
        if(fechaFin.before(fechaIn)) {
	        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
	        return "redirect:/reportes/formReporteKardexXfechas";//redireccionamos de nuevo hacia el formulario.

        }
		List<Kardex> kardex = clienteService.findByFechasKardex(fechaIn, fechaFin); //inicializamos una lista de tipo Kardex y en ella guardamos los registros que cumplan con los parametros establecidos.
		String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
		
		
		model.addAttribute("kardex", kardex); // agregamos al modelo el nombre y el valor del atributo depto el cual se guarda en la sesión.
		model.addAttribute("titulo", "Reporte: Kardex desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/ReporteKardexXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	
	// Cantidad de articulos entrantes 
	
		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
		@GetMapping(value = "/formReporteCantxArticulosEntrantesXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
											// mostrar el form
		public String BuscarCantArticulosEntradaxFechas(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
														// un map de java util

			model.put("titulo", "Cantidad artículos Entrantes por rango de fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
			model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
			return "reportes/formReporteCantxArticulosEntrantesXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
		}
		
		
		// vista
		
		@RequestMapping(value = "/formReporteCantxArticulosEntrantesXfechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
		public String GenerarReporteCantxArticulosEntrantesxFechas(  Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
				@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
				RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

	        logger.info("Fecha inicio: "+ fechaIn); //se realiza un log en la consola indicando la exepción.
	        logger.info("Fecha fin: "+ fechaFin); 
	       
	        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
	        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
		      
	        }else if(fechaFin==null){
	        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
	        }
	        
	        if(fechaFin.before(fechaIn)) {
		        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
		        return "redirect:/reportes/formReporteCantxArticulosEntrantesXfechas";//redireccionamos de nuevo hacia el formulario.

	        }
	        
			List<Entrada> entrada = clienteService.findByCantidadxArticulosEntrantexfechas(fechaIn, fechaFin); //inicializamos una lista de tipo DetalleEntrada y en ella guardamos los registros que cumplan con los parametros establecidos que recibe la funcion findByProductxProveedor, la cual se encarga de buscar los registros que contengan estos mismos.
			String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
			
			
			model.addAttribute("entrada", entrada); // agregamos al modelo el nombre y el valor del atributo requisicion el cual se guarda en la sesión.
			model.addAttribute("titulo", "Reporte: Cantidad entrante de artículos desde fecha "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
			return "reportes/ReporteCantxArticulosEntradaxFecha"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
		}
	
	
	// Articulos entrantes por proveedor
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/formReporteArticulosXfechasxProveedores") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String BuscarArticulosxFechasxProv(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		List<Proveedor> proveedores = clienteService.encontrarProveedores();// instanciamos una lista de proveedor para guardar los registros de los proveedores recuperados mediante la interfaz clienteService y el método encontrarProveedores
		model.put("titulo", "Articulos Entrantes filtrados por fechas y proveedor"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("proveedor", proveedores); // agregamos al modelo el atributo proveedores
		model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/formReporteArticulosXfechasxProveedores"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	@RequestMapping(value = "/formReporteArticulosXfechasxProveedores", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
	public String GenerarReporteArticulosxFechasxProv(  Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
			@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
			@RequestParam(value="prov", required=false) Long id,
			RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

        logger.info("Fecha inicio: "+ fechaIn); //se realiza un log en la consola indicando la exepción.
        logger.info("Fecha fin: "+ fechaFin); 
       
        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
	      
        }else if(fechaFin==null){
        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
        }
        
        if(fechaFin.before(fechaIn)) {
	        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
	        return "redirect:/reportes/formReporteArticulosXfechasxProv";//redireccionamos de nuevo hacia el formulario.

        }
        Proveedor proveedor = clienteService.encontrarProveedor(id); // inicializamos el objeto proveedor, el cual se utilizara para agregar info al titulo y verificar que la información sea valida.
        
		List<DetalleEntrada> detmov = clienteService.findByProductxProveedor(fechaIn, fechaFin, id); //inicializamos una lista de tipo Kardex y en ella guardamos los registros que cumplan con los parametros establecidos.
		String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
		
		
		model.addAttribute("detmocv", detmov); // agregamos al modelo el nombre y el valor del atributo depto el cual se guarda en la sesión.
		model.addAttribute("titulo", "Reporte: Entrada de productos del proveedor: "+proveedor.getNombre_prov()+" desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/ReporteArticulosXfechasxProveedor"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	
	
	// articulos entregados
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/formReporteArticulosEntregadosXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String BuscarArticuloEntregadoxFechas(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		model.put("titulo", "Artículos Entregados por rango de fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/formReporteArticulosEntregadosXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	
	
	@RequestMapping(value = "/formReporteArticulosEntregadosXfechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
	public String GenerarReporteArticulosEntregadoxFechas(  Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
			@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
			RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

        logger.info("Fecha inicio: "+ fechaIn); //se realiza un log en la consola indicando la exepción.
        logger.info("Fecha fin: "+ fechaFin); 
       
        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
	      
        }else if(fechaFin==null){
        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
        }
        
        if(fechaFin.before(fechaIn)) {
	        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
	        return "redirect:/reportes/formReporteArticulosEntregadosXfechas";//redireccionamos de nuevo hacia el formulario.

        }
        
		List<Requisicion> requisicion = clienteService.findByFechasRequisicionesEntregadas(fechaIn, fechaFin); //inicializamos una lista de tipo DetalleEntrada y en ella guardamos los registros que cumplan con los parametros establecidos que recibe la funcion findByProductxProveedor, la cual se encarga de buscar los registros que contengan estos mismos.
		String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
		
		
		model.addAttribute("Requisicion", requisicion); // agregamos al modelo el nombre y el valor del atributo requisicion el cual se guarda en la sesión.
		model.addAttribute("titulo", "Reporte: Cantidad de salida de artículos desde fecha "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/ReporteArticulosEntregadosxFecha"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	
	// Reporte Entradas por articulos por fecha
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@GetMapping(value = "/formReporteEntradasxArticuloXfechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String BuscarEntradasxArticuloxFechas(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util

		List<Articulo> articulos = clienteService.encontrarArticulos(); // instanciamos una lista de proveedor para guardar los registros de los proveedores recuperados mediante la interfaz clienteService y el método encontrarProveedores
		model.put("titulo", "Entradas filtrados por articulo y rango de fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("articulos", articulos); // agregamos al modelo el atributo articulos
		model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/formReporteEntradasxArticuloXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}
	
	//vista
	
	@RequestMapping(value = "/formReporteEntradasxArticuloXfechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
	public String GenerarReporteEntradaxArticuloxFechas(  Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
			@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
			@RequestParam(value="artic", required=false) Long id,
			RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

        logger.info("Fecha inicio: "+ fechaIn); //se realiza un log en la consola indicando la exepción.
        logger.info("Fecha fin: "+ fechaFin); 
       
        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
	      
        }else if(fechaFin==null){
        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
        }
        
        if(fechaFin.before(fechaIn)) {
	        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
	        return "redirect:/reportes/formReporteEntradasxArticuloXfechas";//redireccionamos de nuevo hacia el formulario.

        }
        Articulo articulo = clienteService.findArticuloById(id); //instanciamos el objeto articulo para guardar su registro y utilizarlo en el controlador, especificamente en el titulo
        logger.info("Id del articulo bro: "+ id); 
		List<DetalleEntrada> detmov = clienteService.findByEntradasxProductxFecha(fechaIn, fechaFin, id); //inicializamos una lista de tipo DetalleEntrada y en ella guardamos los registros que cumplan con los parametros establecidos que recibe la funcion findByProductxProveedor, la cual se encarga de buscar los registros que contengan estos mismos.
		String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
		
		
		model.addAttribute("detmocv", detmov); // agregamos al modelo el nombre y el valor del atributo detmov el cual se guarda en la sesión.
		model.addAttribute("titulo", "Reporte: Entradas del Articulo: "+articulo.getNombre()+" desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		return "reportes/ReporteEntradasxArticuloXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
	}


	// Reporte Entregas de articulos por usuario y fecha
	
		@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
		@GetMapping(value = "/formReporteRequisicionesEntregadasxUsuariosxFechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
											// mostrar el form
		public String BuscarArticulosEntregadosxUsuariosxFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
														// un map de java util

			model.put("titulo", "Articulos entregados filtrado por usuario y rango de fechas"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
			model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
			return "reportes/formReporteRequisicionesEntregadasxUsuariosxFechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
		}
		
		//vista del reporte
		
		@RequestMapping(value = "/formReporteRequisicionesEntregadasxUsuariosxFechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
		public String GenerarReporteArticulosEntregadosxUsuarioxFecha(  Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
				@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
				RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

	        
	       
	        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
	        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
		      
	        }else if(fechaFin==null){
	        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
	        }
	        
	        if(fechaFin.before(fechaIn)) {
		        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
		        return "redirect:/reportes/formReporteRequisicionesEntregadasxUsuariosxFechas";//redireccionamos de nuevo hacia el formulario.

	        }
	        
			List<DetalleRequisicion> detreq = clienteService.findByRequisicionesxArticulosxUsuarioxFecha(fechaIn, fechaFin); //inicializamos una lista de tipo DetalleRequisicion y en ella guardamos los registros que cumplan con los parametros establecidos que recibe la funcion findByRequisicionesxArticulosxUsuarioxFecha, la cual se encarga de buscar los registros que contengan estos mismos.
			String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
			
			
			model.addAttribute("detreq", detreq); // agregamos al modelo el nombre y el valor del atributo detreq el cual se guarda en la sesión.
			model.addAttribute("titulo", "Reporte: Articulos Entregados por Usuario con rango de fecha desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
			return "reportes/ReporteEntregasxUsuarioxArticuloXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
		}
	
		
		// Reporte Gastos por departamento filtrado por rango de fecha
		
			@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
			@GetMapping(value = "/formReporteGastosxDepartamentosxFechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
												// mostrar el form
			public String BuscarGastosxDepartamentosxFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
															// un map de java util

				model.put("titulo", "Filtrar por fecha"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
				model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
				return "reportes/formReporteGastosxDepartamentosxFechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
			}
			
			//vista del reporte
			
			@RequestMapping(value = "/formReporteGastosxDepartamentosxFechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
			public String GenerarReporteGastosxDepartamentoxFecha(  Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
					@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
					RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

		        
		       
		        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
		        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
			      
		        }else if(fechaFin==null){
		        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
		        }
		        
		        if(fechaFin.before(fechaIn)) {
			        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
			        return "redirect:/reportes/formReporteGastosxDepartamentosxFechas";//redireccionamos de nuevo hacia el formulario.

		        }
		        
				List<DetalleRequisicion> detreq = clienteService.GastosxRequisicionxdepartamentoxFecha(fechaIn, fechaFin); //inicializamos una lista de tipo DetalleRequisicion y en ella guardamos los registros que cumplan con los parametros establecidos que recibe la funcion GastosxRequisicionxdepartamentoxFecha, la cual se encarga de buscar los registros que contengan estos mismos.
				String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
				
				
				model.addAttribute("detreq", detreq); // agregamos al modelo el nombre y el valor del atributo detreq el cual se guarda en la sesión.
				model.addAttribute("titulo", "Reporte: Gastos de departamentos con rango de fecha desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
				return "reportes/ReporteGastosxDepartamentosXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
			}
		
			
			
			// Reporte Articulos requeridos por usuario especifico en un rango de fechas
			
				@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
				@GetMapping(value = "/formReporteUsuarioxReqxFechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
													// mostrar el form
				public String BuscarRequisicionesxUsuarioxFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
																// un map de java util

					List<Usuario> usuarios = clienteService.encontrarUsuarios();
					
					model.put("titulo", "Filtrar por fecha y usuario específico"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
					model.put("usuarios", usuarios); // agregamos al modelo el atributo articulos
					model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
					return "reportes/formReporteUsuarioxReqxFechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
				}
				
				//vista del reporte
				
				@RequestMapping(value = "/formReporteUsuarioxReqxFechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
				public String GenerarReporteRequisicionesxUsuarioxFecha(  Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
						@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
						@RequestParam(value="usuar", required=false) Long id,
						RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

			        
			       
			        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
			        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
				      
			        }else if(fechaFin==null){
			        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
			        }
			        
			        if(fechaFin.before(fechaIn)) {
				        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
				        return "redirect:/reportes/formReporteUsuarioxReqxFechas";//redireccionamos de nuevo hacia el formulario.

			        }
			        
			        Requisicion requisicion = clienteService.encontrarRequisicion(id); //instanciamos el objeto articulo para guardar su registro y utilizarlo en el controlador, especificamente en el titulo.
					List<DetalleRequisicion> detreq = clienteService.findByRequisicionesxUsuarioxFecha(fechaIn, fechaFin, id); //inicializamos una lista de tipo DetalleRequisicion y en ella guardamos los registros que cumplan con los parametros establecidos que recibe la funcion GastosxRequisicionxdepartamentoxFecha, la cual se encarga de buscar los registros que contengan estos mismos.
					String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
					
					
					model.addAttribute("detreq", detreq); // agregamos al modelo el nombre y el valor del atributo detreq el cual se guarda en la sesión.
					model.addAttribute("titulo", "Reporte: Requisiciones por el usuario: "+requisicion.getSolicitante()+" con rango de fecha desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
					return "reportes/ReporteUsuariosxReqXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
				}
				
				
				// Reporte Gastos por departamento especifico en un rango de fechas
				
				@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
				@GetMapping(value = "/formReporteGastosxDepartmEspecifixFechas") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
													// mostrar el form
				public String BuscarGastoxDepartamentoxFecha(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
																// un map de java util

					List<Departamento> depto = clienteService.encontrarDepartamentos();
					
					model.put("titulo", "Filtrar por fecha y usuario"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
					model.put("depto", depto); // agregamos al modelo el atributo articulos
					model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
					return "reportes/formReporteGastosxDepartmEspecifixFechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
				}
				
				//vista del reporte
				
				@RequestMapping(value = "/formReporteGastosxDepartmEspecifixFechas", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
				public String GenerarReporteDepartamentoEspecificoxFecha(  Model model,@RequestParam(value="FechaIn", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIn, // se habilita el model para pasar información a la vista.
						@RequestParam(value="FechaFin", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
						@RequestParam(value="depa", required=false) Long id, //con la anotación RequestParam obtenemos los valores del formulario anterior
						RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

			        
			       
			        if(fechaIn==null ) { //verificamos si la fecha de inicio o la fecha final tienen valor null
			        	fechaIn= new Date(); //en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
				      
			        }else if(fechaFin==null){
			        	fechaFin= new Date();////en dado caso que fechasIn sea null se le asigna el valor del dia de hoy
			        }
			        
			        if(fechaFin.before(fechaIn)) {
				        flash.addFlashAttribute("error", "La fecha de Inicio es mayor a la final, por favor introduzca un rango de fecha valido");// agregamos el mensaje de error que indica que el rango de fecha es inavlido.
				        return "redirect:/reportes/formReporteGastosxDepartmEspecifixFechas";//redireccionamos de nuevo hacia el formulario.

			        }

					if (id == null) {
						flash.addFlashAttribute("error", "El Departamento no existe");
						return "redirect:/reportes/formReporteGastosxDepartmEspecifixFechas";
					}
			        Departamento depart = clienteService.encontrarDepartamento(id); //instanciamos el objeto departamento para guardar su registro y utilizarlo en el controlador, especificamente en el titulo.
					List<DetalleRequisicion> detreq = clienteService.findByDepartamentoEspecificoxFecha(fechaIn, fechaFin, depart.getNombre_depto()); //inicializamos una lista de tipo departamento y en ella guardamos los registros que cumplan con los parametros establecidos que recibe la funcion GastosxRequisicionxdepartamentoxFecha, la cual se encarga de buscar los registros que contengan estos mismos.
					String pattern = "dd-MM-yyyy"; //patron del formato para transformar la fecha
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);//le damos un formato simple a la fecha para que pueda ser utilizada en el titulo.
					
					
					model.addAttribute("detreq", detreq); // agregamos al modelo el nombre y el valor del atributo detreq el cual se guarda en la sesión.
					model.addAttribute("titulo", "Reporte: Gastos del departamento: "+depart.getNombre_depto()+" con rango de fecha desde "+simpleDateFormat.format(fechaIn)+" Hasta "+simpleDateFormat.format(fechaFin)); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
					return "reportes/ReporteGastosxDepartEspecificoXfechas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
				}
				
				
             // Reporte Usuarios por departamento especifico
				
				@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
				@GetMapping(value = "/formReporteUsuariosxDepartamento") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
													// mostrar el form
				public String BuscarUsuariosxDepartamento(Map<String, Object> model, Authentication authentication, RedirectAttributes flash) {// recibe el objeto model para poder pasar datos a la vista o bien
																// un map de java util

					List<Departamento> depto = clienteService.encontrarDepartamentos();
					
					model.put("titulo", "Filtrar usuarios por departamento específico"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
					model.put("depto", depto); // agregamos al modelo el atributo articulos
					model.put("textoBoton", "Buscar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
					return "reportes/formReporteUsuariosxDepartamento"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
				}
				
				//vista del reporte
				
				@RequestMapping(value = "/formReporteUsuariosxDepartamento", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
				public String GenerarReporteUsuariosxDepartamentoEspecifico(  Model model, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
						@RequestParam(value="depa", required=false) Long id,
						RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

					if (id == null) {
						flash.addFlashAttribute("error", "El Departamento no existe");
						return "redirect:/reportes/formReporteUsuariosxDepartamento";
					}
			        Departamento depart = clienteService.encontrarDepartamento(id); //instanciamos el objeto departamento para guardar su registro y utilizarlo en el controlador, especificamente en el titulo.
					List<Usuario> usuario = clienteService.UsuariosxDepartamento(id); //inicializamos una lista de tipo departamento y en ella guardamos los registros que cumplan con los parametros establecidos que recibe la funcion GastosxRequisicionxdepartamentoxFecha, la cual se encarga de buscar los registros que contengan estos mismos.
					
					
					model.addAttribute("usuario", usuario); // agregamos al modelo el nombre y el valor del atributo detreq el cual se guarda en la sesión.
					model.addAttribute("titulo", "Reporte: Usuarios del departamento: "+depart.getNombre_depto()+"."); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
					return "reportes/ReporteUsuariosxDepartamento"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta reportes con las vistas dentro de ella con la extensión .html.
				}
}
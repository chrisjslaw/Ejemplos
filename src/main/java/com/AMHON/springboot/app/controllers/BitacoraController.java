package com.AMHON.springboot.app.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.AMHON.springboot.app.models.dao.IBitaDao;
import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller // anotación para representar un controllador
@RequestMapping(value="/bitacora") // la ruta que utilizará para mandar peticiones a este controllador y a sus métodos.
@SessionAttributes("bitacoras") //nombre del o de los atributos a utilizar en los model en donde se guardara en la sessión al pasar los atributos en el model con este nombre quedaran guardados en sesión hasta que dicha sesión se cierre.
public class BitacoraController {

	@Autowired //anotación para inyectar la clase servicio
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos que interactuan con la base de datos.
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	@Autowired // anotación para inyectar la interfaz IBitDao
	private IBitaDao bitaDao; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos que interactuan con la base de datos.
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/listaBitacoras" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String listadoBitacoras(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Tablas de Bitacora"); //agregamos al modelo un titulo
		return "bitacora/listaBitacoras"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraArticulos" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraArticulos(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Articulos"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraArtic()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la función bitaDao.encontrarBitacoraArtic el cual se guarda en la sesión.
		return "bitacora/bitacoraArticulos"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraCategorias" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraCategorias(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Categorías"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraCategoria()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la función encontrarBitacoraCategoria el cual se guarda en la sesión.
		return "bitacora/bitacoraCategorias"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraDepartamentos" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraDepartamentos(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Departamentos"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraDepartamento()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la función encontrarBitacoraDepartamento el cual se guarda en la sesión.
		return "bitacora/bitacoraDepartamentos"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraDetalleEntradas" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraDetalle_Entrada(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Detalle de Entradas"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraDetalle_entradas()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la función encontrarBitacoraDetalle_entradas el cual se guarda en la sesión.
		return "bitacora/bitacoraDetalleEntradas"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraDetalleRequisiciones" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraDetalle_Requisiciones(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Detalle de Requisiciones"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraDetalle_requisiciones()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de encontrarBitacoraDetalle_requisiciones el cual se guarda en la sesión.
		return "bitacora/bitacoraDetalleRequisiciones"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraEntrada" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraEntradas(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Entrada"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraDetalle_entradas()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la función encontrarBitacoraEntradas el cual se guarda en la sesión.
		return "bitacora/bitacoraEntrada"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraIsvs" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraIsvs(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de ISVs"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraISV()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado d e la función encontrarBitacoraISV el cual se guarda en la sesión.
		return "bitacora/bitacoraIsvs"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraKardex" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraKardex(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Kardex"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraKardex()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado la función encontrarBitacoraKardex el cual se guarda en la sesión.
		return "bitacora/bitacoraKardex"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraProveedores" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraProveedores(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Proveedores"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraProveedores()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la funcion encontrarBitacoraProveedores el cual se guarda en la sesión.
		return "bitacora/bitacoraProveedores"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraRequisiciones" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraRequisiciones(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Requisiciones"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraRequisicion()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la funcion encontrarBitacoraRequisicion el cual se guarda en la sesión.
		return "bitacora/bitacoraRequisiciones"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraRoles" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraRoles(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Roles"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraRol()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la funcion encontrarBitacoraRol el cual se guarda en la sesión.
		return "bitacora/bitacoraRoles"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraUnidadesMedida" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraUnidadesMedida(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Unidades Medida"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraUnidadMedida()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la funcion encontrarBitacoraUnidadMedida el cual se guarda en la sesión.
		return "bitacora/bitacoraUnidadesMedida"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/bitacoraUsuarios" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String bitacoraUsuarios(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Bitácora de Unidades Medida"); //agregamos al modelo un titulo
		model.addAttribute("bitacoras", bitaDao.encontrarBitacoraUsuario()); // agregamos al modelo el nombre y el valor del atributo bitacoras pasandole el resultado de la funcion encontrarBitacoraUsuario el cual se guarda en la sesión.
		return "bitacora/bitacoraUsuarios"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta bitacora con las vistas dentro de ella con la extensión .html.
	}
	
	
}

package com.AMHON.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.AMHON.springboot.app.models.entity.Entrada;
import com.AMHON.springboot.app.models.entity.Proveedor;

@Controller
@RequestMapping(value="/ayuda")
@SessionAttributes(value= {"ayuda"})
public class AyudaController {


	
	@RequestMapping(value = { "/indice" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String Ayudaprinc( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Principal-introduccion"; // retornamos el nombre de la vista
	}
	
	
	@RequestMapping(value = { "/Inicio-de-Sesion" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaInicioSesion( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Inicio-de-Sesion"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-admin-Gestion-Departamentos" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaDepartamento( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-admin-Gestion-Departamentos"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-admin-Gestion-Usuarios" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaUsuarios( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-admin-Gestion-Usuarios"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-articulos-Gestion-Categorias" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String Ayudacategoria( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-articulos-Gestion-Categorias"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-articulos-Gestion-Unidades" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaUnidades( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-articulos-Gestion-Unidades"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-articulos-Gestion-Articulos" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaArticulos( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-articulos-Gestion-Articulos"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-articulos-Gestion-Proveedores" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaProv( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-articulos-Gestion-Proveedores"; // retornamos el nombre de la vista
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-articulos-Gestion-ISV" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaISV( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-articulos-Gestion-ISV"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-Inventario-Gestion-Entrada" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaEntrada( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-Inventario-Gestion-Entrada"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-Inventario-Gestion-Kardex" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaKardex( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-Inventario-Gestion-Kardex"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN','ROLE_USER','ROLE_Gerente_Financiero')")
	@RequestMapping(value = { "/Modulo-Requisicion-Gestion-Requisicion" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaRequisicion( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-Requisicion-Gestion-Requisicion"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-Reportes-Uso-Reportes" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaReportes( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-Reportes-Uso-Reportes"; // retornamos el nombre de la vista
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')")
	@RequestMapping(value = { "/Modulo-admin-Generar-Backup" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String AyudaGenerarBackup( Model model, Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		model.addAttribute("titulo", "Ayuda al sistema SGS");
		
		return "ayuda/Modulo-admin-Generar-Backup"; // retornamos el nombre de la vista
	}
}

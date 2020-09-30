package com.AMHON.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.dao.IBitacoraDao;
import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.Audit;
import com.AMHON.springboot.app.models.entity.Departamento;
import com.AMHON.springboot.app.models.entity.DetalleRequisicion;
import com.AMHON.springboot.app.models.entity.Kardex;
import com.AMHON.springboot.app.models.entity.Requisicion;
import com.AMHON.springboot.app.models.entity.Role;
import com.AMHON.springboot.app.models.entity.UnidadMedida;
import com.AMHON.springboot.app.models.entity.Usuario;
import com.AMHON.springboot.app.models.service.IClienteService;
import com.AMHON.springboot.app.util.paginator.PageRender;

@Controller // anotación para representar un controllador
@RequestMapping(value="/usuarios") // la ruta que se pedira
@SessionAttributes("usuario")
public class UsuarioController {

	@Autowired
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; //importamos BCryptPasswordEncoder para utilizarla en el hasheo de las claves antes de guardar el usuario
	
	@Autowired
	IBitacoraDao bitacoraDao; //inyectamos la interfaz de bitacora para utilizar sus métodos
	
	
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')")
	@RequestMapping(value = { "/listaUsuarios" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlara y el tipo de petición en este caso get
	public String listarUsers(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
		
		
	List<Usuario> usuarios = clienteService.encontrarUsuarios();

		logger.info("lo que tiene usuarios: ".concat(usuarios.toString()));
		
		
		model.addAttribute("titulo", "Listado de Usuarios"); //agregamos al modelo un titulo
		model.addAttribute("usuario", usuarios); // agregamos al modelo el atributo usuarios
  
		return "usuarios/listaUsuarios"; // retornamos el nombre de la vista
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')")
	@RequestMapping(value = "/formUsuario") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String crear(Map<String, Object> model) {// recibe el objeto model para poder pasar datos a la vista o bien
													// un map de java util
		List<Departamento> deptos = clienteService.encontrarDepartamentos();
		
		Usuario usuario = new Usuario();
		model.put("deptos", deptos);
		model.put("usuario", usuario);
		model.put("titulo", "Formulario de Creación de Usuario");
		model.put("textoBoton", "Crear Usuario");
		return "usuarios/formUsuario";
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')")
	@RequestMapping(value = {"/formUsuario","/formUsuarioedit"}, method = RequestMethod.POST)
	public String guardar(@Valid Usuario usuario, BindingResult result, Model model,
			@RequestParam(name="rol", required= false) String rol,
	 RedirectAttributes flash, SessionStatus status) {

		//logger.info("hey salio:"+usuario.getId());
		
		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			if(usuario.getId()!= null) {
				List<Departamento> deptos = clienteService.encontrarDepartamentos();
				model.addAttribute("deptos", deptos);
				flash.addFlashAttribute("error", "Los campos no deben ir vacios");
				model.addAttribute("titulo", "Editar Usuario");
				model.addAttribute("textoBoton", "Editar Usuario");
				
				return "redirect:/usuarios/formUsuarioedit/"+usuario.getId(); // si tiene error va redirigir al formulario
			}
			List<Departamento> deptos = clienteService.encontrarDepartamentos();
			model.addAttribute("deptos", deptos);
			model.addAttribute("titulo", "Formulario de Creación de Usuario");
			model.addAttribute("textoBoton", "Crear Usuario");
			return "usuarios/formUsuario"; // si tiene error va redirigir al formulario

		}


		String mensajeflash = (usuario.getId() != null) ? "Usuario editado con éxito!" : "Usuario "+usuario.getUsername()+" creado con éxito!";
			if(usuario.getId()!= null) {
		for( Role role: usuario.getRoles()) {
			logger.info("Role: ".concat(role.getAuthority()));
		role.setAuthority(rol);
		}
				usuario.setPassword(usuario.getPassword());
				logger.info("tu password es:"+usuario.getPassword());
				
				try { //Probamos guardar la entrada mediante un try catch para manejar la exepcion.
					clienteService.save(usuario); //Mediante la interfaz clienteService y el metodo save guardamos la instancia usuario.
					} catch (Exception  e) {// si falla el guardado			
							        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
							        status.setComplete();//cerramos la session para evitar tener transacciones pendientes.
							        flash.addFlashAttribute("error", "El Nombre de usuario ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre de la factura ya esxiste.
							        return "redirect:/usuarios/formUsuario";//redireccionamos de nuevo hacia el formulario.
			        
					}
				status.setComplete(); // elimina el objeto de la sesión.
				flash.addFlashAttribute("success", mensajeflash);
				return "redirect:/usuarios/listaUsuarios";
			}
		
		    Role role= new Role();
			role.setAuthority(rol);
			logger.info("tu rol con get fuera de editar es:"+role.getAuthority());
			usuario.addRole(role);
			String bcryptPassword = passwordEncoder.encode(usuario.getPassword());
			usuario.setPassword(bcryptPassword);
			logger.info("tu password es:"+bcryptPassword);
			
			
			try { //Probamos guardar la entrada mediante un try catch para manejar la exepcion.
				clienteService.save(usuario); //Mediante la interfaz clienteService y el metodo save guardamos la instancia usuario.
				} catch (Exception  e) {// si falla el guardado			
						        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
						        status.setComplete();//cerramos la session para evitar tener transacciones pendientes.
						        flash.addFlashAttribute("error", "El Nombre de usuario o el cargo ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre de la factura ya esxiste.
						        return "redirect:/usuarios/formUsuario";//redireccionamos de nuevo hacia el formulario.
		        
				}
			
		
			status.setComplete(); // elimina el objeto de la sesión.
			flash.addFlashAttribute("success", mensajeflash);
			return "redirect:/usuarios/listaUsuarios";
		
		
	}
	
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')")
	@RequestMapping(value = "/formUsuarioedit/{id}")
	public String editarUser(@PathVariable(value = "id") Long id, Map<String, Object> model,
			@RequestParam(name="rol", required= false) String rol,RedirectAttributes flash) {

		Usuario usuario = null;
		

		if (id > 0) {
			usuario = clienteService.findOneUser(id);
			
			if (usuario == null) {
				flash.addFlashAttribute("error", "El ID del usuario no existe en la Base de Datos!");
				logger.info("no existe hay id:");
				
				return "redirect:/usuarios/listaUsuarios";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del usuario no puede ser cero!");
			logger.info("nope hay id:");
			return "redirect:/usuarios/listaUsuarios";
		}
		
		for( Role role: usuario.getRoles()) {
			logger.info("Role: ".concat(role.getAuthority()));
			rol=role.getAuthority();
		}
		
        List<Departamento> deptos = clienteService.encontrarDepartamentos();
		
		model.put("deptos", deptos);
		
		model.put("usuario", usuario);
		model.put("rol", rol);
		logger.info("bro role:"+rol);
		logger.info("bro:"+usuario.getId());
		model.put("titulo", "Editar Usuario");
		model.put("textoBoton", "Editar Usuario");
		return "usuarios/formUsuarioedit";
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')")
	@RequestMapping(value = "/formUsuarioeditPass/{id}")
	public String editarUserPassword(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {

		Usuario usuario = null;
		

		if (id > 0) {
			usuario = clienteService.findOneUser(id);
			
			if (usuario == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la Base de Datos!");
				logger.info("no existe hay id:");
				
				return "redirect:/usuarios/listaUsuarios";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
			
			return "redirect:/usuarios/listaUsuarios";
		}
		
		
		
		model.put("usuario", usuario);
		model.put("titulo", "Editar Password");
		model.put("textoBoton", "Guardar Contraseña");
		return "usuarios/formUsuarioeditPass";
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')")
	@RequestMapping(value = {"/formUsuarioeditPass"}, method = RequestMethod.POST)
	public String guardarPassword(@Valid Usuario usuario, BindingResult result, Model model,
	 RedirectAttributes flash, SessionStatus status) {

		//logger.info("hey salio:"+usuario.getId());
		
		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Editar Password");
			model.addAttribute("textoBoton", "Guardar Password");
			return "usuarios/formUsuarioeditPass"; // si tiene error va redirigir al formulario

		}


		String mensajeflash = (usuario.getId() != null) ? "Password editado con éxito!" : "Password del usuario: "+usuario.getUsername()+" no se pudo editar!";
			if(usuario.getId()!= null) {
		        String bcryptPassword = passwordEncoder.encode(usuario.getPassword());
		        usuario.setPassword(bcryptPassword);
				clienteService.save(usuario);
				status.setComplete(); // elimina el objeto de la sesión.
				flash.addFlashAttribute("success", mensajeflash);
				return "redirect:/usuarios/listaUsuarios";
			}
		
			flash.addFlashAttribute("success", mensajeflash);
			return "redirect:/usuarios/listaUsuarios";
		
		
	}
	
	
	
	@RequestMapping(value = "/formRecovPass")
	public String recuperarPass( Map<String, Object> model,
			RedirectAttributes flash) {

		
		model.put("titulo", "Recuperar Password");
		model.put("textoBoton", "Validar");
		return "usuarios/formRecovPass";
	}
	
	
	@RequestMapping(value = "/formRecovPass", method = RequestMethod.POST)
	public String verificarRecuvpass( BindingResult result, Model model,
			@RequestParam(name="user[]", required=false) String user,//para obtener las lineas de requisicion
			@RequestParam(name="p1[]", required= false) String p1,//para obtener las lineas de requisicion
			@RequestParam(name="p2[]", required= false) String p2,
			RedirectAttributes flash, SessionStatus status) {
		
		Usuario usuario = clienteService.findByUsername(user);
		
		logger.info("p1:"+usuario.getP1());
		logger.info("p2:"+usuario.getP2());
		
		if (p1.equals(usuario.getP1()) && p2.equals(usuario.getP2())) {
			model.addAttribute("titulo", "Formulario de Recuperación de Password");
			model.addAttribute("textoBoton", "Recuperar contraseña");
			return "usuarios/formUsuarioRecupPass";
		}

		
		flash.addFlashAttribute("error", "Las preguntas de seguridad no coinciden con el nombre de usuario.");
		return "redirect:/usuarios/formRecovPass";
	}
	
	@RequestMapping(value = "/formRecupPass", method = RequestMethod.POST)
	public String recovUserPassword(@Valid Usuario usuario, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

			//logger.info("hey salio:"+usuario.getId());
			
			if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
										// que valide
				model.addAttribute("titulo", "Formulario de Recuperación de Password");
				model.addAttribute("textoBoton", "Recuperar contraseña");
				return "usuarios/formUsuarioRecupPass"; // si tiene error va redirigir al formulario

			}


			String mensajeflash = (usuario.getId() != null) ? "Password editado con éxito!" : "Password del usuario: "+usuario.getUsername()+" no se pudo editar!";
				if(usuario.getId()!= null) {
			        String bcryptPassword = passwordEncoder.encode(usuario.getPassword());
			        usuario.setPassword(bcryptPassword);
					clienteService.save(usuario);
					status.setComplete(); // elimina el objeto de la sesión.
					flash.addFlashAttribute("success", mensajeflash);
					return "login";
				}
			
				flash.addFlashAttribute("error", mensajeflash);
				return "login";
			
			
		}
}

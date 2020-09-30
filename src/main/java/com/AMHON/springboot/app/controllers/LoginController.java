package com.AMHON.springboot.app.controllers;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.Usuario;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller
@SessionAttributes("usuario")
public class LoginController {
	protected final Log logger = LogFactory.getLog(this.getClass());
	@GetMapping(value= "/login")
	public String login(@RequestParam (value="error", required=false) String error,
			@RequestParam (value="logout", required=false) String logout,
			Model model, Principal principal, RedirectAttributes flash, HttpServletRequest request) {
		
		if(principal != null) {
			flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente.");
			return"redirect:/";
		}
	
		if(error != null) {
			model.addAttribute("error", "Error en el login: Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
		}
		
		
		
		if(logout !=null) {
			
			model.addAttribute("success", "Ha cerrado Sesión Exitosamente!");
		}
		
		model.addAttribute("titulo", "Autenticación");
		return "login";
	}
	
	@RequestMapping(value = "/recuparPass")
	public String recuperarPass( Map<String, Object> model,
			RedirectAttributes flash) {

	
		model.put("titulo", "Comprobar cuenta");
		model.put("textoBoton", "Validar");
		return "formRecovPass";
	}
	
	@Autowired
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; //importamos BCryptPasswordEncoder para utilizarla en el hasheo de las claves antes de guardar el usuario
	
	
	@RequestMapping(value = "/formRecovPass", method = RequestMethod.POST)
	public String verificarRecuvpass(  Model model,
			@RequestParam(name="user[]", required=false) String user,//para obtener las lineas de requisicion
			@RequestParam(name="p1[]", required= false) String p1,//para obtener las lineas de requisicion
			@RequestParam(name="p2[]", required= false) String p2,
			RedirectAttributes flash, SessionStatus status) {
		
		Usuario usuario = clienteService.findByUsername(user);
		if (usuario == null) {
			flash.addFlashAttribute("error", "El nombre de usuario: "+user +" no esta registrado!");
			logger.info("no existe hay id:");
			
			return "redirect:recuparPass";
		}
		//logger.info("p1:"+usuario.getP1());
		//logger.info("p2:"+usuario.getP2());
		
		if (p1.equals(usuario.getP1()) && p2.equals(usuario.getP2())) {
			model.addAttribute("titulo", "Formulario de Cambio de Contraseña");
			model.addAttribute("usuario", usuario);
			model.addAttribute("textoBoton", "Cambiar contraseña");
			flash.addFlashAttribute("success", "Las Información es correcta, Por favor introduzca su nueva contraseña");
			return "formUsuarioRecupPass";
		}

		
		flash.addFlashAttribute("error", "Una de las respuestas a las preguntas de seguridad no coinciden con la información del usuario. Por favor verifique que las respuestas estén correctas");
		return "redirect:recuparPass";
	}
	
	
	
	@RequestMapping(value = "/formRecupPass", method = RequestMethod.POST)
	public String recovUserPassword(@Valid Usuario usuario, BindingResult result, Model model,
			RedirectAttributes flash, SessionStatus status) {

			//logger.info("hey salio inicio:"+usuario.getId());
			//logger.info("hey salio inicio:"+usuario.getNombreCompleto());
			
			if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
										// que valide
				//logger.info("hey salio con err:"+usuario.getId());
				//logger.info("hey salio con err:"+usuario.getNombreCompleto());
				model.addAttribute("titulo", "Formulario de Cambio de Contraseña");
				model.addAttribute("textoBoton", "Cambiar contraseña");
				return "formUsuarioRecupPass"; // si tiene error va redirigir al formulario

			}

               
				if(usuario.getId()!= null) {
			        String bcryptPassword = passwordEncoder.encode(usuario.getPassword());
			        usuario.setPassword(bcryptPassword);
			        
					clienteService.save(usuario);
					status.setComplete(); // elimina el objeto de la sesión.
					model.addAttribute("success", "Usuario: "+usuario.getUsername() +"Se cambio la Contraseña correctamente. Por favor Ingrese con su nueva Contraseña");
					model.addAttribute("titulo", "Autenticación");
					return "login";
				}
			
				flash.addFlashAttribute("error", "Ha ocurrido un error, no se pudo cambiar su contraseña");
				return "/";
			
			
		}
}

package com.AMHON.springboot.app.controllers;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.Info;
import com.AMHON.springboot.app.models.service.IClienteService;


@Controller // anotación para representar un controllador
@RequestMapping(value="/info") // la ruta que utilizará para mandar peticiones a este controllador y a sus métodos.
@SessionAttributes("inf") //nombre del o de los atributos a utilizar en los model en donde se guardara en la sessión al pasar los atributos en el model con este nombre quedaran guardados en sesión hasta que dicha sesión se cierre.
public class InfoController {

	@Autowired//anotación para inyectar la clase servicio
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = {"/cambiarInfo"}, method = RequestMethod.POST) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso POST
	public String guardarInfo(@Valid Info info, BindingResult result, Model model,
	 RedirectAttributes flash, SessionStatus status) { //importamos la clase Model para pasar datos a la vista y habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

		
		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Info");  //agregamos al modelo un titulo el cual se utilizara en el html de la pagina
			model.addAttribute("textoBoton", "Guardar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
			return "info/cambiarInfo"; // si tiene error va redirigir al formulario

		}


		String mensajeflash = (info.getId_info() != null) ? "Info editada con éxito!" : "Info "+info.getId_info()+" no existe"; //Verificamos el Id de depto, si viene nulo el mensajeflash sera departamento creado con exito y si no viene nulo el mensajeflash se le setea: departamento editado con exito.
		
		    clienteService.saveInfo(info); //Mediante la interfaz clienteService y el metodo saveIsv guardamos la instancia ISV isv.
			status.setComplete(); // elimina el objeto de la sesión.
			flash.addFlashAttribute("success", mensajeflash); //Pasamos el atributo flash con el mensaje de success, el cual se obtiene de validar si el id_isv es null o no.
			return "redirect:/landing"; // redireccionamos hacia la pagina principal o landing
		
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = "/cambiarInfo") //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso GET
	public String editarISV( Map<String, Object> model,
			RedirectAttributes flash) { //importamos la clase Model para pasar datos a la vista y habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar.
      
		Long id = 1L; // creamos la variable con el id que se buscara, debido a que es un unico registro siempre sera 1L
		Info info = null; // creamos la instancia de Info info
		

		if (id > 0) { // validamos que el ud sea mayor a 0
	info = clienteService.encontrarInfo(id); // si el id es mayor a 0 entonces mediante la interfaz clienteService utilizamos el metodo encontrarInfo pasandole la variable id para obtener el registro correspondiente.
			
			if (info == null) { // validamos que isv sea null
				flash.addFlashAttribute("error", "El ID de Info no existe en la Base de Datos!"); // en caso de ser null agregamos un mensaje de error el cual pasaremos al redireccionar.
				logger.info("no existe hay id:"); // pasamos un mensaje al log
				
				return "redirect:/landing"; // redireccionamos a la pagina principal
			}
		} else { // en caso de que sea 0
			flash.addFlashAttribute("error", "El ID de Info no puede ser cero!"); // agregamos un mensaje indicando que el id no puede ser 0
			return "redirect:/landing"; //redireccionamos a la pagina principal
		}
		
		
		model.put("inf", info); // agregamos al modelo el nombre y el valor del atributo isv el cual se guarda en la sesión
		model.put("titulo", "Editar  Información Sobre AMHON");  // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("textoBoton", "Guardar");  // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "info/cambiarInfo";  //Retornamos a la ruta del controllador cambiarISV que obtiene el post para que este pueda guardar la información.
	}
	
}

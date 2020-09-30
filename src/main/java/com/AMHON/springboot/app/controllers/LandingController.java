package com.AMHON.springboot.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.AMHON.springboot.app.models.entity.Info;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller
@SessionAttributes("inf") //nombre del o de los atributos a utilizar en los model en donde se guardara en la sessión al pasar los atributos en el model con este nombre quedaran guardados en sesión hasta que dicha sesión se cierre.
public class LandingController {
	
	@Autowired//anotación para inyectar la clase servicio
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos
	
	

	@RequestMapping(value= {"/landing","/"})
	public String ver(Model model, HttpServletRequest request) {
		
		Long id = 1L; // creamos la variable con el id que se buscara, debido a que es un unico registro siempre sera 1L

	    Info info = clienteService.encontrarInfo(id); // si el id es mayor a 0 entonces mediante la interfaz clienteService utilizamos el metodo encontrarInfo pasandole la variable id para obtener el registro correspondiente.
			
		model.addAttribute("titulo", "Menu");
		model.addAttribute("inf", info);
		return "landing";
	}
}
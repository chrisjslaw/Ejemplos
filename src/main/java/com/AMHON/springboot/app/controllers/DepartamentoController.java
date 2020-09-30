package com.AMHON.springboot.app.controllers;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AMHON.springboot.app.models.entity.Departamento;
import com.AMHON.springboot.app.models.service.IClienteService;

@Controller // anotación para representar un controllador
@RequestMapping(value="/departamento") // la ruta que utilizará para mandar peticiones a este controllador y a sus métodos.
@SessionAttributes("depto") //nombre del o de los atributos a utilizar en los model en donde se guardara en la sessión al pasar los atributos en el model con este nombre quedaran guardados en sesión hasta que dicha sesión se cierre.
public class DepartamentoController {

	@Autowired //anotación para inyectar la clase servicio
	private IClienteService clienteService; // importando la interfaz siempre tiene que ser el más generico el cual contiene los métodos que interactuan con la base de datos.
	
	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = { "/listaDepartamentos" }, method = RequestMethod.GET) //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso get
	public String listarDepartamentos(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {//importamos la clase Model para pasar datos a la vista y requerimos el parametro page
		
		
			List<Departamento> deptos = clienteService.encontrarDepartamentos(); //Creamos una lista de tipo Departamento para obtener todos los registro de departamentos mediante la interfaz clienteService que llama el método encontrarDepartamentos
				
		model.addAttribute("titulo", "Listado de Departamentos"); //agregamos al modelo un titulo
		model.addAttribute("depto", deptos); // agregamos al modelo el nombre y el valor del atributo depto el cual se guarda en la sesión.
		return "departamento/listaDepartamentos"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta departamento con las vistas dentro de ella con la extensión .html.
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@RequestMapping(value = "/formDepartamento") // esta es la ruta, no se mete el parametro Get ya que este se encarga de
										// mostrar el form
	public String crearDepartamento(Map<String, Object> model) {// Utilizamos la interfaz map para poder pasar datos a la vista o bien
		
		Departamento depto = new Departamento(); //Creamos una nueva instancia de Departamento la cual se pasara al formulario de crear departamento para que le sean seteados los valores correspondientes.
		model.put("depto", depto); // agregamos al modelo el nombre y el valor del atributo depto el cual se guarda en la sesión.
		model.put("titulo", "Formulario de Departamento"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("textoBoton", "Crear Departamento"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "departamento/formDepartamento"; // retornamos el nombre de la vista especificando su ruta la cual se encuentra en src/main/resources dentro de la carpeta templates la cual tendra la carpeta departamento con las vistas dentro de ella con la extensión .html.
	}
	
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@RequestMapping(value = "/formDepartamento", method = RequestMethod.POST) // esta es la ruta que recibe los datos del formDepartamento, tiene asignado que el metodo del request sea post para recibir el post del form Departamento.
	public String guardarDepartamento(@Valid Departamento depto, BindingResult result, Model model, // habilitamos la anotacion @Valid para validar las restricciones de las variables de Departamento y tambien se habilita el BindingResult para manejar los errores que presenten las restricciones, se habilita el model para pasar información a la vista.
			RedirectAttributes flash, SessionStatus status) { //habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar y tambien se habilita el status de la sesión para poder cerrarla una vez que finalicemos el guardado de los datos.

		if (result.hasErrors()) {// en esta parte se agrega el binding a lo que recibe el metodo que procesa para
									// que valide
			model.addAttribute("titulo", "Formulario de Departamento"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
			model.addAttribute("textoBoton", "Guardar Departamento"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
			return "departamento/formDepartamento"; // si tiene error va redirigir al mismo formulario de nuevo

		}

		String mensajeflash = (depto.getId_depto() != null) ? "Departamento editado con éxito!" : "Departamento creado con éxito!";//Verificamos el Id de depto, si viene nulo el mensajeflash sera departamento creado con exito y si no viene nulo el mensajeflash se le setea: departamento editado con exito.
		try {
		clienteService.saveDepartamento(depto); //Mediante la interfaz clienteService y el metodo saveDepartamento guardamos la instancia Departamento depto.
		} catch (Exception  e) {// si falla el guardado			
				        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
				        status.setComplete();//cerramos la session para evitar tener transacciones pendientes.
				        model.addAttribute("titulo", "Formulario de Departamento"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
						model.addAttribute("textoBoton", "Guardar Departamento"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
				        flash.addFlashAttribute("error", "El Nombre del departamento ya existe en la Base de Datos!");// agregamos el mensaje de error que indica que el nombre del departamento ya esxiste.
				        return "departamento/formDepartamento";//redireccionamos de nuevo hacia el formulario.
        
		}
		
		status.setComplete(); // elimina el objeto depto de la sesión.
		flash.addFlashAttribute("success", mensajeflash); //Pasamos el atributo flash con el mensaje de success, el cual se obtiene de validar si el id_depto es null.
		return "redirect:/departamento/listaDepartamentos"; //se redirecciona a la lista de departamentos
	}
	
	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@RequestMapping(value = "/formDepartamento/{id}") // esta es la ruta del controllador que recibe la variable id desde la url y asi poder obtener el registro especifico.
	public String editarDepartamento(@PathVariable(value = "id") Long id, Map<String, Object> model, //Utilizamos la anotación @Path variable para obtener la variable id que vienen dentro del url, se habilita el model para pasar información a la vista y habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar.
			RedirectAttributes flash) {

		Departamento depto = null; //inicializamos una instancia de Departamento e igualamos la referencia a null.
		

		if (id > 0) { //comparamos si la variable id es mayor a 0
	depto = clienteService.encontrarDepartamento(id); // si el id es mayor a 0 entonces mediante la interfaz clienteService utilizamos el metodo encontrarDepartamento pasandole la variable id para obtener el registro correspondiente.
			
			if (depto == null) { //Comparamos si la variable es igual a null
				flash.addFlashAttribute("error", "El ID del departamento no existe en la Base de Datos!"); //En caso de que el depto sea null pasamos el atributo flash con el mensaje de error correspondiente.
				logger.info("no existe hay id:"); //utilizamos el logger para mostrarlo en la consola
				
				return "redirect:/departamento/listaDepartamentos"; //redireccionamos a la lista de departamentos.
			}
		} else { // en caso de que el id no sea mayor a 0 realizamos lo siguiente.
			flash.addFlashAttribute("error", "El ID del articulo no puede ser cero!"); //Pasamos el atributo flash con el mensaje de error correspondiente.
			return "redirect:/departamento/listaDepartamentos";  //redireccionamos a la lista de departamentos.
		}
		
		model.put("depto", depto); // agregamos al modelo el nombre y el valor del atributo depto el cual se guarda en la sesión.
		model.put("titulo", "Editar Departamento"); // agregamos al modelo el nombre y el valor del atributo titulo el cual solamente se pasa para ser utilizado en la vista.
		model.put("textoBoton", "Editar"); // agregamos al modelo el nombre y el valor del atributo textoBoton el cual solamente se pasa para ser utilizado en la vista.
		return "departamento/formDepartamento"; //Retornamos a la ruta del controllador formDepartamento que obtiene el post para que este pueda guardar la información.
	}

	@PreAuthorize("hasRole('ROLE_Super_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador.
	@RequestMapping(value = "/eliminarDepartamento/{id}") // esta es la ruta del controllador que recibe la variable id desde la url, la cua se utilizará para obtener el registro especifico.
	public String eliminarDepartamento(@PathVariable(value = "id") Long id, RedirectAttributes flash) { //Utilizamos la anotación @Path variable para obtener la variable id que vienen dentro del url, habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar.

		if (id > 0) { //comparamos si la variable id es mayor a 0
			Departamento depto = clienteService.encontrarDepartamento(id);//cuando la condicion se cumple se procede a obtener el registro especifico mediante la interfaz clienteService y el metodo encontrar departamento pasandole el parametro id.

			try { //Probamos eliminar el departamento mediante un try catch para manejar la exepcion.
				clienteService.EliminarDepartamento(depto.getId_depto()); // Mediante la interfaz clienteService se procede a llamar el metodo eliminarDepartamento y le pasamos el parametro id para borrar el registro especifico con esa id. 
				
				} catch (Exception  e) {// si falla el borrado			
						        logger.info("exception on update: " + e.getMessage()); //se realiza un log en la consola indicando la exepción.
						       
						        flash.addFlashAttribute("error", "No se pudo borrar el registro porque esta ligado a otros datos");// agregamos el mensaje de error que indica que el registro no puede ser borrado.
						        return "redirect:/departamento/listaDepartamentos";//redireccionamos de nuevo hacia el formulario.
		        
				}
			flash.addFlashAttribute("success", "Departamento: "+depto.getNombre_depto()+ " eliminado con éxito!"); //Pasamos el atributo flash con el mensaje de success correspondiente.

		} 

		return "redirect:/departamento/listaDepartamentos"; //Redireccionamos a la lista de departamentos una vez que ha sido eliminado.
	}
	
}

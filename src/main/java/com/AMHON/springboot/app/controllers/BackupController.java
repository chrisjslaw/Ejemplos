package com.AMHON.springboot.app.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // anotación para representar un controllador
@RequestMapping(value="/backup") // la ruta que utilizará para mandar peticiones a este controllador y a sus métodos.
public class BackupController {

	private final Log logger = LogFactory.getLog(this.getClass()); // codigo para implementar la función de log en la consola
	
	
	@PreAuthorize("hasAnyRole('ROLE_Super_ADMIN','ROLE_ADMIN')") //Anotación para indicar que roles tienen permiso para acceder al controlador
	@RequestMapping(value = "/generarBackup") //anotacion donde se especifica la ruta que se controlará y el tipo de petición en este caso GET
	public String editarISV( Map<String, Object> model,
			RedirectAttributes flash) { //importamos la clase Model para pasar datos a la vista y habilitamos la especializacion del model RedirectAttributes flash para poder mandar mensajes al redireccionar.
      
		try {
			//Runtime.getRuntime().exec("cmd /c C:\\Users\\Sistemas\\Desktop\\sqlbackup.bat");
			
		/*	String s = Paths.get("").toAbsolutePath().toString();
			String path = System.getProperty("user.dir");
			System.out.println("Current relative path is: " + s);
			logger.info("Current relative path asin to string: "+ Paths.get("").toAbsolutePath());
			logger.info("Current relative path is: " + s+"\\bat\\sqlbackup.bat");
			logger.info("Current relative path is: " + path+"\\bat\\sqlbackup.bat");
			//Runtime.getRuntime().exec("cmd /c "+Paths.get("").toAbsolutePath()+"\\bat\\sqlbackup.bat");
			*/
			
		/*	URI pathToBroker = Paths.get("").toAbsolutePath().toUri();   
			Path pathToBroker2 = Paths.get(pathToBroker);
			String brokerCommand = pathToBroker2.toString(); */
			
			Runtime rt = Runtime.getRuntime();
			String commands = ("cmd /c C:\\SgsBackup\\bat\\sqlbackup.bat");
			Process proc = rt.exec(commands);

			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(proc.getErrorStream()));

			// Read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			String s1 = null;
			while ((s1 = stdInput.readLine()) != null) {
			    System.out.println(s1);
			}

			// Read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s1 = stdError.readLine()) != null) {
			    System.out.println(s1);
			}
			flash.addFlashAttribute("success", "Backup Realizado Correctamente!");
			
		} catch (IOException e) {
			logger.info("exception de backup: " + e.getMessage());
			flash.addFlashAttribute("error", "No se pudo generar el Backup de la Base de Datos, por favor contacte al administrador.");// agregamos el mensaje de error que indica que el nombre de la factura ya esxiste.
			return "redirect:/landing"; //redireccionamos a la pagina principal

		}
			
		 //Pasamos el atributo flash con el mensaje de success, el cual se obtiene de validar si el id_isv es null o no.
		return "redirect:/landing"; //redireccionamos a la pagina principal
	}
	
}

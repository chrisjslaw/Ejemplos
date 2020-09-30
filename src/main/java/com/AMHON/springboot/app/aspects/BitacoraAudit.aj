package com.AMHON.springboot.app.aspects;


import java.net.URLDecoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.AMHON.springboot.app.models.dao.IBitacoraDao;
import com.AMHON.springboot.app.models.entity.Audit;


@Aspect //anotación para indicar que es un aspecto
@Component // anotación que permite inyectar e interactuar con los demás beans
public class BitacoraAudit {

	protected final Log logger = LogFactory.getLog(getClass());

	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";


	@Autowired
	IBitacoraDao bitacoraDao; //codigo para inyeccion 
	
	 @PersistenceContext
	    private EntityManager entitymanager;

	@After("within(com.AMHON.springboot.app.controllers..*)") //anotación donde se especifica el punto de corte de nuestro aspecto indicando la accion que se va interceptar
	// ("execution(*
																// com.AMHON.springboot.app.controllers.*controller*.*(..))")
	public void afterReturnWebMethodExecution(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs(); // Codigo para obtener los argumentos que se pasaron por el controllador o el punto de union
		String methodName = joinPoint.getSignature().getName(); // variable donde se guarda el nombre del metodo de la clase
		//Signature method = joinPoint.getSignature(); 
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest(); //codigo para obtener los atributos que pasan por los request http desde el cliente
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // codigo para obtener la instancia de autenticación y sus atributos
		logger.info("Si entro bro!:" + auth.getName());
		if (auth.getName() != null) {//se verifica que el nombre del usuario no sea nulo dentro de la autenticación
			try {
				for (Object o : args) { //se crea un objeto para recorrer la coleccion de argumentos osea por cada objeto o de argumentos
					Audit bi = new Audit(); // se crea una nueva instancia de la clase bitacora
					bi.setUsername(auth.getName()); //guardamos el nombre de usuario que se obtiene de la autenticación dentro de la entidad bitacora. 
					
					//
					  
				        Session session = entitymanager.unwrap(Session.class);
				           String pr =session.getEntityManagerFactory().toString();
				           logger.info("inside if:"+session.getTransaction().getStatus());
					 logger.info("inside AOP {}"+pr);
					logger.info("tu nombre de clase es:" );
					// EntityTransaction tx =
					// .getEntityManagerHolder().getEntityManager().getTransaction();
					logger.info("tu username es:" + auth.getName()); //codigo para mostrar el nombre de usuario en consola bajo logging para efectos de auditoria
					bi.setNomb_class(o.getClass().getCanonicalName()); //obtenemos y seteamos el nombre de la clase que se obtiene del objeto o
					logger.info("tu nombre de clase es:" + o.getClass().getSimpleName()); //codigo para mostrar el nombre de la clase que se ejecuto en el punto de corte en consola bajo logging para efectos de auditoria
					bi.setNomb_metodo(methodName); // 
					logger.info("tu nombre de metodo es:" + methodName);
					bi.setIp(request.getLocalAddr());
					logger.info("Ip:" + request.getLocalAddr());
					bi.setAsp(o.toString());
					logger.info("tu nombre de o es:" + o);
					bitacoraDao.save(bi);
				}
			} catch (Exception e) {

			}

		}

	}
	
}

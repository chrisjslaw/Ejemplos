package com.AMHON.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.AMHON.springboot.app.models.entity.Bitacora;

@Repository
public class BitacoraRegistrosDaoImpl implements IBitaDao{ //implementamos en esta clase la iterfaz IbitaDao

	@PersistenceContext //esta anotación permite que el programador pueda utilizar entityManager de forma automatica sin preocuparse por cerrar las transacciones.
	private EntityManager em; //se encarga de abrir y cerrar las transacciones.
	
	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraArtic() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y articulos 
		Query q = em.createNativeQuery("SELECT * FROM bitacora, articulos_aud where bitacora.id = articulos_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de articulo
		List<Object[]> articulos = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		/*for (Object[] a : authors) {
		    System.out.println("Author "
		            + a[0]
		            + " "
		            + a[1]);
		}*/
		return articulos; //retornamos la lista objeto categorias
	}

	@SuppressWarnings("unchecked")// al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraCategoria() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y categoria 
		Query q = em.createNativeQuery("SELECT * FROM bitacora, categorias_aud where bitacora.id = categorias_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora que esten ligados a la tabla categorias
		List<Object[]> categorias = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return categorias; //retornamos la lista objeto categorias
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraDepartamento() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y departamento
		Query q = em.createNativeQuery("SELECT * FROM bitacora, departamentos_aud where bitacora.id = departamentos_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de departamentos
		List<Object[]> departamentos = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return departamentos; //retornamos la lista objeto departamentos
	}
	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraDetalle_entradas() {//función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y detalle_entrada.
		Query q = em.createNativeQuery("SELECT * FROM bitacora, detalle_entradas_aud where bitacora.id = detalle_entradas_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de detalle_entrada
		List<Object[]> det_entradas = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return det_entradas; //retornamos la lista objeto departamentos
	}
	
	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraDetalle_requisiciones() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y detalle_requisiciones.
		Query q = em.createNativeQuery("SELECT * FROM bitacora, detalle_requisiciones_aud where bitacora.id = detalle_requisiciones_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de detalle_requisiciones
		List<Object[]> detalle_requisiciones = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return detalle_requisiciones; //retornamos la lista objeto detalle_requisiciones
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraEntradas() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y entradas.
		Query q = em.createNativeQuery("SELECT * FROM bitacora, entradas_aud where bitacora.id = entradas_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de entradas
		List<Object[]> entradas = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return entradas; //retornamos la lista objeto entradas.
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraISV() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y isvs.
		Query q = em.createNativeQuery("SELECT * FROM bitacora, isv_aud where bitacora.id = isv_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de isvs
		List<Object[]> isvs = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return isvs; //retornamos la lista objeto isvs.
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraKardex() {//función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y kardex.
		Query q = em.createNativeQuery("SELECT * FROM bitacora, kardex_aud where bitacora.id = kardex_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de kardex
		List<Object[]> kardex = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return kardex; //retornamos la lista objeto kardex.
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraProveedores() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y proveedores.
		Query q = em.createNativeQuery("SELECT * FROM bitacora, proveedores_aud where bitacora.id = proveedores_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de proveedores
		List<Object[]> proveedores = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return proveedores; //retornamos la lista objeto proveedores.
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraRequisicion() {//función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y requisiciones.
		Query q = em.createNativeQuery("SELECT * FROM bitacora, requisiciones_aud where bitacora.id = requisiciones_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de requisiciones
		List<Object[]> requisiciones = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return requisiciones; //retornamos la lista objeto requisiciones.
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraRol() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y Roles.
		Query q = em.createNativeQuery("SELECT bitacora.id, bitacora.user_name, bitacora.date, bitacora.ip, roles_aud.id as id_rol, roles_aud.revtype FROM bitacora, roles_aud where bitacora.id = roles_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de Roles
		List<Object[]> roles = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return roles; //retornamos la lista objeto roles.
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraUnidadMedida() {//función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y Unidades_medida.
		Query q = em.createNativeQuery("SELECT * FROM bitacora, unidades_medida_aud where bitacora.id = unidades_medida_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de Unidades_medida
		List<Object[]> unidades = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return unidades; //retornamos la lista objeto unidades.
	}

	@SuppressWarnings("unchecked") // al pasar el resultado a la lista no se esta verificando entonces genera una advertencia con esta anotación podemos suprimir dicha advertencia ya que no es necesario.
	@Transactional(readOnly=true) // anotación que indica que solo sera de lectura para evitar inyección sql
	@Override //se utiliza para forzar al compilador a comprobar en tiempo de compilación que estás sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución, los cuales serían mucho más difíciles de detectar.
	public List<Object[]> encontrarBitacoraUsuario() { //función que que me retorna una lista de tipo objeto con los resultados de una consulta, en este caso de bitacora y users.
		Query q = em.createNativeQuery("SELECT bitacora.id, bitacora.user_name, bitacora.date, bitacora.ip, users_aud.id as id_user, users_aud.revtype FROM bitacora, users_aud where bitacora.id = users_aud.rev");// Consulta de manera nativa a la base de datos para obtener los registro de bitacora de users
		List<Object[]> usuarios = q.getResultList(); //guardamos el resultado en una lista de tipo objeto
		
		return usuarios; //retornamos la lista objeto usuarios.
	}

}

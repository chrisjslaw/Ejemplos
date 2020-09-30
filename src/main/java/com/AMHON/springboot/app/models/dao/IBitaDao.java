package com.AMHON.springboot.app.models.dao;

import java.util.List;

import com.AMHON.springboot.app.models.entity.Audit;
import com.AMHON.springboot.app.models.entity.Bitacora;

public interface IBitaDao {

	public List<Object[]> encontrarBitacoraArtic(); //Función para obtener los registros de la bitácora de Articulos.
	
	public List<Object[]> encontrarBitacoraCategoria(); //Función para obtener los registros de la bitácora de Categorias
	
	public List<Object[]> encontrarBitacoraDepartamento(); //Función para obtener los registros de la bitácora de Departamentos.
	
	public List<Object[]> encontrarBitacoraDetalle_entradas(); //Función para obtener los registros de la bitácora de Detalle_Entradas
	
	public List<Object[]> encontrarBitacoraDetalle_requisiciones(); //Función para obtener los registros de la bitácora de Detalle_Requisiciones.
	
	public List<Object[]> encontrarBitacoraEntradas(); //Función para obtener los registros de la bitácora de Entradas.

	public List<Object[]> encontrarBitacoraISV(); //Función para obtener los registros de la bitácora de ISVs.
	
	public List<Object[]> encontrarBitacoraKardex(); //Función para obtener los registros de la bitácora de kardex.
	
	public List<Object[]> encontrarBitacoraProveedores(); //Función para obtener los registros de la bitácora de Proveedores.
	
	public List<Object[]> encontrarBitacoraRequisicion(); //Función para obtener los registros de la bitácora de requisiciones.
	
	public List<Object[]> encontrarBitacoraRol(); //Función para obtener los registros de la bitácora de Roles.
	
	public List<Object[]> encontrarBitacoraUnidadMedida(); //Función para obtener los registros de la bitácora de UnidadMedida.
	
	public List<Object[]> encontrarBitacoraUsuario(); //Función para obtener los registros de la bitácora de Usuarios.
	
}

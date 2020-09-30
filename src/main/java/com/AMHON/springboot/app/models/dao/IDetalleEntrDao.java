package com.AMHON.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.DetalleRequisicion;

public interface IDetalleEntrDao extends CrudRepository<DetalleEntrada, Long>{	 
	/* @Query("select d from DetalleEntrada d where d.articulo = ?1") */
	@Query("FROM DetalleEntrada d where d.articulo.id_artic = :articulo")
	public List<DetalleEntrada> findByArticulo(@Param("articulo") Long articulo);
	
	
	@Query("FROM DetalleEntrada d where d.entrada.id_entrada = :entrada")
	public List<DetalleEntrada> findByIdEntrada(@Param("entrada") Long entrada);
	
	@Query("SELECT d, count(e) FROM DetalleEntrada d JOIN d.entrada e where d.entrada.id_entrada = e.id_entrada and e.proveedor.id_prov = :proveedor and e.fecha between :fechaIn and :fechaFin GROUP BY d")
	public List<DetalleEntrada> findByProductxProveedor(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin, @Param("proveedor") Long proveedor);
	//select d from detalle_entradas d, entradas e where e.id_entrada = d.id_entrada and e.id_prov ='4'; sintaxis base para producto por proveedor
	
	
	@Query("FROM DetalleEntrada d where d.articulo.id_artic = :articulo and d.entrada.fecha between :fechaIn and :fechaFin")
	public List<DetalleEntrada> findByEntradasxProductxFecha(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin, @Param("articulo") Long articulo);	
	//select *, sum(detalle_entradas.cantidad) as total from detalle_entradas group by detalle_entradas.id_artic order by detalle_entradas.id_det_entr  base para entradas por producto
	//@Query("SELECT d, sum(d.cantidad) as total FROM DetalleEntrada d JOIN d.entrada e where d.entrada.id_entrada = e.id_entrada and d.articulo.id_artic = :articulo and e.fecha between :fechaIn and :fechaFin GROUP BY d.articulo.id_artic")

	@Query("SELECT new map( e.proveedor.nombre_prov as prov, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total)"
			+ " FROM DetalleEntrada d JOIN d.entrada e where d.entrada.id_entrada = e.id_entrada and "
			+ "e.fecha between :fechaIn and :fechaFin GROUP BY d.entrada.proveedor")
	public List<DetalleEntrada> GastosxEntradaxproveedorxFecha(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
	
}

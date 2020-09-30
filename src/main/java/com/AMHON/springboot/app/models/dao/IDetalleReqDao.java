package com.AMHON.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.AMHON.springboot.app.models.entity.DetalleEntrada;
import com.AMHON.springboot.app.models.entity.DetalleRequisicion;

public interface IDetalleReqDao extends CrudRepository<DetalleRequisicion, Long>{

	@Query("FROM DetalleRequisicion d where d.articulo.id_artic = :articulo")
	public List<DetalleRequisicion> findByArticuloReq(@Param("articulo") Long articulo);

	@Query("FROM DetalleRequisicion d where d.requisicion.id_requi = :requisicion")
	public List<DetalleRequisicion> findByIdReq(@Param("requisicion") Long requisicion);
	
	@Query("SELECT d, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total"
			+ " FROM DetalleRequisicion d JOIN d.requisicion r where d.requisicion.id_requi = r.id_requi and d.requisicion.estado = 'Entregado' and "
			+ "r.fecha between :fechaIn and :fechaFin GROUP BY d.articulo.id_artic, d.requisicion.usuario.id, d.id_det_req")
	public List<DetalleRequisicion> findByRequisicionesxArticulosxUsuarioxFecha(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);	
	// para sacar la cantidad del articulo que se le dio al usuario 
	
	
	@Query("SELECT d, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total"
			+ " FROM DetalleRequisicion d JOIN d.requisicion r where d.requisicion.id_requi = r.id_requi and d.requisicion.estado = 'Entregado' and "
			+ "r.fecha between :fechaIn and :fechaFin GROUP BY d.requisicion.departamento")
	public List<DetalleRequisicion> GastosxRequisicionxdepartamentoxFecha(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
	
	@Query("SELECT new map( r.departamento as depart, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total)"
			+ " FROM DetalleRequisicion d JOIN d.requisicion r where d.requisicion.id_requi = r.id_requi and d.requisicion.estado = 'Entregado' and "
			+ "r.fecha between :fechaIn and :fechaFin GROUP BY d.requisicion.departamento")
	public List<DetalleRequisicion> GastosxRequisicionxdepartamentoxFechamodifc(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
	
	@Query("SELECT d, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total"
			+ " FROM DetalleRequisicion d JOIN d.requisicion r where d.requisicion.id_requi = r.id_requi and d.requisicion.usuario.id= :usuario and d.requisicion.estado = 'Entregado' and "
			+ "r.fecha between :fechaIn and :fechaFin GROUP BY d.requisicion.id_requi, d.articulo.id_artic")
	public List<DetalleRequisicion> findByRequisicionesxUsuarioxFecha(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin, @Param("usuario") Long usuario);	
	
	
	
	@Query("SELECT d, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total"
			+ " FROM DetalleRequisicion d JOIN d.requisicion r where d.requisicion.id_requi = r.id_requi and d.requisicion.departamento= :depto and d.requisicion.estado = 'Entregado' and "
			+ "r.fecha between :fechaIn and :fechaFin GROUP BY d.requisicion.id_requi, d.articulo.id_artic")
	public List<DetalleRequisicion> findByDepartamentoEspecificoxFecha(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin, @Param("depto") String depto);	
	
}


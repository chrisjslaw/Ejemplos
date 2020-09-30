package com.AMHON.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.AMHON.springboot.app.models.entity.Entrada;
import com.AMHON.springboot.app.models.entity.Requisicion;

public interface IRequisicionDao extends PagingAndSortingRepository<Requisicion, Long>{

	@Query("select q from Requisicion q where q.estado = ?1 ")
	public List<Requisicion> findByEstado(String term);
	
	@Query("select q from Requisicion q where datediff(curdate(),q.fecha)<=92 and q.usuario.id = :usuario")
	public List<Requisicion> findByFechaTresMeses(@Param("usuario") Long usuario);     //consulta para obtener los registros del usuario especifico en un rango de 3 meses

	@Query("FROM Requisicion d where d.fecha between :fechaIn and :fechaFin")
	public List<Requisicion> findByFechasRequisicion(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
	@Query("SELECT d, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total FROM Requisicion r, DetalleRequisicion d where r.fecha between :fechaIn and :fechaFin and r.estado ='Entregado' and r.id_requi= d.requisicion.id_requi group by d.articulo.id_artic")
	public List<Requisicion> findByFechasRequisicionesEntregadas(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
	@Query("SELECT new map( d.articulo.nombre as artic, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total) FROM Requisicion r, DetalleRequisicion d where r.fecha between :fechaIn and :fechaFin and r.estado ='Entregado' and r.id_requi= d.requisicion.id_requi group by d.articulo.id_artic")
	public List<Requisicion> findByFechasRequisicionesEntregadasGrafic(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);

}

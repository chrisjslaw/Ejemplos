package com.AMHON.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.AMHON.springboot.app.models.entity.Entrada;


public interface IEntradaDao extends PagingAndSortingRepository<Entrada, Long>{

//	@Query("FROM Entrada d where d.fecha between :fechaIn and :fechaFin group by d.fecha")
//	public List<Entrada> findByFechasEntrada(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
//	
	@Query("FROM Entrada d where d.fecha between :fechaIn and :fechaFin")
	public List<Entrada> findByFechasEntrada(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
	@Query("SELECT d, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total FROM Entrada e, DetalleEntrada d where e.fecha between :fechaIn and :fechaFin and e.id_entrada= d.entrada.id_entrada group by d.articulo.id_artic")
	public List<Entrada> findByCantidadxArticulosEntrantexfechas(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
	@Query("SELECT new map( d.articulo.nombre as artic, sum(d.cantidad) as total_articulos, sum(d.cantidad*d.precio) as costo_total) FROM Entrada e, DetalleEntrada d where e.fecha between :fechaIn and :fechaFin and e.id_entrada= d.entrada.id_entrada group by d.articulo.id_artic")
	public List<Entrada> findByCantidadxArticulosEntrantexfechasGrafic(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
}

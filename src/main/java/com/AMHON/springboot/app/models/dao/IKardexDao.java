package com.AMHON.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.AMHON.springboot.app.models.entity.Kardex;


public interface IKardexDao extends PagingAndSortingRepository<Kardex, Long>{

	@Query("FROM Kardex d where d.articulo.id_artic = :articulo")
	public List<Kardex> findByArticulo(@Param("articulo") Long articulo);
	
	@Query("FROM Kardex d where d.det_mov.id_det_entr = :detmov")
	public Kardex findByDetalleMov(@Param("detmov") Long detmov);
	
	@Query("FROM Kardex d where d.det_req.id_det_req = :detreq")
	public Kardex findByDetalleReq(@Param("detreq") Long detreq);
	
	@Transactional
	@Modifying
	@Query("Delete FROM Kardex d where d.det_req.id_det_req = :detreq")
	void deleteByDetalleReq(@Param("detreq") Long detreq);
	
	@Query("FROM Kardex d where d.fecha between :fechaIn and :fechaFin")
	public List<Kardex> findByFechasKardex(@Param("fechaIn") Date fechaIn, @Param("fechaFin") Date fechaFin);
	
	
}

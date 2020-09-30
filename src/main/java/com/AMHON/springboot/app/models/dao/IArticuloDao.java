package com.AMHON.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.AMHON.springboot.app.models.entity.Articulo;

public interface IArticuloDao extends CrudRepository<Articulo, Long>{

	@Query("select a from Articulo a where a.nombre like %?1%")
	public List<Articulo> findByNombreArt(String term);
	public List<Articulo> findByNombreContainingIgnoreCase (String term);
	
	@Query("select a from Articulo a where a.nombre like %?1% and a.existencia > 0")
	public List<Articulo> findByNombreArtExist(String term);
	
	
}

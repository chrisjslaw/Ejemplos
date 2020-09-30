package com.AMHON.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReaderFactory;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.AMHON.springboot.app.models.entity.Articulo;
import com.AMHON.springboot.app.models.entity.Audit;

public interface IBitacoraDao extends PagingAndSortingRepository<Audit, Long>{

	
	
}

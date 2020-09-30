package com.AMHON.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.AMHON.springboot.app.models.entity.Departamento;


public interface IDepartamentoDao extends PagingAndSortingRepository<Departamento, Long>{

}

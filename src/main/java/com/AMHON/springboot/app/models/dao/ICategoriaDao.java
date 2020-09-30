package com.AMHON.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.AMHON.springboot.app.models.entity.Categoria;

public interface ICategoriaDao extends PagingAndSortingRepository<Categoria, Long>{

}

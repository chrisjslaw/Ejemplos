package com.AMHON.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.AMHON.springboot.app.models.entity.Proveedor;

public interface IProveedorDao extends PagingAndSortingRepository<Proveedor, Long>{

}

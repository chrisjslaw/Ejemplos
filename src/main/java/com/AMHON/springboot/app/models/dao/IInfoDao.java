package com.AMHON.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.AMHON.springboot.app.models.entity.Info;

public interface IInfoDao extends PagingAndSortingRepository<Info, Long>{

}

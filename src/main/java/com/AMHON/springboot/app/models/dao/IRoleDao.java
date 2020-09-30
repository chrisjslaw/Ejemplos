package com.AMHON.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.AMHON.springboot.app.models.entity.Role;
import com.AMHON.springboot.app.models.entity.Usuario;

public interface IRoleDao extends CrudRepository<Role, Long> {
	
	
}

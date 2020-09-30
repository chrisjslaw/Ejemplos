package com.AMHON.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.AMHON.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends PagingAndSortingRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username);
	
	//public Usuario findByrolesu_Id(Long id); 
	
	//@Query("Select authorithy FROM Usuario AS us LEFT JOIN Role AS on Role.user_id= Usuario.id WHERE Usuario.id = %?1%")
	//public List<Usuario> FindAllWithIDQuery(Long id);
	
	@Query("FROM Usuario u where u.depto.id_depto = :depto")
	public List<Usuario> UsuariosxDepartamento(@Param("depto") Long depto);
}

package com.AMHON.springboot.app.models.entity;


import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.hibernate.envers.Audited;


@Entity
@Audited
@Table(name="Unidades_medida")
public class UnidadMedida implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_unidad;

	@NotEmpty //validadores para que no sean vacios
	@Column(length = 40, unique=true)
	private String nombre_unid;
	
	/*@NotEmpty
	@Column(length = 80)
	private String valor;*/
		
	@Column(length = 200)
	private String descripcion;
	

	public Long getId_unidad() {
		return id_unidad;
	}

	public void setId_unidad(Long id_unidad) {
		this.id_unidad = id_unidad;
	}

	public String getNombre_unid() {
		return nombre_unid;
	}

	public void setNombre_unid(String nombre_unid) {
		this.nombre_unid = nombre_unid;
	}



	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	

}

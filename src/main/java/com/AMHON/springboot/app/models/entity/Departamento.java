package com.AMHON.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name="departamentos")
public class Departamento implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_depto;
	
	@NotNull
	@Column(unique = true)
	private String nombre_depto;
	
	@Column(length=150)
	private String descripcion;
	
	@OneToMany(mappedBy="depto" ,fetch=FetchType.LAZY, cascade=CascadeType.REFRESH)//carga solo la factura de ese cliente hasta que se invoca el get, cascade es para que tenga persistencia los datos de esta clase ej: cuando se guardan muchas facturas y mappedBy crea la relacion en la tabla factura agregando la id de cliente automaticamente
	private List<Usuario> usuarios;

	public Departamento() {
		this.usuarios = new ArrayList<Usuario>();
	}
	
	public Long getId_depto() {
		return id_depto;
	}

	public void setId_depto(Long id_depto) {
		this.id_depto = id_depto;
	}

	public String getNombre_depto() {
		return nombre_depto;
	}

	public void setNombre_depto(String nombre_depto) {
		this.nombre_depto = nombre_depto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	
	
}

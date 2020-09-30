package com.AMHON.springboot.app.models.entity;

import java.io.Serializable;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Audited
@Table(name="Proveedores")
public class Proveedor implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_prov;

	@NotEmpty //validadores para que no sean vacios
	@Column(unique = true)
	private String nombre_prov;
	
	@NotEmpty
	@Column(length = 80)
	private String tel_prov;
	
	@NotEmpty
	@Column(length = 100)
	private String direccion;
	
	@NotEmpty
	@Email
	@Column(length = 80)
	private String email;
	
	@Column(length = 200)
	private String observacion;
	
	
	@OneToMany(mappedBy="proveedor" ,fetch=FetchType.LAZY)//carga solo la factura de ese cliente hasta que se invoca el get, cascade es para que tenga persistencia los datos de esta clase ej: cuando se guardan muchas facturas y mappedBy crea la relacion en la tabla factura agregando la id de cliente automaticamente
	private List<Entrada> Entradas;

	public Long getId_prov() {
		return id_prov;
	}

	public void setId_prov(Long id_prov) {
		this.id_prov = id_prov;
	}

	public String getNombre_prov() {
		return nombre_prov;
	}

	public void setNombre_prov(String nombre_prov) {
		this.nombre_prov = nombre_prov;
	}

	public List<Entrada> getEntradas() {
		return Entradas;
	}

	public void setEntradas(List<Entrada> entradas) {
		Entradas = entradas;
	}

	public String getTel_prov() {
		return tel_prov;
	}

	public void setTel_prov(String tel_prov) {
		this.tel_prov = tel_prov;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	
	
	
}

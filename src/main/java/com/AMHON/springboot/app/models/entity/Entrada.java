package com.AMHON.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;



@Entity
@Audited
@Table(name="Entradas")
public class Entrada implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_entrada;
	
  
	@Column(length = 30)
	private String responsbl;
	
  
	@NotEmpty
	@Column(length=30, unique=true)
	private String num_factura;
    
   
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	@PrePersist
	public void prePersist() {
		fecha = new Date();
	}

	//@JsonManagedReference
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_prov")
	private Proveedor proveedor;
	
	@JsonBackReference
	//@JsonManagedReference
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="id_entrada")//como es una relacion unidireccional se especifica con joinColumn 
	private List<DetalleEntrada> lineasM;
	
	public Entrada() {
		this.lineasM = new ArrayList<DetalleEntrada>();
	}

	public Long getId_entrada() {
		return id_entrada;
	}

	public void setId_entrada(Long id_mov) {
		this.id_entrada = id_mov;
	}


	public String getResponsbl() {
		return responsbl;
	}

	public void setResponsbl(String responsbl) {
		this.responsbl = responsbl;
	}


	public String getNum_factura() {
		return num_factura;
	}

	public void setNum_factura(String num_factura) {
		this.num_factura = num_factura;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public List<DetalleEntrada> getLineasM() {
		return lineasM;
	}

	public void setLineasM(List<DetalleEntrada> lineasM) {
		this.lineasM = lineasM;
	}
	
	public void addItemMovimiento(DetalleEntrada detmov) {
		this.lineasM.add(detmov);
	}
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getTotal() {
		Double total = 0.0;
		
		int size = lineasM.size();
		
		for(int i=0; i< size; i++) {
			total+= lineasM.get(i).calcularImporte();
		}
		return total;
	}
	
	
}

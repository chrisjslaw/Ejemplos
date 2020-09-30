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

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class ,
        property = "id_requi")
@Table(name="Requisiciones")
public class Requisicion implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_requi;
	
	/*
	 * @Column(length = 30) private String responsbl;
	 */
	
	@Column(length = 30)
	private String solicitante;
	
	@Column(length=60)
	private String cargo;
	
	private String departamento;
	
	@Column(length = 250)
	private String motivo;
	
	@Column(length=15)
	private String estado;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	@Column(length = 30)
	private String entregado_por;
	
	@Temporal(TemporalType.DATE)
	private Date fecha_aprob;
	
	
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY )
	private Usuario usuario;
	
	
	@PrePersist
	public void prePersist() {
		fecha = new Date();
	}
	
	
	
	@JsonBackReference
	  @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval =true)
	  @JoinColumn(name="id_requi")//como es una relacion unidireccional se especifica con joinColumn 
	  private List<DetalleRequisicion> lineasRe;
	 
	
	
	  public Requisicion() {
		  this.lineasRe = new ArrayList<DetalleRequisicion>(); 
		  }



	public Long getId_requi() {
		return id_requi;
	}



	public void setId_requi(Long id_requi) {
		this.id_requi = id_requi;
	}



	/*
	 * public String getResponsbl() { return responsbl; }
	 * 
	 * 
	 * 
	 * public void setResponsbl(String responsbl) { this.responsbl = responsbl; }
	 */



	public String getSolicitante() {
		return solicitante;
	}



	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getCargo() {
		return cargo;
	}



	public void setCargo(String cargo) {
		this.cargo = cargo;
	}



	public String getDepartamento() {
		return departamento;
	}



	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public Date getFecha() {
		return fecha;
	}



	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}



	public String getMotivo() {
		return motivo;
	}



	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha_aprob() {
		return fecha_aprob;
	}



	public void setFecha_aprob(Date fecha_aprob) {
		this.fecha_aprob = fecha_aprob;
	}



	public List<DetalleRequisicion> getLineasRe() {
		return lineasRe;
	}



	public Usuario getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}



	public String getEntregado_por() {
		return entregado_por;
	}



	public void setEntregado_por(String entregado_por) {
		this.entregado_por = entregado_por;
	}



	public void setLineasRe(List<DetalleRequisicion> lineasRe) {
		this.lineasRe = lineasRe;
	}
	
	public void addItemRequisicion(DetalleRequisicion detreq) {
		this.lineasRe.add(detreq);
	}
	 

	public Double getTotal() {
		Double total = 0.0;
		
		int size = lineasRe.size();
		
		for(int i=0; i< size; i++) {
			total+= lineasRe.get(i).calcularImportePrecProm();
		}
		return total;
	}
	
}

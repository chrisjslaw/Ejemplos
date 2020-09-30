package com.AMHON.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;


@Entity
@Audited
@Table(name="kardex")
public class Kardex implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_kardex;
	
	@NotNull
	private Integer cantidad;
	
	@NumberFormat(style = Style.CURRENCY)
	@NotNull
	private double precio;
	
	@NotNull
	@Column(length=15)
	private String tipo_mov;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date fecha;
	
	
	@ManyToOne(fetch=FetchType.LAZY )
	@JoinColumn(name="id_artic")
	private Articulo articulo;
	
	
	@ManyToOne(fetch=FetchType.LAZY , cascade = CascadeType.ALL)
	@JoinColumn(name="id_det_mov")
	private DetalleEntrada det_mov;
	
	@ManyToOne(fetch=FetchType.LAZY , cascade = CascadeType.ALL)
	@JoinColumn(name="id_det_req")
	private DetalleRequisicion det_req;

	@PrePersist
	public void prePersist() {
		fecha = new Date();
	}
    

	public Long getId_kardex() {
		return id_kardex;
	}

	public void setId_kardex(Long id_kardex) {
		this.id_kardex = id_kardex;
	}



	public Integer getCantidad() {
		return cantidad;
	}


	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}


	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}


	public String getTipo_mov() {
		return tipo_mov;
	}


	public void setTipo_mov(String tipo_mov) {
		this.tipo_mov = tipo_mov;
	}


	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public Articulo getArticulo() {
		return articulo;
	}


	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}


	public DetalleEntrada getDet_mov() {
		return det_mov;
	}


	public void setDet_mov(DetalleEntrada det_mov) {
		this.det_mov = det_mov;
	}


	public DetalleRequisicion getDet_req() {
		return det_req;
	}


	public void setDet_req(DetalleRequisicion det_req) {
		this.det_req = det_req;
	}
		
	

}

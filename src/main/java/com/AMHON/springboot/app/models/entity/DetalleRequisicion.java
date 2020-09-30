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
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Audited
@Table(name="Detalle_requisiciones")
public class DetalleRequisicion implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_det_req;
	
	
	@NotNull
	@Column(length=15)
	private String tipo_mov;
	
	private Integer cantidad;
	
	@NumberFormat(pattern = "#,###,###,###.##")
	@NotNull
	private Double precio;
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_artic")
	private Articulo articulo;
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_requi")
	private Requisicion requisicion;
	
	@JsonBackReference
	@OneToMany(mappedBy="det_req" ,fetch=FetchType.LAZY,  cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},  orphanRemoval = true)//carga solo la factura de ese cliente hasta que se invoca el get, cascade es para que tenga persistencia los datos de esta clase ej: cuando se guardan muchas facturas y mappedBy crea la relacion en la tabla factura agregando la id de cliente automaticamente
	private List<Kardex> kardexreq;
	
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@PrePersist
	public void prePersist() {
		fecha = new Date();
	}
	
	public DetalleRequisicion() {
		this.kardexreq = new ArrayList<Kardex>();
	}

	public Long getId_det_req() {
		return id_det_req;
	}

	public void setId_det_req(Long id_det_req) {
		this.id_det_req = id_det_req;
	}

	public String getTipo_mov() {
		return tipo_mov;
	}

	public void setTipo_mov(String tipo_mov) {
		this.tipo_mov = tipo_mov;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}


	public List<Kardex> getKardexreq() {
		return kardexreq;
	}

	public void setKardexreq(List<Kardex> kardexreq) {
		this.kardexreq = kardexreq;
	}

	public Requisicion getRequisicion() {
		return requisicion;
	}

	public void setRequisicion(Requisicion requisicion) {
		this.requisicion = requisicion;
	}

	public Double calcularImportePrecProm() {
		return cantidad.doubleValue()*precio;
	}


}

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
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Audited
@Table(name="Detalle_Entradas")
public class DetalleEntrada implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_det_entr;
	
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
	@JoinColumn(name="id_entrada")
	private Entrada entrada;
	
	@JsonManagedReference
	@OneToMany(mappedBy="det_mov" ,fetch=FetchType.LAZY, cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},  orphanRemoval = true)//carga solo la factura de ese cliente hasta que se invoca el get, cascade es para que tenga persistencia los datos de esta clase ej: cuando se guardan muchas facturas y mappedBy crea la relacion en la tabla factura agregando la id de cliente automaticamente
	private List<Kardex> kardexent;
	
	public DetalleEntrada() {
		this.kardexent = new ArrayList<Kardex>();
	}

	public Long getId_det_entr() {
		return id_det_entr;
	}

	public void setId_det_entr(Long id_det_mov) {
		this.id_det_entr = id_det_mov;
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

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public List<Kardex> getKardexent() {
		return kardexent;
	}

	public void setKardexent(List<Kardex> kardexent) {
		this.kardexent = kardexent;
	}

	public Double calcularImporte() {
		return cantidad.doubleValue()*precio;
	}

	
}

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.NumberFormat;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity //Anotación para indicar que es una clase entidad jpa(Java persistence API).
@Audited // Con esta anotación especificamos que la clase entidad va ser auditada.
@Table(name="Articulos") //La anotación table sirve para especificar el nombre que le daremos en la base de datos.
public class Articulo implements Serializable{ //creamos la clase publica ariculo implementando la interfaz serializable 

	private static final long serialVersionUID = 1L; //indica el identificador unico al serializarla
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_artic;
	
	@NotNull //anotación que indica que este campo no puede ser nulo
	@Column(unique=true)
	private String nombre;
	
	@NumberFormat(pattern = "#,###,###,###.##")
    private double precio_prom;
	
	
	@NumberFormat(pattern = "#,###,###,###.##")
	private double ultimo_precio;
	 
	
	@NotNull
	@Column(length=25)
	private String cod_barra;
	
	@Column(length=150)
	private String observacion;
	
	@NotNull
	private Integer stock_min;
	
	@NotNull
	private Integer req_max;
	
	@Column(length=25)
	private Integer existencia=0;
	
	@Column(length=25)
	private Integer tot_salidas=0;
	
	@NumberFormat(pattern = "#,###,###,###.##")
	private double costo_total;
	
	@NumberFormat(pattern = "#,###,###,###.##")
	private double costo_totsalida;
	
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_unidad")
	private UnidadMedida unidadMedida;
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_categ")
	private Categoria categoria;
	
	@OneToMany(mappedBy="articulo" ,fetch=FetchType.LAZY)//carga solo la factura de ese cliente hasta que se invoca el get, cascade es para que tenga persistencia los datos de esta clase ej: cuando se guardan muchas facturas y mappedBy crea la relacion en la tabla factura agregando la id de cliente automaticamente
	private List<Kardex> kardexart;
	
	public Long getId_artic() {
		return id_artic;
	}

	public void setId_artic(Long id_artic) {
		this.id_artic = id_artic;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getStock_min() {
		return stock_min;
	}

	public void setStock_min(Integer stock_min) {
		this.stock_min = stock_min;
	}

	public Integer getReq_max() {
		return req_max;
	}

	public void setReq_max(Integer req_max) {
		this.req_max = req_max;
	}


	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getCod_barra() {
		return cod_barra;
	}

	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}

	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public double getPrecio_prom() {
		return precio_prom;
	}

	public void setPrecio_prom(double precio_prom) {
		this.precio_prom = precio_prom;
	}

	public double getUltimo_precio() {
		return ultimo_precio;
	}

	public void setUltimo_precio(double ultimo_precio) {
		this.ultimo_precio = ultimo_precio;
	}

	public Integer getExistencia() {
		return existencia;
	}

	public Integer getTot_salidas() {
		return tot_salidas;
	}

	public void setTot_salidas(Integer tot_salidas) {
		this.tot_salidas = tot_salidas;
	}

	public double getCosto_totsalida() {
		return costo_totsalida;
	}

	public void setCosto_totsalida(double costo_totsalida) {
		this.costo_totsalida = costo_totsalida;
	}


	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}

	public double getCosto_total() {
		return costo_total;
	}
	

	public void setCosto_total(double costo_total) {
		this.costo_total = costo_total;
	}	
	
	
	
}

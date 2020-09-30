package com.AMHON.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Bitacoras")
public class Audit implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	@NotEmpty
	@Column(length = 30)
	private String username;
	
	@NotEmpty
	private String nomb_class;
	
	@NotEmpty
	private String nomb_metodo;
	
	@NotEmpty
	private String ip;
	
	
	@NotEmpty
	@Column(length = 700)
	private String asp;

	
	@NotNull
	@Column(name="fecha_audit")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy hh:mm:ss")
	private Date fechaAudit;
	
	@PrePersist
	public void prePersist() {
		fechaAudit = new Date();
	}

	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}



	public String getNomb_class() {
		return nomb_class;
	}



	public void setNomb_class(String nomb_class) {
		this.nomb_class = nomb_class;
	}



	public String getNomb_metodo() {
		return nomb_metodo;
	}



	public void setNomb_metodo(String nomb_metodo) {
		this.nomb_metodo = nomb_metodo;
	}



	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
     
	

	public String getAsp() {
		return asp;
	}



	public void setAsp(String asp) {
		this.asp = asp;
	}



	public Date getFechaAudit() {
		return fechaAudit;
	}

	public void setFechaAudit(Date fechaAudit) {
		this.fechaAudit = fechaAudit;
	}
	
	
	




	private static final long serialVersionUID = 1L;
	
}

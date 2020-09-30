package com.AMHON.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;


@Entity
@Audited
@Table(name="Roles", uniqueConstraints = {@UniqueConstraint(columnNames= {"user_id","authority"})})
public class Role implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String authority;
	
	@ManyToOne(fetch = FetchType.LAZY) //codigo para referenciar la relación 
	@JoinColumn(name = "user_id") // campo con el que se da la relación
	private Usuario usuario;
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAuthority() {
		return authority;
	}


	public void setAuthority(String authority) {
		this.authority = authority;
	}


	private static final long serialVersionUID = 1L;

	
}

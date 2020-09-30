package com.AMHON.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Info implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_info;
	
	@Column(length = 512, columnDefinition = "text")
	private String mision;
	
	@Column(length = 512, columnDefinition = "text")
	private String vision;
	
	private String valores;
	
	@Column(length = 650, columnDefinition = "text")
	private String l1;
	
	@Column(length = 650, columnDefinition = "text")
	private String l2;
	
	@Column(length = 650, columnDefinition = "text")
	private String l3;
	
	public Long getId_info() {
		return id_info;
	}
	public void setId_info(Long id_info) {
		this.id_info = id_info;
	}
	public String getMision() {
		return mision;
	}
	public void setMision(String mision) {
		this.mision = mision;
	}
	public String getVision() {
		return vision;
	}
	public void setVision(String vision) {
		this.vision = vision;
	}
	public String getValores() {
		return valores;
	}
	public void setValores(String valores) {
		this.valores = valores;
	}
	public String getL1() {
		return l1;
	}
	public void setL1(String l1) {
		this.l1 = l1;
	}
	public String getL2() {
		return l2;
	}
	public void setL2(String l2) {
		this.l2 = l2;
	}
	public String getL3() {
		return l3;
	}
	public void setL3(String l3) {
		this.l3 = l3;
	}
	
	

}

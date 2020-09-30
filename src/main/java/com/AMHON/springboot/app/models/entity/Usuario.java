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

@Entity
@Audited
@Table(name = "Users")
public class Usuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(length = 30, unique = true)
	private String username;

	@NotEmpty
	@Column(length = 40)
	private String cargo;

	@ValidPassword 
	@Column(length = 60)
	private String password;
	
	@NotEmpty
	@Column(name ="nombr_compl",length = 60)
	private String nombreCompleto;
	
	@NotEmpty
	@Column(length = 40)
	private String p1;
	
	@NotEmpty
	@Column(length = 40)
	private String p2;
	
	private boolean enabled;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Role> rolesu;
	
	@OneToMany(mappedBy="usuario" ,fetch=FetchType.LAZY)//carga solo la factura de ese cliente hasta que se invoca el get, cascade es para que tenga persistencia los datos de esta clase ej: cuando se guardan muchas facturas y mappedBy crea la relacion en la tabla factura agregando la id de cliente automaticamente
	private List<Requisicion> requisiciones;
	
	@ManyToOne( fetch=FetchType.LAZY)
	@JoinColumn(name = "id_depto")
	private Departamento depto;
	
	public Usuario() {
		this.rolesu = new ArrayList<Role>();
		requisiciones = new ArrayList<Requisicion>();
	}
	

	@PrePersist
	public void prePersist() {
		createAt = new Date();
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return rolesu;
	}

	public void setRoles(List<Role> roles) {
		this.rolesu = roles;
	}
    public void addRole(Role roles) {
        this.rolesu.add(roles);
    }
    
    public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	

	public String getNombreCompleto() {
		return nombreCompleto;
	}


	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}


	public List<Role> getRolesu() {
		return rolesu;
	}


	public void setRolesu(List<Role> rolesu) {
		this.rolesu = rolesu;
	}

	public String getCargo() {
		return cargo;
	}


	public void setCargo(String cargo) {
		this.cargo = cargo;
	}


	public String getP1() {
		return p1;
	}


	public void setP1(String p1) {
		this.p1 = p1;
	}


	public String getP2() {
		return p2;
	}


	public void setP2(String p2) {
		this.p2 = p2;
	}


	public Departamento getDepto() {
		return depto;
	}


	public void setDepto(Departamento depto) {
		this.depto = depto;
	}


	public List<Requisicion> getRequisiciones() {
		return requisiciones;
	}


	public void setRequisiciones(List<Requisicion> requisiciones) {
		this.requisiciones = requisiciones;
	}

	public void addRequisicion(Requisicion requisicion) {
		requisiciones.add(requisicion);
	}

	private static final long serialVersionUID = 1L;
	
	

}

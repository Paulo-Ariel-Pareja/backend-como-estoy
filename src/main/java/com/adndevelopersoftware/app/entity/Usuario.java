package com.adndevelopersoftware.app.entity;

import java.io.Serializable;
import java.util.List;

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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	@NotNull(message = "El usuario es obligatorio")
	private String usuario;
	
	@Column(nullable = false)
	@Size(min = 10, max = 90, message = "la clave publica debe tener entre 10 y 90 caracteres!")
	private String clavePersonal;
	
	@Column(nullable = false)
	@Size(min = 20, max = 90, message = "la clave privada debe 20 y 90 caracteres!")
	private String clavePrivada;
		
	@Column(nullable = false)
	@NotNull(message = "El mensaje es obligatorio")
	@Size(min = 1, max = 90, message = "el mensaje debe de tener entre 1 y 90 caracteres!")
	@OneToMany(fetch=FetchType.LAZY)
	private List<Mensaje> mensaje;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<Mensaje> getMensaje() {
		return mensaje;
	}

	public void setMensaje(List<Mensaje> mensaje) {
		this.mensaje = mensaje;
	}

	public String getClavePersonal() {
		return clavePersonal;
	}

	public void setClavePersonal(String clavePersonal) {
		this.clavePersonal = clavePersonal;
	}

	public String getClavePrivada() {
		return clavePrivada;
	}

	public void setClavePrivada(String clavePrivada) {
		this.clavePrivada = clavePrivada;
	}
	
}

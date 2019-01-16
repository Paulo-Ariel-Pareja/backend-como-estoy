package com.adndevelopersoftware.app.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DatoRecibe implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String clave;
	
	@NotNull(message = "Las coordenadas son obligatorias!")
	private String coordenadas;
	
	@NotNull(message = "El mensaje es obligatorio")
	@Size(min = 1, max = 90, message = "el mensaje debe de tener entre 1 y 90 caracteres!")
	private String mensaje;

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}

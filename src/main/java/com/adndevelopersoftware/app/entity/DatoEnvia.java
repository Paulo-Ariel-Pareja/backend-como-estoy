package com.adndevelopersoftware.app.entity;

import java.io.Serializable;
import java.util.List;

public class DatoEnvia implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Mensaje> mensaje;

	public List<Mensaje> getMensaje() {
		return mensaje;
	}

	public void setMensaje(List<Mensaje> mensaje) {
		this.mensaje = mensaje;
	}
	
}

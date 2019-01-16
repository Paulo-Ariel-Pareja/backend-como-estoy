package com.adndevelopersoftware.app.service;

import com.adndevelopersoftware.app.entity.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsuarioAndClavePersonal(String usuario, String clavePersonal);

}

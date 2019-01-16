package com.adndevelopersoftware.app.service;

import com.adndevelopersoftware.app.entity.Usuario;

public interface IUsuarioService {
	
	public Usuario save(Usuario usuario);
	
	public Usuario findByUsuarioAndClavePersonal(String usuario, String clavePersonal);
	
	public Usuario findByUsuarioAndClavePersonalAndClavePrivada(String usuario, String clavePersonal, String clavePrivada);

}

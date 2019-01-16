package com.adndevelopersoftware.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adndevelopersoftware.app.dao.IUsuarioDao;
import com.adndevelopersoftware.app.entity.Usuario;

@Service
public class UsuarioServiceImplementacion implements IUsuarioService{

	@Autowired
	private IUsuarioDao dao;

	@Override
	public Usuario findByUsuarioAndClavePersonal(String usuario, String clavePersonal) {
		return dao.findByUsuarioAndClavePersonal(usuario, clavePersonal);
	}

	@Override
	public Usuario save(Usuario cliente) {
		return dao.save(cliente);
	}

	@Override
	public Usuario findByUsuarioAndClavePersonalAndClavePrivada(String usuario, String clavePersonal,
			String clavePrivada) {
		return 	dao.findByUsuarioAndClavePersonalAndClavePrivada(usuario, clavePersonal, clavePrivada);

	}

}

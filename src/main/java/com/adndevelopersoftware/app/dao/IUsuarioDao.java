package com.adndevelopersoftware.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.adndevelopersoftware.app.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	public Usuario findByUsuario(String usuario);
	
	public Usuario findByUsuarioAndClavePersonal(String usuario, String clavePersonal);

	/*
	@Query(value = "select u.id, u.usuario, u.clave_personal, m.id, m.coordenadas, m.create_at, m.mensaje from "
			+ "(select * from usuarios WHERE usuario = ?1 and clave_personal = ?2) as u inner "
			+ "join (select * from usuarios_mensaje) as i on i.usuario_id = u.id inner join (select * from "
			+ "mensajes) as m on i.mensaje_id = m.id order by m.create_at desc", nativeQuery = true)
    public Usuario consultaPersonalizada(String usuario, 
    					String clavePersonal);
	 */
}

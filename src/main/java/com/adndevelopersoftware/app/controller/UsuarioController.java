package com.adndevelopersoftware.app.controller;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adndevelopers.adn.clientesapp2.models.entity.Cliente;
import com.adndevelopersoftware.app.entity.Mensaje;
import com.adndevelopersoftware.app.entity.Usuario;
import com.adndevelopersoftware.app.service.UsuarioServiceImplementacion;

@RestController
@RequestMapping("/api")
public class UsuarioController {
	
	@Autowired
	UsuarioServiceImplementacion service;

	/*
	@GetMapping("/usuarios/{usuario}")
	public ResponseEntity<?> obtenerUsuario(@PathVariable String usuario) {
		Usuario usuarioEncontrado = null;
		Map<String, Object> response = new HashMap<>();
		try {
			usuarioEncontrado = service.findByUsuario(usuario);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (usuarioEncontrado == null) {
			response.put("mensaje", "El cliente ".concat(usuario.toString().concat(" no esta registrado!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Usuario>(usuarioEncontrado, HttpStatus.OK);
	}
	*/
	@PostMapping("/usuarios/{usuario}/{clavePersonal}")
	public ResponseEntity<?> insertarComentario(@PathVariable String usuario, 
												@PathVariable String clavePersonal,
												@Valid @RequestBody Mensaje mensajeNuevo, 
												BindingResult result){
		
		Usuario usuarioEncontrado = null;
		Map<String, Object> response = new HashMap<>();
		try {
			usuarioEncontrado = service.findByUsuarioAndClavePersonal(usuario, clavePersonal);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (usuarioEncontrado == null) {
			response.put("mensaje", "El usuario o la clave son incorrectas");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		List<Mensaje> mOtros = usuarioEncontrado.getMensaje();
		Collections.sort(mOtros, new MensajeComparatorByFecha(false));
		
		usuarioEncontrado.setMensaje(mOtros);
		return new ResponseEntity<Usuario>(usuarioEncontrado, HttpStatus.OK);
	}
	
	@GetMapping("/usuarios/{usuario}/{clavePersonal}")
	public ResponseEntity<?> obtenerUsuarioConClave(@PathVariable String usuario, 
													@PathVariable String clavePersonal) {
		Usuario usuarioEncontrado = null;
		Map<String, Object> response = new HashMap<>();
		try {
			usuarioEncontrado = service.findByUsuarioAndClavePersonal(usuario, clavePersonal);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (usuarioEncontrado == null) {
			response.put("mensaje", "El usuario o la clave son incorrectas");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		List<Mensaje> mOtros = usuarioEncontrado.getMensaje();
		Collections.sort(mOtros, new MensajeComparatorByFecha(false));
		
		usuarioEncontrado.setMensaje(mOtros);
		return new ResponseEntity<Usuario>(usuarioEncontrado, HttpStatus.OK);
	}
	
	class MensajeComparatorByFecha implements Comparator<Mensaje> {
	    private boolean asc;
	    MensajeComparatorByFecha(boolean asc) {
	        this.asc = asc;
	    }
	    @Override
	    public int compare(Mensaje o1, Mensaje o2) {
	        int ret;
	        if (asc) {
	            ret = o1.getCreateAt().compareTo(o2.getCreateAt());
	        } else {
	            ret = o2.getCreateAt().compareTo(o1.getCreateAt());
	        }
	        return ret;
	    }
	}
}

package com.adndevelopersoftware.app.controller;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adndevelopersoftware.app.entity.Mensaje;
import com.adndevelopersoftware.app.entity.Usuario;
import com.adndevelopersoftware.app.service.UsuarioServiceImplementacion;

@RestController
@RequestMapping("/api")
public class UsuarioController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	UsuarioServiceImplementacion service;

	@PostMapping("/usuarios/{usuario}/{clavePersonal}/{clavePersonal}")
	public ResponseEntity<?> insertarComentario(@PathVariable String usuario, @PathVariable String clavePersonal,
			//@RequestBody String clavePrivada, 
			@RequestBody Mensaje mensaje, 
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		Mensaje mensajeNuevo = null;
		Usuario usuarioEncontrado = null;
		Usuario usuarioNuevo = null;

		try {
			usuarioEncontrado = service.findByUsuarioAndClavePersonal(usuario, clavePersonal);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String clavePrivada = "123456789123456789";
		System.out.println("clave privada: "  + clavePrivada);
		String dbPass = usuarioEncontrado.getClavePrivada();
		if (usuarioEncontrado == null || !passwordEncoder.matches(clavePrivada, dbPass)) {
			response.put("mensaje", "El usuario o las claves son incorrectas");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		List<Mensaje> listaMensajes = usuarioEncontrado.getMensaje();
		
		mensajeNuevo.setCoordenadas(mensaje.getCoordenadas());
		mensajeNuevo.setMensaje(mensaje.getMensaje());
		listaMensajes.add(mensajeNuevo);
		usuarioEncontrado.setMensaje(listaMensajes);
		usuarioNuevo = service.save(usuarioEncontrado);
		response.put("mensaje", "datos actualizados");
		response.put("usuario", usuarioNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@PostMapping("/usuarios/{usuario}")
	public ResponseEntity<?> insertarNuevoUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {

		Usuario usuarioNuevo = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			usuarioNuevo = service.save(usuario);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error registrar el usuario.");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Usuario creado con exito!");
		response.put("usuario", usuarioNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/usuarios/{usuario}/{clavePersonal}")
	public ResponseEntity<?> obtenerUsuarioConClave(@PathVariable String usuario, @PathVariable String clavePersonal) {
		Usuario usuarioEncontrado = null;
		Map<String, Object> response = new HashMap<>();
		try {
			usuarioEncontrado = service.findByUsuarioAndClavePersonal(usuario, clavePersonal);
		} catch (DataAccessException e) {
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

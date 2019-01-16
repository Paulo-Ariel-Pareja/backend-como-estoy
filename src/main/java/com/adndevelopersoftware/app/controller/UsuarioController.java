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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adndevelopersoftware.app.entity.DatoEnvia;
import com.adndevelopersoftware.app.entity.DatoRecibe;
import com.adndevelopersoftware.app.entity.Mensaje;
import com.adndevelopersoftware.app.entity.Usuario;
import com.adndevelopersoftware.app.service.MensajeServiceImplementacion;
import com.adndevelopersoftware.app.service.UsuarioServiceImplementacion;

@RestController
@RequestMapping("/api")
public class UsuarioController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	UsuarioServiceImplementacion uService;
	
	@Autowired
	MensajeServiceImplementacion mService;

	@PutMapping("/usuarios/{usuario}/{clavePersonal}")
	public ResponseEntity<?> insertarComentario(@PathVariable String usuario, 
												@PathVariable String clavePersonal,
												@RequestBody DatoRecibe datosRecibe) {

		Map<String, Object> response = new HashMap<>();

		Usuario usuarioEncontrado = null;
		Usuario usuarioUpdated = null;
		try {
			usuarioEncontrado = uService.findByUsuarioAndClavePersonal(usuario, clavePersonal);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String clavePrivada = datosRecibe.getClave();
		
		if (usuarioEncontrado == null || !passwordEncoder.matches(clavePrivada, usuarioEncontrado.getClavePrivada())) {
			response.put("mensaje", "El usuario o las claves son incorrectas");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		Mensaje mensajeNuevo = new Mensaje();
		Usuario usuarioGuardar = new Usuario();
		List<Mensaje> listaMensajes = usuarioEncontrado.getMensaje();
		try {
			String coordenadas = datosRecibe.getCoordenadas();
			if(coordenadas==null) {
				response.put("error", "Las coordenadas son obligatorias!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			String mensaje = datosRecibe.getMensaje();
			if(mensaje==null) {
				response.put("error", "El mensaje es obligatorio!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			mensajeNuevo.setCoordenadas(coordenadas);
			mensajeNuevo.setMensaje(mensaje);
			Mensaje mensajeGuardado = mService.save(mensajeNuevo);
			listaMensajes.add(mensajeGuardado);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error registrar el mensaje.");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
		try {
			usuarioEncontrado.setMensaje(listaMensajes);
			usuarioUpdated = uService.save(usuarioEncontrado);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar al cliente.");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Collections.sort(listaMensajes, new MensajeComparatorByFecha(false));
		DatoEnvia datoEnvia = new DatoEnvia();
		datoEnvia.setMensaje(listaMensajes);
		return new ResponseEntity<DatoEnvia>(datoEnvia, HttpStatus.CREATED);
	}

	@PostMapping("/usuarios")
	public ResponseEntity<?> insertarNuevoUsuario(@Valid @RequestBody Usuario usuario, 
												BindingResult result) {

		Usuario usuarioNuevo = null;
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		String claveOriginal = null;
		
		try {
			claveOriginal = usuario.getClavePrivada();
			String clave = passwordEncoder.encode(claveOriginal);
			usuario.setClavePrivada(clave);
			usuarioNuevo = uService.save(usuario);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error registrar el usuario.");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		usuarioNuevo.setClavePrivada(claveOriginal);
		response.put("usuario", usuarioNuevo);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/usuarios/{usuario}/{clavePersonal}")
	public ResponseEntity<?> obtenerUsuarioConClave(@PathVariable String usuario, @PathVariable String clavePersonal) {
		Usuario usuarioEncontrado = null;
		Map<String, Object> response = new HashMap<>();
		try {
			usuarioEncontrado = uService.findByUsuarioAndClavePersonal(usuario, clavePersonal);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (usuarioEncontrado == null) {
			response.put("mensaje", "El usuario o la clave son incorrectas");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		List<Mensaje> enviarMensajes = usuarioEncontrado.getMensaje();
		Collections.sort(enviarMensajes, new MensajeComparatorByFecha(false));

		DatoEnvia datoEnvia = new DatoEnvia();
		datoEnvia.setMensaje(enviarMensajes);
		return new ResponseEntity<DatoEnvia>(datoEnvia, HttpStatus.OK);
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

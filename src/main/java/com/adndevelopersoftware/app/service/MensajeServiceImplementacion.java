package com.adndevelopersoftware.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adndevelopersoftware.app.dao.IMensajeDao;
import com.adndevelopersoftware.app.entity.Mensaje;

@Service
public class MensajeServiceImplementacion implements IMensajeService{

	@Autowired
	private IMensajeDao dao;
	
	@Override
	public Mensaje save(Mensaje mensaje) {
		return dao.save(mensaje);
	}

}

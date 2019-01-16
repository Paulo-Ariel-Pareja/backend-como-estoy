package com.adndevelopersoftware.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.adndevelopersoftware.app.entity.Mensaje;


public interface IMensajeDao extends CrudRepository<Mensaje, Long>{

}

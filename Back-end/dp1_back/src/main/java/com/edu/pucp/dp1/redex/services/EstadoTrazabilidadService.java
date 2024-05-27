package com.edu.pucp.dp1.redex.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pucp.dp1.redex.model.EstadoTrazabilidad;
import com.edu.pucp.dp1.redex.repository.EstadoTrazabilidadRepository;

@Service
public class EstadoTrazabilidadService {
    
    @Autowired
    private EstadoTrazabilidadRepository estadoTrazabilidadRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadoTrazabilidadService.class);

    public EstadoTrazabilidad register(EstadoTrazabilidad estadoTrazabilidad){
        try {
            return estadoTrazabilidadRepository.save(estadoTrazabilidad);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<EstadoTrazabilidad> getAll(){
        try {
            return estadoTrazabilidadRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public EstadoTrazabilidad get(int id){
        try {
            return estadoTrazabilidadRepository.findEstadoTrazabilidadById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public EstadoTrazabilidad update(EstadoTrazabilidad estadoTrazabilidad){
        try {
            return estadoTrazabilidadRepository.save(estadoTrazabilidad);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            estadoTrazabilidadRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}

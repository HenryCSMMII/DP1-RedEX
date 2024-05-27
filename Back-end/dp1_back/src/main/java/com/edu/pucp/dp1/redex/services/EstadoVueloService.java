package com.edu.pucp.dp1.redex.services;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pucp.dp1.redex.model.EstadoVuelo;
import com.edu.pucp.dp1.redex.repository.EstadoVueloRepository;


@Service
public class EstadoVueloService {
    
    @Autowired
    private EstadoVueloRepository estadoVueloRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadoVueloService.class);

    public EstadoVuelo register(EstadoVuelo estadoVuelo){
        try {
            return estadoVueloRepository.save(estadoVuelo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<EstadoVuelo> getAll(){
        try {
            return estadoVueloRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public EstadoVuelo get(int id){
        try {
            return estadoVueloRepository.findEstadoVueloById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public EstadoVuelo update(EstadoVuelo estadoVuelo){
        try {
            return estadoVueloRepository.save(estadoVuelo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }


    public void delete(int id){
        try {
            estadoVueloRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}

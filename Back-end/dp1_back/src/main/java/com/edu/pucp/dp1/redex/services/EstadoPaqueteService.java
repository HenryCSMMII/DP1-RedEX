package com.edu.pucp.dp1.redex.services;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pucp.dp1.redex.model.EstadoPaquete;
import com.edu.pucp.dp1.redex.repository.EstadoPaqueteRepository;


@Service
public class EstadoPaqueteService {
    
    @Autowired
    private EstadoPaqueteRepository estadoPaqueteRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadoPaqueteService.class);

    public EstadoPaquete register(EstadoPaquete estadoPaquete){
        try {
            return estadoPaqueteRepository.save(estadoPaquete);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<EstadoPaquete> getAll(){
        try {
            return estadoPaqueteRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public EstadoPaquete get(int id){
        try {
            return estadoPaqueteRepository.findEstadoPaqueteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public EstadoPaquete update(EstadoPaquete estadoPaquete){
        try {
            return estadoPaqueteRepository.save(estadoPaquete);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            estadoPaqueteRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}

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

import com.edu.pucp.dp1.redex.model.Paquete;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.EstadoPaquete;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.repository.PaqueteRepository;
import com.edu.pucp.dp1.redex.repository.AirportRepository;
import com.edu.pucp.dp1.redex.repository.EstadoPaqueteRepository;

@Service
public class PaqueteService {
    
    @Autowired
    private PaqueteRepository paqueteRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private EstadoPaqueteRepository estadoPaqueteRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaqueteService.class);

    public Paquete register(Paquete paquete){
        try {
            return paqueteRepository.save(paquete);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Paquete> getAll(){
        try {
            return paqueteRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Paquete get(int id){
        try {
            return paqueteRepository.findPackageById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Paquete update(Paquete paquete){
        try {
            return paqueteRepository.save(paquete);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            paqueteRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public List<Paquete> listarPaquetesPorAeropuerto(int aeropuertoId){
        try {
            Airport airport = new Airport();
            airport = airportRepository.findAirportById(aeropuertoId);
            return paqueteRepository.findPackageByAirport(airport);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Paquete> listarPaquetesPorEstado(int estadoId){
        try {
            EstadoPaquete estado = new EstadoPaquete();
            estado = estadoPaqueteRepository.findEstadoPaqueteById(estadoId);
            return paqueteRepository.findPackageByEstadoPaquete(estado);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Paquete> listPaquetesByIds(int idInicio, int idFinal){
        try{
            List<Paquete> paquetes = paqueteRepository.findPaqueteByIds(idInicio, idFinal);
            return paquetes;
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }    

}

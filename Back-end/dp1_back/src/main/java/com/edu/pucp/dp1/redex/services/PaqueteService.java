package com.edu.pucp.dp1.redex.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.edu.pucp.dp1.redex.model.Paquete;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.EstadoPaquete;
import com.edu.pucp.dp1.redex.repository.PaqueteRepository;
import com.edu.pucp.dp1.redex.repository.AirportRepository;
import com.edu.pucp.dp1.redex.repository.FlightRepository;
import com.edu.pucp.dp1.redex.repository.EstadoPaqueteRepository;

@Service
public class PaqueteService {
    
    @Autowired
    private PaqueteRepository paqueteRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private EstadoPaqueteRepository estadoPaqueteRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaqueteService.class);

    @Transactional
    public Paquete register(Paquete paquete){
        try {
            // Obtener aeropuertos y vuelo por ID
            Airport origin = airportRepository.findById(paquete.getOrigin().getId())
                                              .orElseThrow(() -> new RuntimeException("Origin airport not found"));
            Airport destination = airportRepository.findById(paquete.getDestination().getId())
                                                   .orElseThrow(() -> new RuntimeException("Destination airport not found"));
            Flight assignedFlight = flightRepository.findById(paquete.getAssignedFlight().getId())
                                                    .orElseThrow(() -> new RuntimeException("Assigned flight not found"));
            // Asignar aeropuertos y vuelo obtenidos
            paquete.setOrigin(origin);
            paquete.setDestination(destination);
            paquete.setAssignedFlight(assignedFlight);
            return paqueteRepository.save(paquete);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public List<Paquete> getAll(){
        try {
            return paqueteRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public Paquete get(int id){
        try {
            return paqueteRepository.findPackageById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public Paquete update(Paquete paquete){
        try {
            // Obtener aeropuertos y vuelo por ID
            Airport origin = airportRepository.findById(paquete.getOrigin().getId())
                                              .orElseThrow(() -> new RuntimeException("Origin airport not found"));
            Airport destination = airportRepository.findById(paquete.getDestination().getId())
                                                   .orElseThrow(() -> new RuntimeException("Destination airport not found"));
            Flight assignedFlight = flightRepository.findById(paquete.getAssignedFlight().getId())
                                                    .orElseThrow(() -> new RuntimeException("Assigned flight not found"));
            // Asignar aeropuertos y vuelo obtenidos
            paquete.setOrigin(origin);
            paquete.setDestination(destination);
            paquete.setAssignedFlight(assignedFlight);
            return paqueteRepository.save(paquete);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
            Airport airport = airportRepository.findById(aeropuertoId)
                                               .orElseThrow(() -> new RuntimeException("Airport not found"));
            return paqueteRepository.findPackageByAirport(airport);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public List<Paquete> listarPaquetesPorEstado(int estadoId){
        try {
            EstadoPaquete estado = estadoPaqueteRepository.findById(estadoId)
                                                          .orElseThrow(() -> new RuntimeException("EstadoPaquete not found"));
            return paqueteRepository.findPackageByEstadoPaquete(estado);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}

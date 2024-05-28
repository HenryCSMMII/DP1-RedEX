package com.edu.pucp.dp1.redex.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.repository.ShipmentRepository;
import com.edu.pucp.dp1.redex.repository.AirportRepository;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private AirportRepository airportRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentService.class);

    @Transactional
    public Shipment register(Shipment shipment){
        try {
            // Obtener aeropuertos por ID
            Airport origen = airportRepository.findById(shipment.getOrigen().getId())
                                              .orElseThrow(() -> new RuntimeException("Origin airport not found"));
            Airport destino = airportRepository.findById(shipment.getDestino().getId())
                                               .orElseThrow(() -> new RuntimeException("Destination airport not found"));
            // Asignar aeropuertos obtenidos
            shipment.setOrigen(origen);
            shipment.setDestino(destino);
            return shipmentRepository.save(shipment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;  // Propagar la excepción para que el controlador pueda manejarla
        }
    }

    public List<Shipment> getAll(){
        try {
            return shipmentRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public Shipment get(int id){
        try {
            return shipmentRepository.findById(id).orElse(null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public Shipment update(Shipment shipment){
        try {
            // Obtener aeropuertos por ID
            Airport origen = airportRepository.findById(shipment.getOrigen().getId())
                                              .orElseThrow(() -> new RuntimeException("Origin airport not found"));
            Airport destino = airportRepository.findById(shipment.getDestino().getId())
                                               .orElseThrow(() -> new RuntimeException("Destination airport not found"));
            // Asignar aeropuertos obtenidos
            shipment.setOrigen(origen);
            shipment.setDestino(destino);
            return shipmentRepository.save(shipment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void delete(int id){
        try {
            shipmentRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}

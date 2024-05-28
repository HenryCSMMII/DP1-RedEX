package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.dto.ShipmentDTO;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.repository.ShipmentRepository;
import com.edu.pucp.dp1.redex.repository.AirportRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
            Airport origen = airportRepository.findById(shipment.getOrigen().getId())
                                              .orElseThrow(() -> new RuntimeException("Origin airport not found"));
            Airport destino = airportRepository.findById(shipment.getDestino().getId())
                                               .orElseThrow(() -> new RuntimeException("Destination airport not found"));
            shipment.setOrigen(origen);
            shipment.setDestino(destino);
            return shipmentRepository.save(shipment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public List<ShipmentDTO> getAll(){
        try {
            List<Shipment> shipments = shipmentRepository.findAll();
            return shipments.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public ShipmentDTO get(int id){
        try {
            Shipment shipment = shipmentRepository.findById(id).orElse(null);
            return convertToDTO(shipment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public Shipment update(Shipment shipment){
        try {
            Airport origen = airportRepository.findById(shipment.getOrigen().getId())
                                              .orElseThrow(() -> new RuntimeException("Origin airport not found"));
            Airport destino = airportRepository.findById(shipment.getDestino().getId())
                                               .orElseThrow(() -> new RuntimeException("Destination airport not found"));
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

    public List<ShipmentDTO> listShipmentsByIds(int idInicio, int idFinal){
        try {
            List<Shipment> shipments = shipmentRepository.findShipmentByIds(idInicio, idFinal);
            return shipments.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    private ShipmentDTO convertToDTO(Shipment shipment) {
        return new ShipmentDTO(
            shipment.getId(),
            shipment.getCantidad(),
            shipment.getOrigen().getId(),
            shipment.getDestino().getId(),
            shipment.getTipo(),
            shipment.getFechaInicio(),
            shipment.getFechaFin(),
            shipment.getTiempoActivo()
        );
    }
}

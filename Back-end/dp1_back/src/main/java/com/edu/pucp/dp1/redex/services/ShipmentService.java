package com.edu.pucp.dp1.redex.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pucp.dp1.redex.model.Paquete;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.repository.ShipmentRepository;

@Service
public class ShipmentService {
    
    @Autowired
    private ShipmentRepository shipmentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentService.class);

    public Shipment register(Shipment shipment){
        try {
            return shipmentRepository.save(shipment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Shipment> getAll(){
        try {
            return shipmentRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Shipment get(int id){
        try {
            return shipmentRepository.findShipmentById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Shipment update(Shipment shipment){
        try {
            return shipmentRepository.save(shipment);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            shipmentRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public List<Shipment> listShipmentsByIds(int idInicio, int idFinal){
        try{
            List<Shipment> envios = shipmentRepository.findShipmentByIds(idInicio, idFinal);
            return envios;
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }    
}

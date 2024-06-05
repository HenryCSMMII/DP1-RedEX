package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.dto.ShipmentDTO;
import com.edu.pucp.dp1.redex.model.Shipment;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @GetMapping("/all")
    public List<ShipmentDTO> getAllShipments() {
        return BD.shipmentsTemp.stream()
                .map(shipment -> new ShipmentDTO(
                        shipment.getId(),
                        shipment.getPackageQuantity(),
                        shipment.getDepartureAirport().getId(),
                        shipment.getArrivalAirport().getId(),
                        shipment.getState() != null ? shipment.getState().name() : null,
                        shipment.getDepartureTime(),
                        shipment.getArrivalTime(),
                        shipment.getClient() != null ? shipment.getClient().getDni() : null))
                .collect(Collectors.toList());
    }

    @GetMapping("/read")
    public String readShipments() {
        try {
            BD.readShipments();
            return "Envíos leídos exitosamente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al leer el archivo de envíos.";
        }
    }
}

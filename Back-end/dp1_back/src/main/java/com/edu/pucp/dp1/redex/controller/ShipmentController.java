package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.dto.ShipmentDTO;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.model.State;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    @GetMapping("/")
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

    @PostMapping("/read")
    public List<ShipmentDTO> readShipments(@RequestParam("file") MultipartFile file) {
        try {
            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            
            BD.readShipments(convFile.getAbsolutePath());
            
            return BD.shipmentsTemp.stream()
                    .map(shipment -> {
                        Integer departureAirportId = shipment.getDepartureAirport() != null ? shipment.getDepartureAirport().getId() : null;
                        Integer arrivalAirportId = shipment.getArrivalAirport() != null ? shipment.getArrivalAirport().getId() : null;
                        String state = shipment.getState() != null ? shipment.getState().name() : null;
                        Integer clientSenderId = shipment.getClient() != null ? shipment.getClient().getDni() : null;
                        
                        return new ShipmentDTO(
                                shipment.getId(),
                                shipment.getPackageQuantity(),
                                departureAirportId != null ? departureAirportId : 0,
                                arrivalAirportId != null ? arrivalAirportId : 0,
                                state,
                                shipment.getDepartureTime(),
                                shipment.getArrivalTime(),
                                clientSenderId);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    @PostMapping("/create")
    public String createShipment(@RequestBody Shipment request) {
        try {
            // Set departure airport
            Airport departure = BD.airports.stream()
                    .filter(a -> a.getId() == request.getDepartureAirport().getId())
                    .findFirst()
                    .orElse(null);
            request.setDeparturAirport(departure);
            
            // Set arrival airport
            Airport arrival = BD.airports.stream()
                    .filter(a -> a.getId() == request.getArrivalAirport().getId())
                    .findFirst()
                    .orElse(null);
            request.setArrivalAirport(arrival);
            
            // Set state
            request.setState(State.Creado);
            
            // Set register date time
            request.setRegisterDateTime(request.getRegisterDateTime());
            
            BD.shipmentsTemp.add(request);
            return "Envío creado exitosamente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al crear el envío.";
        }
    }    
}

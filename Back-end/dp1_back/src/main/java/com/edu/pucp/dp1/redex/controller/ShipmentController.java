package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.dto.ShipmentDTO;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.model.State;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public String readShipments(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }
        
        try {
            // Save the file to a temporary location
            Path tempDir = Files.createTempDirectory("");
            Path tempFile = tempDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFile);
            
            // Read the shipments from the file
            BD.readShipments(tempFile.toString());
            
            // Delete the temporary file
            Files.delete(tempFile);
            Files.delete(tempDir);
            
            return "Envíos leídos exitosamente.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al leer el archivo de envíos.";
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
            
            BD.shipmentsTemp.add(request);
            return "Envío creado exitosamente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al crear el envío.";
        }
    }    
}

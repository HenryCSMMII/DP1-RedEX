package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.dto.AirportDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    @GetMapping("/all")
    public List<AirportDTO> getAllAirports() {
                return BD.airports.stream()
                .map(airport -> new AirportDTO(
                    airport.getId(),
                    airport.getCode(),
                    airport.getLatitude(),
                    airport.getLongitude(),
                    airport.getMax_capacity(),
                    airport.getCountry().getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/read")
    public String readAirports() {
        try {
            BD.readAirports();
            return "Aeropuertos leídos exitosamente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al leer el archivo de aeropuertos.";
        }
    }
}

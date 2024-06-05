package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.dto.FlightDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vuelo")
public class FlightController {

    @GetMapping("/")
    public List<FlightDTO> getAllFlights() {
        return BD.flightsTemp.stream()
                .map(flight -> new FlightDTO(
                        flight.getId(),
                        flight.getCode(),
                        flight.getMax_capacity(),
                        flight.getUsed_capacityAlgorithm(),
                        flight.getDeparture_date_time(),
                        flight.getArrival_date_time(),
                        flight.getDeparture_airport().getId(),
                        flight.getArrival_airport().getId(),
                        flight.getEstimated_time()))
                .collect(Collectors.toList());
    }

    

    @GetMapping("/read")
    public String readFlights() {
        try {
            BD.readFlights();
            return "Vuelos leídos exitosamente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al leer el archivo de vuelos.";
        }
    }

    @GetMapping(value = "/{idInicio}/{idFinal}")
    public List<FlightDTO> listarFlightsByIds(@PathVariable int idInicio, @PathVariable int idFinal) {
        // Verificar y ajustar los índices
        if (idInicio < 1)idInicio = 1;
        if (idFinal > BD.flightsTemp.size())idFinal = BD.flightsTemp.size();
        if (idInicio > idFinal) return List.of();

        // Crear sublista y mapear a DTOs
        return BD.flightsTemp.subList(idInicio-1, idFinal).stream()
                .map(flight -> new FlightDTO(
                        flight.getId(),
                        flight.getCode(),
                        flight.getMax_capacity(),
                        flight.getUsed_capacityAlgorithm(),
                        flight.getDeparture_date_time(),
                        flight.getArrival_date_time(),
                        flight.getDeparture_airport().getId(),
                        flight.getArrival_airport().getId(),
                        flight.getEstimated_time()))
                .collect(Collectors.toList());
    }

}

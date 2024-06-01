package com.edu.pucp.dp1.redex.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class FlightDTO {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final LocalDate BASE_DATE = LocalDate.of(2024, 1, 1);  // Fecha base arbitraria    
    private int id;
    private String origin;
    private String destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int capacity;
    private String flightNumber;
    private int currentLoad;
    private int duration;
    private int estadoVueloId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;


    public FlightDTO(String origin, String destination, String departureTime, String arrivalTime, int capacity) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = parseTime(departureTime);
        this.arrivalTime = parseTime(arrivalTime);
        this.capacity = capacity;
        this.currentLoad = 0;
    }

    private static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            System.err.println("Hora nula o vacía proporcionada, no se puede parsear.");
            return null;
        }
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la hora: " + timeStr);
            return null;
        }
    }    
}

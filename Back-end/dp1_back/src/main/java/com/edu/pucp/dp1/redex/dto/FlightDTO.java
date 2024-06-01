package com.edu.pucp.dp1.redex.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class FlightDTO {
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
}

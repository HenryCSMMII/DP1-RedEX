package com.edu.pucp.dp1.redex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PaqueteDTO {
    private int id;
    private int originId;
    private int destinationId;
    private LocalTime departureTime;
    private LocalDate shipmentDateTime;
    private String packageId;
    private int quantity;
    private int assignedFlightId;
    private double tiempoTotal;
    private int airportId;
    private int estadoPaqueteId;
    private int shipmentId;
}

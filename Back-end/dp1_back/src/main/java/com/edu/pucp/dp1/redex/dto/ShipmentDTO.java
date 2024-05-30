package com.edu.pucp.dp1.redex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ShipmentDTO {

    private int id;
    private int cantidad;
    private int origenId;
    private int destinoId;
    private int tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double tiempoActivo;
    private int clientSenderId;
    private int clientReceiverId;
}

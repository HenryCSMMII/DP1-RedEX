package com.edu.pucp.dp1.redex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PackageFlightDTO {

    private int id;
    private int paqueteId;
    private int flightId;
    private int estadoTrazabilidadId;
}

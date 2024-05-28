package com.edu.pucp.dp1.redex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class NodeDTO {
    private int id;
    private Double latitud;
    private Double longitud;
    private int airportId;
    private int flightId;
}

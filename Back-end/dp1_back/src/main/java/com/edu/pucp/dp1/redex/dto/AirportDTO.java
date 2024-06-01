package com.edu.pucp.dp1.redex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AirportDTO {

    private int id;
    private String codigoIATA;
    private Double latitude;
    private Double longitude;
    private int capacity;
    private int currentCapacity;
    private int cityId; // Solo el ID de la ciudad

    public AirportDTO(String codigoIATA,int cityId, int capacity){
        this.codigoIATA = codigoIATA;
        this.cityId = cityId;
        this.capacity = capacity;
    }
}

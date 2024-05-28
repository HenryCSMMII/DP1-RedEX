package com.edu.pucp.dp1.redex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CityDTO {
    private int id;
    private String nombre;
    private String abreviatura;
    private int zonahoraria;
    private int countryId;
}

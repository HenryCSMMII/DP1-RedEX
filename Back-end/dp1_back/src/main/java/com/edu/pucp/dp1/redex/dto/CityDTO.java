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
    private int countryId;
    private String name;

    // Additional constructors or methods if needed
}

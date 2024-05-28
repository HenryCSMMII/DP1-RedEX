package com.edu.pucp.dp1.redex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CountryDTO {

    private int id;
    private String name;
    private String shortname;
    private int continentId;
}

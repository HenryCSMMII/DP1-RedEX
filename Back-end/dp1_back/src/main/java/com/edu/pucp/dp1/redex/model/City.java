package com.edu.pucp.dp1.redex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter @Setter
public class City {

    private int id;
	private static int increment = 0;
    private int countryId;
    private String name;

     public City() {
		City.increment += 1;
		this.id = City.increment;
	}

    // Additional constructors or methods if needed
}

package com.edu.pucp.dp1.redex.dto;

import java.util.Date;

import com.edu.pucp.dp1.redex.model.Airport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter

public class FlightDTO {
	private int id;
	private String code;
	private int max_capacity;
	private int[] used_capacity;
	private Date departure_date_time;
	private Date arrival_date_time;
	private int departure_airportId;
	private int arrival_airportId;
	private long estimated_time;
}

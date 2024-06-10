package com.edu.pucp.dp1.redex.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class FlightDTO {
    private int id;
    private String code;
    private int max_capacity;
    private int used_capacity;
    private Date departure_date_time;
    private Date arrival_date_time;
    private int departure_airportId;
    private int arrival_airportId;
    private long estimated_time;
    private String state;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<ShipmentDTO> shipment = null;

    public FlightDTO(int id, String code, int max_capacity, int used_capacity, Date departure_date_time,
                     Date arrival_date_time, int departure_airportId, int arrival_airportId, long estimated_time, String state) {
        this.id = id;
        this.code = code;
        this.max_capacity = max_capacity;
        this.used_capacity = used_capacity;
        this.departure_date_time = departure_date_time;
        this.arrival_date_time = arrival_date_time;
        this.departure_airportId = departure_airportId;
        this.arrival_airportId = arrival_airportId;
        this.estimated_time = estimated_time;
        this.state = state;
    }
}

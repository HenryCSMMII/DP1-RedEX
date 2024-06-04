package com.edu.pucp.dp1.redex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ShipmentDTO {
    private Integer id;
    private int packageQuantity;
    private int departureAirportId;
    private int arrivalAirportId;
    private String state;
    private Date departureTime;
    private Date arrivalTime;
    private Integer clientSenderId;  // Asegúrate de que este campo es Integer
}

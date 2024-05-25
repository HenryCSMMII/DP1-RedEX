package com.edu.pucp.dp1.redex.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.edu.pucp.dp1.redex.model.BaseEntity;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "flight")
@SQLDelete(sql = "UPDATE flight SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter

public class Flight extends BaseEntity{

    public Flight(String origin2, String destination2, String departureTime2, String arrivalTime2, int capacity2,
            String flightnumber2, int currentLoad2, int duration2) {
        //TODO Auto-generated constructor stub
    }

    // Atributos estáticos
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final LocalDate BASE_DATE = LocalDate.of(2024, 1, 1);  // Fecha base arbitraria

    // Atributos de instancia

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "departureTime", nullable = false)
    private LocalTime departureTime;

    @Column(name = "arrivalTime", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "flightNumber", nullable = false)
    private String flightNumber;

    @Column(name = "currentLoad", nullable = false)
    private int currentLoad;

    @Column(name = "duration", nullable = false)
    private int duration;

    // Relaciones con otras entidades va en paquetes
    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "id_paquete")
    // private Flight flight;

}

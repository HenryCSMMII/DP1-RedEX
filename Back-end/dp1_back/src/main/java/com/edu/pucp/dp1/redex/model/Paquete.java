package com.edu.pucp.dp1.redex.model;

import java.time.LocalDate;
import java.time.LocalTime;

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

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "package")
@SQLDelete(sql = "UPDATE package SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter

public class Paquete extends BaseEntity{

    // String origin;
    // String destination;
    // String departureTime;
    // private LocalDateTime shipmentDateTime;
    // String packageId;
    // int quantity;
    // private String assignedFlightId;
    // private List<Flight> escalas;
    // private long tiempoTotal;

    @Column(name="origin", nullable = false)
    private String origin;

    @Column(name="destination", nullable = false)
    private String destination;

    @Column(name="departureTime", nullable = false)
    private LocalTime departureTime;

    @Column(name="shipmentDateTime", nullable = false)
    private LocalDate shipmentDateTime;

    @Column(name="packageId", nullable = false)
    private String packageId;

    @Column(name="quantity", nullable = false)
    private int quantity;

    @Column(name="assignedFlightId", nullable = false)
    private String assignedFlightId;

    @Column(name="tiempoTotal", nullable = false)
    private double tiempoTotal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAeropuerto")
    private Airport airport;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "idEstadoPaquete")
    private EstadoPaquete estadoPaquete;

}

package com.edu.pucp.dp1.redex.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Paquete extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    @JsonBackReference
    private Shipment shipment;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "package_flight",
        joinColumns = @JoinColumn(name = "package_id"),
        inverseJoinColumns = @JoinColumn(name = "flight_id")
    )
    private List<Flight> flights;

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Paquete{" +
                "id=" + getId() +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", shipmentDateTime=" + shipmentDateTime +
                ", packageId='" + packageId + '\'' +
                ", quantity=" + quantity +
                ", assignedFlightId='" + assignedFlightId + '\'' +
                ", tiempoTotal=" + tiempoTotal +
                ", airport=" + (airport != null ? airport.getCodigoIATA() : "null") +
                ", estadoPaquete=" + (estadoPaquete != null ? estadoPaquete.getId() : "null") +
                ", shipment=" + (shipment != null ? shipment.getId() : "null") +
                '}';
    }
}

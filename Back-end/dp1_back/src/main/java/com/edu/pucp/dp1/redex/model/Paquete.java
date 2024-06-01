package com.edu.pucp.dp1.redex.model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
//import javax.persistence.OneToMany;
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
@Table(name = "paquete")
@SQLDelete(sql = "UPDATE paquete SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Paquete extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_id", nullable = false)
    private Airport origin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id", nullable = false)
    private Airport destination;

    @Column(name="departureTime", nullable = false)
    private LocalTime departureTime;

    @Column(name="shipmentDateTime", nullable = false)
    private LocalDate shipmentDateTime;

    @Column(name="packageId", nullable = false)
    private String packageId;

    @Column(name="quantity", nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignedFlightId", nullable = false)
    private Flight assignedFlight;

    @Column(name="tiempoTotal", nullable = false)
    private double tiempoTotal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAeropuerto")
    private Airport airport;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEstadoPaquete")
    private EstadoPaquete estadoPaquete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    @JsonBackReference
    private Shipment shipment;

    /*TABLA INTERMEDIA
    @OneToMany(mappedBy = "paquete", fetch = FetchType.LAZY)
    private List<PackageFlight> packageFlights;*/

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Paquete{" +
                "id=" + getId() +
                ", origin=" + (origin != null ? origin.getCodigoIATA() : "null") +
                ", destination=" + (destination != null ? destination.getCodigoIATA() : "null") +
                ", departureTime=" + departureTime +
                ", shipmentDateTime=" + shipmentDateTime +
                ", packageId='" + packageId + '\'' +
                ", quantity=" + quantity +
                ", assignedFlight=" + (assignedFlight != null ? assignedFlight.getId() : "null") +
                ", tiempoTotal=" + tiempoTotal +
                ", airport=" + (airport != null ? airport.getCodigoIATA() : "null") +
                ", estadoPaquete=" + (estadoPaquete != null ? estadoPaquete.getId() : "null") +
                ", shipment=" + (shipment != null ? shipment.getId() : "null") +
                '}';
    }
}

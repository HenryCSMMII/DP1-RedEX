package com.edu.pucp.dp1.redex.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "flight")
@SQLDelete(sql = "UPDATE flight SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Flight extends BaseEntity {

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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="idEstadoVuelo")
    private EstadoVuelo estadoVuelo;

    // Relaciones con otras entidades va en paquetes
    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "id_paquete")
    // private Flight flight;

    // Constructor adicional
    public Flight(String origin, String destination, LocalTime departureTime, LocalTime arrivalTime, int capacity, String flightNumber, int currentLoad, int duration) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.capacity = capacity;
        this.flightNumber = flightNumber;
        this.currentLoad = currentLoad;
        this.duration = duration;
    }

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Flight{" +
                "id=" + getId() +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", capacity=" + capacity +
                ", flightNumber='" + flightNumber + '\'' +
                ", currentLoad=" + currentLoad +
                ", duration=" + duration +
                '}';
    }
}

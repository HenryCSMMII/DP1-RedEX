package com.edu.pucp.dp1.redex.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.edu.pucp.dp1.redex.model.Algorithm.Parameters;
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
@AllArgsConstructor
@Getter @Setter
public class Flight extends BaseEntity {

    // Atributos estáticos
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final LocalDate BASE_DATE = LocalDate.of(2024, 1, 1);  // Fecha base arbitraria

    // Atributos de instancia
    private int id;

    private static int increment = 0;

    

    //@Column(name = "origin", nullable = false)
    private Airport origin;

    //@Column(name = "destination", nullable = false)
    private Airport destination;

    @Column(name = "departureTime", nullable = false)
    private LocalTime departureTime;

    @Column(name = "arrivalTime", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "flightNumber", nullable = false)
    private String flightNumber;

    @Column(name = "currentLoad", nullable = false)
    private int[] currentLoad;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "arrivalDate", nullable = false)
    private LocalDate arrivalDate;

    @Column(name = "departureDate", nullable = false)
    private LocalDate departureDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_vuelo_id", nullable = false)
    private EstadoVuelo estadoVuelo;

    private long estimated_time;
    
	private long difference_system;//diferencia de peru con aeropuerto de salida

    public Flight() {
		this.currentLoad = new int[Parameters.POPULATION_NUM_INDIVIDUALS*2];
		this.id = increment + 1; 
		Flight.increment += 1;
		
		for(int i=0;i<currentLoad.length;i++) {
			currentLoad[i] = 0;
		}
	}

    public Flight(Flight flight) {
		this.id = flight.getId();
		//this.code = flight.getCode();
		this.capacity = flight.getCapacity();
        this.departureDate = flight.getDepartureDate();
        this.departureTime = flight.getDepartureTime();
        this.arrivalDate = flight.getArrivalDate();
        this.arrivalTime = flight.getArrivalTime();
        if(flight.getOrigin()!=null) this.origin = new Airport(flight.getOrigin());
		if(flight.getDestination()!=null)this.destination = new Airport(flight.getDestination());


		if(flight.getCurrentLoad()!=null) {
			this.currentLoad = new int[Parameters.POPULATION_NUM_INDIVIDUALS*2];
			for(int i=0;i<flight.getCurrentLoad().length;i++) {
				this.currentLoad[i] = flight.getCurrentLoad()[i];
			}
		}
		this.estimated_time = flight.getEstimated_time();
		this.difference_system = flight.getDifference_system();
	}

    public void addSecondsToArrivalTime(long secToAdd) {
        arrivalTime = arrivalTime.plusSeconds(secToAdd);

        if (arrivalTime.toSecondOfDay() >= 86400) {
            int nDays = arrivalTime.toSecondOfDay() / 86400;
            arrivalTime = arrivalTime.minusHours(24 * nDays);
            arrivalDate = arrivalDate.plusDays(nDays);
        }
    }

    public void addSecondsToDepartureTime(long secToAdd) {
        departureTime = departureTime.plusSeconds(secToAdd);

        if (departureTime.toSecondOfDay() >= 86400) {
            int nDays = departureTime.toSecondOfDay() / 86400;
            departureTime = departureTime.minusHours(24 * nDays);
            departureDate = departureDate.plusDays(nDays);
        }
    }

    /* Constructor adicional
    public Flight(String origin, String destination, LocalTime departureTime, LocalTime arrivalTime, int capacity, String flightNumber, int currentLoad, int duration, EstadoVuelo estadoVuelo, LocalDate arrivalDate, LocalDate departureDate) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.capacity = capacity;
        this.flightNumber = flightNumber;
        this.currentLoad = currentLoad;
        this.duration = duration;
        this.estadoVuelo = estadoVuelo;
        this.arrivalDate = arrivalDate; 
        this.departureDate = departureDate;
    }*/

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
                ", estadoVuelo=" + (estadoVuelo != null ? estadoVuelo.getEstado() : "null") +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                '}';
    }
}

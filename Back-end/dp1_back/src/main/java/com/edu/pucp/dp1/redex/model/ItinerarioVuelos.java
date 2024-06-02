package com.edu.pucp.dp1.redex.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "itinerario_vuelo")
@SQLDelete(sql = "UPDATE itinerario_vuelo SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ItinerarioVuelos {

    private int id;
    
    private long estimated_time;

    private Boolean active;
	
	private List<Flight> flights;

    public ItinerarioVuelos(ItinerarioVuelos itinerarioVuelos) {
		if(itinerarioVuelos.getFlights()!=null) this.flights = new ArrayList<Flight>(itinerarioVuelos.getFlights());
		this.estimated_time = itinerarioVuelos.getEstimated_time();
		this.active = itinerarioVuelos.active;
	}

}

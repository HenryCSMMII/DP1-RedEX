package com.edu.pucp.dp1.redex.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.utils.TimeZoneAirport;

public class Flight {
    
	private int id;
	private static int increment = 0;
	private String code;
	private int max_capacity;
	private int[] used_capacity;
	private Date departure_date_time;
	private Date arrival_date_time;
	private Airport departure_airport;
	private Airport arrival_airport;
	private long estimated_time;
	private long difference_system; // diferencia de peru con aeropuerto de salida
	private List<Shipment> shipments;
	private FlightState state;
	private Date salida;
	private Date llegada;

	public List<Shipment> getShipments() {
		return shipments;
	}

	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}

	public FlightState getState() {
		return state;
	}

	public void setState(FlightState state) {
		this.state = state;
	}

	public Flight() {
		this.used_capacity = new int[BD.POPULATION_NUM_INDIVIDUALS * 2];
		this.id = increment + 1; 
		Flight.increment += 1;
		this.state = FlightState.En_Espera;
		for (int i = 0; i < used_capacity.length; i++) {
			used_capacity[i] = 0;
		}
	}
	
	public Flight(int id, String code, int max_capacity, int[] used_capacity, Date departure_date_time,
			Date arrival_date_time, Airport departure_airport, Airport arrival_airport, long estimated_time,
			Date salida, Date llegada) {
		super();
		this.id = id;
		this.code = code;
		this.max_capacity = max_capacity;
		this.used_capacity = used_capacity;
		this.departure_date_time = departure_date_time;
		this.arrival_date_time = arrival_date_time;
		this.departure_airport = departure_airport;
		this.arrival_airport = arrival_airport;
		this.state = FlightState.En_Espera;
		this.salida = salida;
		this.llegada = llegada;
	}
	
	public Flight(Flight flight) {
		this.id = flight.getId();
		this.code = flight.getCode();
		this.max_capacity = flight.getMax_capacity();
		if (flight.getDeparture_date_time() != null) this.departure_date_time = new Date(flight.getDeparture_date_time().getTime());
		if (flight.getArrival_date_time() != null) this.arrival_date_time = new Date(flight.getArrival_date_time().getTime());
		if (flight.getSalida() != null) this.salida = new Date(flight.getSalida().getTime());
		if (flight.getLlegada() != null) this.llegada = new Date(flight.getLlegada().getTime());
		if (flight.getDeparture_airport() != null) this.departure_airport = new Airport(flight.getDeparture_airport());
		if (flight.getArrival_airport() != null) this.arrival_airport = new Airport(flight.getArrival_airport());
		if (flight.getUsed_capacity() != null) {
			this.used_capacity = new int[BD.POPULATION_NUM_INDIVIDUALS * 2];
			for (int i = 0; i < flight.getUsed_capacity().length; i++) {
				this.used_capacity[i] = flight.getUsed_capacity()[i];
			}
		}
		this.estimated_time = flight.getEstimated_time();
		this.difference_system = flight.getDifference_system();
		this.state = flight.getState();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public int getMax_capacity() {
		return max_capacity;
	}
	
	public void setMax_capacity(int max_capacity) {
		this.max_capacity = max_capacity;
	}
	
	public int[] getUsed_capacity() {
		return used_capacity;
	}

	public int getUsed_capacityAlgorithm() {
		return used_capacity[0];
	}

	public void setUsed_capacity(int[] used_capacity) {
		this.used_capacity = used_capacity;
	}
	
	public Date getDeparture_date_time() {
		return departure_date_time;
	}
	
	public void setDeparture_date_time(Date departure_date_time) {
		this.departure_date_time = departure_date_time;
	}
	
	public Date getArrival_date_time() {
		return arrival_date_time;
	}

	public void setSalida(Date salida) {
		this.salida = salida;
	}
	
	public Date getSalida() {
		return salida;
	}

	public void setLlegada(Date llegada) {
		this.llegada = llegada;
	}
	
	public Date getLlegada() {
		return llegada;
	}		
	
	public void setArrival_date_time(Date arrival_date_time) {
		this.arrival_date_time = arrival_date_time;
	}
	
	public Airport getDeparture_airport() {
		return departure_airport;
	}
	
	public void setDeparture_airport(Airport departure_airport) {
		this.departure_airport = departure_airport;
	}
	
	public Airport getArrival_airport() {
		return arrival_airport;
	}
	
	public void setArrival_airport(Airport arrival_airport) {
		this.arrival_airport = arrival_airport;
	}
	
	public void setEstimated_time(long estimated_time) {
		this.estimated_time = estimated_time;
	}
	
	public long getEstimated_time() {
		return estimated_time;
	}

	public void calcEstimatedTime() {
		this.estimated_time = arrival_date_time.getTime() - departure_date_time.getTime() + TimeZoneAirport.calc_difference(arrival_airport, departure_airport) * 60 * 1000;
	}
	
	public long getDifference_system() {
		return difference_system;
	}

	public void setDifference_system(long difference_system) {
		this.difference_system = difference_system;
	}

	 @Override
    public boolean equals(Object obj) {
        // Verifica si 'this' es igual a 'obj'
        if (this == obj) {
            return true;
        }
        // Verifica si 'obj' es nulo o si las clases son diferentes
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        // Realiza un casting de 'obj' a 'Flight'
        Flight flight = (Flight) obj;
        // Compara los campos relevantes (id y departure_date_time)
        return this.id == flight.id &&
               this.departure_date_time.equals(flight.departure_date_time);
    }

    @Override
    public int hashCode() {
        // Utiliza un método de utilidad para calcular el hash
        return Objects.hash(id, departure_date_time);
    }

}

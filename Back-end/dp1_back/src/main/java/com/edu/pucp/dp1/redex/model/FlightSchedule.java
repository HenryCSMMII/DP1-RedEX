package com.edu.pucp.dp1.redex.model;

import java.util.ArrayList;
import java.util.List;

public class FlightSchedule {
    private int id;
	private long estimated_time;
	private Boolean active;
	
	private List<Flight> flights;
	
	public FlightSchedule() {
		this.flights = new ArrayList<Flight>();
		this.estimated_time = 0;
		this.active = true;
	}

	public FlightSchedule(int id, int estimated_time, Boolean active, List<Flight> flights) {
		super();
		this.id = id;
		this.estimated_time = estimated_time;
		this.active = active;
		this.flights = flights;
	}
	
	public FlightSchedule(FlightSchedule flightSche) {
		if(flightSche.getFlights()!=null) this.flights = new ArrayList<Flight>(flightSche.getFlights());
		this.estimated_time = flightSche.getEstimated_time();
		this.active = flightSche.active;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getEstimated_time() {
		return estimated_time;
	}

	public void setEstimated_time(long estimated_time) {
		this.estimated_time = estimated_time;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

}

package com.edu.pucp.dp1.redex.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Shipment {
    private Integer id;
	private String code;
	private Date registerDateTime;
	private Date departureTime;
	private Date arrivalTime;
	private int packageQuantity;
	private Airport departureAirport;
	private Airport arrivalAirport;
	private FlightSchedule flightSchedule;
	private Client client;
	private Operator operator;
	private State state;
	
	public Shipment() {

	}
	
	public Shipment(Shipment shipment) {
		this.id = shipment.getId();
		this.code = shipment.getCode();
		if(shipment.getRegisterDateTime() != null) this.registerDateTime = new Date(shipment.getRegisterDateTime().getTime());
		if(shipment.getDepartureTime() != null) this.departureTime = new Date(shipment.getDepartureTime().getTime());
		if(shipment.getArrivalTime() != null)	this.arrivalTime = new Date(shipment.getArrivalTime().getTime());
		if(shipment.getClient() != null)	this.client = new Client(shipment.getClient());
		this.operator = shipment.getOperator();
		this.state = shipment.state;
		this.packageQuantity = shipment.getPackageQuantity();
		if(shipment.getDepartureAirport() != null){
			this.departureAirport = new Airport(shipment.getDepartureAirport());
		}
		
		if(shipment.getArrivalAirport() != null){
			this.arrivalAirport = new Airport(shipment.getArrivalAirport());
		}
		//this.flightSchedule = new FlightSchedule(shipment.getFlightSchedule());
    }

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
	public void setCode(String code) {
		this.code = code;
	}
    
    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }
    
    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public int getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(int packageQuantity) {
        this.packageQuantity = packageQuantity;
    }
    
    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDeparturAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }
    
    
    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
    
    
    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }
    
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

	public Date getRegisterDateTime() {
		return registerDateTime;
	}

	public void setRegisterDateTime(Date registerDateTime) {
		this.registerDateTime = registerDateTime;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}
    
    public Shipment(Integer id,	String code, Date departureTime, Date arrivalTime, Integer packageQuantity, 
    				Airport departureAirport, Airport arrivalAirport, FlightSchedule flightSchedule, State state,
    				Client client, Operator operator) {
        this.id = id;
        this.code = code;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.packageQuantity = packageQuantity;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.flightSchedule = flightSchedule;
        this.state = state;
        this.client = client;
        this.operator = operator;
    }
    
    public long calc_max_possible_time() {
    	
    	long max_time = 0;
    	
    	if(this.departureAirport.getCountry().getContinent().getAbbrev().equals(this.arrivalAirport.getCountry().getContinent().getAbbrev())){ //Es el mismo continente
    		max_time=24*60*60*1000;
    	}
    	else {
    		max_time=48*60*60*1000;
            System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
    	}
    	
    	LocalDateTime dateOfInterest = LocalDateTime.now();
        System.out.println("ARRIVAL: " + this.arrivalAirport.getTime_zone());
        System.out.println(this.arrivalAirport.getName() + " " + this.arrivalAirport.getCode());
        System.out.println("DEPARTURE: " + this.getDepartureAirport().getTime_zone());
        System.out.println(this.getDepartureAirport().getName() + " " + this.getDepartureAirport().getCode());
        
    	long difference = ChronoUnit.MINUTES.between(dateOfInterest.atZone(ZoneId.of(this.arrivalAirport.getTime_zone())),dateOfInterest.atZone(ZoneId.of(this.getDepartureAirport().getTime_zone())));

    	// max_time -= difference*60*1000;
    	
        
    	return max_time; 
    }
    
    public long calc_max_additional_days() {
    	
    	long max_time = 0;
    	
    	if(this.departureAirport.getCountry().getContinent().getAbbrev().equals(this.arrivalAirport.getCountry().getContinent().getAbbrev())){ //Es el mismo continente
    		max_time=24*60*60*1000;
    	}
    	else {
    		max_time=48*60*60*1000;
    	}
    	
    	return max_time; 
    }

}

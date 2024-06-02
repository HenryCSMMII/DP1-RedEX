package com.edu.pucp.dp1.redex.model;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class Airport {

    private int id;
	private static int increment = 0;
    //private String name;
    private String codigoIATA;
	private String latitude;
	private String longitude;
	private int max_capacity;
    private int currentCapacity;
	private String time_zone;
	private Country country;
    
    public Airport() {
		this.id=increment+1; 
		this.increment +=1;
	}
	
	public Airport(int id, /*String name,*/ String codigoIATA, String latitude, String longitude, int max_capacity,
			int currentCapacity, String time_zone, Country country) {
		super();
		this.id = id;
		//this.name = name;
		this.codigoIATA = codigoIATA;
		this.latitude = latitude;
		this.longitude = longitude;
		this.max_capacity = max_capacity;
		this.currentCapacity = currentCapacity;
		this.time_zone = time_zone;
		this.country = country;
	}
	
	public Airport(Airport airport) {
		this.id = airport.getId();
		//this.name = airport.getName();
		this.codigoIATA = airport.getCode();
		this.latitude = airport.getLatitude();
		this.longitude = airport.getLongitude();
		this.max_capacity = airport.getMax_capacity();
		this.currentCapacity = airport.getCurrentCapacity();
		
		/*if(airport.getStorage() != null) {
			for(int i=0;i<airport.getStorage().size();i++) {
				this.storage.add(i, new StorageCapacity(airport.getStorage().get(i)));
			}
		}*/
		this.time_zone = airport.getTime_zone();
		if(airport.getCountry()!=null) this.country = new Country(airport.getCountry());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	// public String getName() {
	// 	return name;
	// }
	// public void setName(String name) {
	// 	this.name = name;
	// }
	
	public String getCode() {
		return codigoIATA;
	}
	public void setCode(String code) {
		this.codigoIATA = code;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public int getMax_capacity() {
		return max_capacity;
	}

	public void setMax_capacity(int max_capacity) {
		this.max_capacity = max_capacity;
	}
	
	public int getCurrentCapacity() {
		return currentCapacity;
	}
	public void setCurrentCapacity(int currentCapacity) {
		this.currentCapacity = currentCapacity;
	}
    
	public String getTime_zone() {
		return time_zone;
	}
	
	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}    
}

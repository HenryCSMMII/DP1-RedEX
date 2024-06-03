package com.edu.pucp.dp1.redex.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.FlightSchedule;

public class TimeZoneAirport {
    public static long calc_difference(Airport airport_1, Airport airport_2) {
		LocalDateTime dateOfInterest = LocalDateTime.now();
        System.out.println("Airport 1: "+ airport_1.getTime_zone());
        System.out.println(ZoneId.of(airport_1.getTime_zone()));
        System.out.println(ZoneId.of(airport_2.getTime_zone()));
    	long difference = ChronoUnit.MINUTES.between(dateOfInterest.atZone(ZoneId.of(airport_1.getTime_zone())),dateOfInterest.atZone(ZoneId.of(airport_2.getTime_zone())));
    	System.out.println("DIFFERENCE: "+difference);
    	return difference;
	}
	
	public static long calc_wait_time(FlightSchedule flight_schedule) {
		
		if(flight_schedule.getFlights().size() == 1) {
			return 0; //Primer vuelo, no hay tiempo de espera
		}
		else {
			return flight_schedule.getFlights().get(flight_schedule.getFlights().size()-2).getDeparture_date_time().getTime() - flight_schedule.getFlights().get(flight_schedule.getFlights().size()-1).getArrival_date_time().getTime();
		}
	}
}

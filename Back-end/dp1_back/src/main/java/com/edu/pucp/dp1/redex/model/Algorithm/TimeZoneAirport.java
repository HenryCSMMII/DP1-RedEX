package com.edu.pucp.dp1.redex.model.Algorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.ItinerarioVuelos;


public class TimeZoneAirport {

	public static long calc_difference(Airport airport_1, Airport airport_2) {
		LocalDateTime dateOfInterest = LocalDateTime.now();
    	//long difference = ChronoUnit.MINUTES.between(dateOfInterest.atZone(ZoneId.of(airport_1.getTime_zone())),dateOfInterest.atZone(ZoneId.of(airport_2.getTime_zone())));
    	long difference = ChronoUnit.MINUTES.between(dateOfInterest.atOffset(ZoneOffset.ofHours(airport_1.getCity().getZonahoraria())).toZonedDateTime(), dateOfInterest.atOffset(ZoneOffset.ofHours(airport_2.getCity().getZonahoraria())).toZonedDateTime());
    	return difference;
	}
	
	public static long calc_wait_time(ItinerarioVuelos flight_schedule) {
		
		if(flight_schedule.getFlights().size() == 1) {
			return 0; //Primer vuelo, no hay tiempo de espera
		}
		else {
			return (flight_schedule.getFlights().get(flight_schedule.getFlights().size()-2).getDepartureTime().toNanoOfDay() - flight_schedule.getFlights().get(flight_schedule.getFlights().size()-1).getArrivalTime().toNanoOfDay())/1000000;
		}
	}
	

}

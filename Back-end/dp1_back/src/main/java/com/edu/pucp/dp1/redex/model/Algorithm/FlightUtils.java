package com.edu.pucp.dp1.redex.model.Algorithm;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FlightUtils {

    private static final Map<String, String> airportToContinent = new HashMap<>();

    // Método para establecer los datos del aeropuerto
    public static void setAirportToContinentData(Map<String, String> airportData) {
        airportToContinent.putAll(airportData);
    }

    public static boolean isSameContinent(String origin, String destination) {
        String originContinent = airportToContinent.get(origin);
        String destinationContinent = airportToContinent.get(destination);
        return originContinent != null && destinationContinent != null && originContinent.equals(destinationContinent);
    }
    public static LocalTime getDeadlineConsideringContinent(String origin, String destination, LocalTime departureTime, Map<String, String> airportToContinentData) {
        boolean sameContinent = isSameContinent(origin, destination);
        long maxDurationMinutes;
        if (sameContinent) {
            maxDurationMinutes = TimeUnit.HOURS.toMinutes(24); // Mismo continente: 24 horas
        } else {
            maxDurationMinutes = TimeUnit.DAYS.toMinutes(2); // Distinto continente: 2 días
        }
        return departureTime.plusMinutes(maxDurationMinutes);
    }

    // Método para calcular el tiempo máximo de entrega basado en continentes
    public static LocalTime getDeadline(String origin, String destination, LocalTime departureTime) {
        boolean sameContinent = isSameContinent(origin, destination);
        long maxDurationMinutes = sameContinent ? TimeUnit.HOURS.toMinutes(24) : TimeUnit.DAYS.toMinutes(2);
        return departureTime.plusMinutes(maxDurationMinutes);
    }

    private static long calculateFlightDuration(Flight flight) {
        LocalTime departureTime = flight.getDepartureTime();
        LocalTime arrivalTime = flight.getArrivalTime();
        long duration = Duration.between(departureTime, arrivalTime).toMinutes();
        if (duration < 0) {
            duration += 1440; // Agrega 24 horas en minutos si el tiempo es negativo
        }
        return duration;
    }
    private static long calculateRouteDurationMinutes(List<Flight> flightRoute) {
        long totalDuration = 0;
        for (Flight flight : flightRoute) {
            totalDuration += calculateFlightDuration(flight);
        }
        return totalDuration;
    }

}

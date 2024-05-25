package com.edu.pucp.dp1.redex.services;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.repository.FlightRepository;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Service
public class FlightService{

    @Autowired
    private FlightRepository flightRepository;

    private static final Logger LOGGER = (Logger) LoggerFactory.logger(FlightService.class);
    @PersistenceContext
    EntityManager entityManager;
    
    public Flight register(Flight flight){
        try {
            return flightRepository.save(flight);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Flight> getAll(){
        try {
            return flightRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Flight get(int id){
        try {
            return flightRepository.findFlightById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Flight update(Flight flight){
        try {
            return flightRepository.save(flight);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            flightRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void calculateDuration(Flight flight) {
        int duration;
        if (flight.getArrivalTime().isBefore(flight.getDepartureTime())) {
            // El vuelo pasa de medianoche
            duration = (int) ChronoUnit.MINUTES.between(flight.getDepartureTime(), LocalTime.MAX) + 1
                    + (int) ChronoUnit.MINUTES.between(LocalTime.MIN, flight.getArrivalTime());
        } else {
            // Vuelo dentro del mismo día
            duration = (int) ChronoUnit.MINUTES.between(flight.getDepartureTime(), flight.getArrivalTime());
        }
        flight.setDuration(duration);
    }

    private static LocalTime parseTime(String timeStr, Flight flight) {
        if (timeStr == null || timeStr.isEmpty()) {
            System.err.println("Hora nula o vacía proporcionada, no se puede parsear.");
            return null;
        }
        try {
            return LocalTime.parse(timeStr, flight.TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la hora: " + timeStr);
            return null;
        }
    }

    public String toString(Flight flight) {
        return "Vuelo " + flight.getOrigin() + " -> " + flight.getDestination() + " (" + flight.getDepartureTime() + " - " +
        flight.getArrivalTime() + ") - Capacidad: " + flight.getCapacity();
    }    
}
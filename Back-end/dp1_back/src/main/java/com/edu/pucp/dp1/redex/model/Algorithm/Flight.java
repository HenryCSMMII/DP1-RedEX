package com.edu.pucp.dp1.redex.model.Algorithm;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Flight {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final LocalDate BASE_DATE = LocalDate.of(2024, 1, 1);  // Fecha base arbitraria
    private String origin;
    private String destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int capacity;
    private String flightNumber;
    private int currentLoad;
    private int duration;

    private ArrayList<Package> packages;

    public void setDuration(int duration) {
        this.duration = duration;
    }


    // Constructor de la clase Flight
    public Flight( String origin, String destination, String departureTime, String arrivalTime, int capacity) {

        this.origin = origin;
        this.destination = destination;
        this.departureTime = parseTime(departureTime);
        this.arrivalTime = parseTime(arrivalTime);
        this.capacity = capacity;

        this.currentLoad = 0; // Inicializar con 0 o ajustar según sea necesario
    }

    public void calculateDuration() {
        if (arrivalTime.isBefore(departureTime)) {
            // El vuelo pasa de medianoche
            duration = (int) ChronoUnit.MINUTES.between(departureTime, LocalTime.MAX) + 1
                    + (int) ChronoUnit.MINUTES.between(LocalTime.MIN, arrivalTime);
        } else {
            // Vuelo dentro del mismo día
            duration = (int) ChronoUnit.MINUTES.between(departureTime, arrivalTime);
        }
    }
    // Métodos getter y setter


    public ArrayList<Package> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<Package> packages) {
        this.packages = packages;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    private static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            System.err.println("Hora nula o vacía proporcionada, no se puede parsear.");
            return null;
        }
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la hora: " + timeStr);
            return null;
        }
    }


    public int getDuration() {
        return duration;
    }

}

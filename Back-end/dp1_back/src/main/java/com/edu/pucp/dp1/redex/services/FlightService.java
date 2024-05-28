package com.edu.pucp.dp1.redex.services;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pucp.dp1.redex.dto.FlightDTO;
import com.edu.pucp.dp1.redex.model.EstadoVuelo;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.repository.EstadoVueloRepository;
import com.edu.pucp.dp1.redex.repository.FlightRepository;

@Service
public class FlightService{

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private EstadoVueloRepository estadoVueloRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

    public void registerListFlight(List<Flight> flights){
        try {
            for (Flight flight : flights) {
                flightRepository.save(flight);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public FlightDTO parseFlightFromLine(String line) {
        String[] parts = line.split("-");
        String origin = parts[0];
        String destination = parts[1];
        LocalTime departureTime = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime arrivalTime = LocalTime.parse(parts[3], DateTimeFormatter.ofPattern("HH:mm"));
        int capacity = Integer.parseInt(parts[4]);
        EstadoVuelo estadoVuelo = new EstadoVuelo();
        estadoVuelo.setId(1);
        estadoVuelo.setEstado("PENDIENTE");

        Flight flight = new Flight(origin, destination, departureTime, arrivalTime, capacity, "0", 0, 0, estadoVuelo);
        return convertToDTO(flight);
    }

    public List<FlightDTO> listarVuelosPorEstado(int idEstado){
        try {
            EstadoVuelo estado = estadoVueloRepository.findEstadoVueloById(idEstado);
            return flightRepository.findFlightByEstadoVuelo(estado).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<FlightDTO> listFlightByIds(int idInicio, int idFinal){
        try{
            return flightRepository.findFlightByIds(idInicio, idFinal).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public FlightDTO register(FlightDTO flightDTO){
        try {
            Flight flight = convertToEntity(flightDTO);
            flight = flightRepository.save(flight);
            return convertToDTO(flight);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<FlightDTO> getAll(){
        try {
            return flightRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public FlightDTO get(int id){
        try {
            Flight flight = flightRepository.findFlightById(id);
            return flight != null ? convertToDTO(flight) : null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public FlightDTO update(FlightDTO flightDTO){
        try {
            Flight flight = convertToEntity(flightDTO);
            flight = flightRepository.save(flight);
            return convertToDTO(flight);
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

    private FlightDTO convertToDTO(Flight flight) {
        return new FlightDTO(
                flight.getId(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getCapacity(),
                flight.getFlightNumber(),
                flight.getCurrentLoad(),
                flight.getDuration(),
                flight.getEstadoVuelo().getId()
        );
    }

    private Flight convertToEntity(FlightDTO flightDTO) {
        Flight flight = new Flight();
        flight.setId(flightDTO.getId());
        flight.setOrigin(flightDTO.getOrigin());
        flight.setDestination(flightDTO.getDestination());
        flight.setDepartureTime(flightDTO.getDepartureTime());
        flight.setArrivalTime(flightDTO.getArrivalTime());
        flight.setCapacity(flightDTO.getCapacity());
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setCurrentLoad(flightDTO.getCurrentLoad());
        flight.setDuration(flightDTO.getDuration());
        EstadoVuelo estadoVuelo = estadoVueloRepository.findEstadoVueloById(flightDTO.getEstadoVueloId());
        flight.setEstadoVuelo(estadoVuelo);
        return flight;
    }
}

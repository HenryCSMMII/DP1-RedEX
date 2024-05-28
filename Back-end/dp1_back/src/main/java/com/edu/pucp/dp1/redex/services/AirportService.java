package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.dto.AirportDTO;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.City;
import com.edu.pucp.dp1.redex.repository.AirportRepository;
import com.edu.pucp.dp1.redex.repository.CityRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private CityRepository cityRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AirportService.class);

    @Transactional
    public AirportDTO register(AirportDTO airportDTO){
        try {
            Airport airport = convertToEntity(airportDTO);
            Airport savedAirport = airportRepository.save(airport);
            return convertToDTO(savedAirport);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public List<AirportDTO> getAll(){
        try {
            return airportRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public AirportDTO get(int id){
        try {
            Airport airport = airportRepository.findAirportById(id);
            return convertToDTO(airport);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public AirportDTO update(AirportDTO airportDTO){
        try {
            Airport airport = convertToEntity(airportDTO);
            Airport updatedAirport = airportRepository.save(airport);
            return convertToDTO(updatedAirport);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void delete(int id){
        try {
            airportRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<AirportDTO> listAirportsByIds(int idInicio, int idFinal){
        try{
            List<Airport> airports = airportRepository.findAirportByIds(idInicio, idFinal);
            return airports.stream().map(this::convertToDTO).collect(Collectors.toList());
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    private AirportDTO convertToDTO(Airport airport) {
        return new AirportDTO(
                airport.getId(),
                airport.getCodigoIATA(),
                airport.getLatitude(),
                airport.getLongitude(),
                airport.getCapacity(),
                airport.getCity().getId() // Solo el ID de la ciudad
        );
    }

    private Airport convertToEntity(AirportDTO airportDTO) {
        City city = cityRepository.findById(airportDTO.getCityId())
                                  .orElseThrow(() -> new RuntimeException("City not found"));
        return new Airport(
                airportDTO.getCodigoIATA(),
                city,
                airportDTO.getLatitude(),
                airportDTO.getLongitude(),
                airportDTO.getCapacity()
        );
    }
}

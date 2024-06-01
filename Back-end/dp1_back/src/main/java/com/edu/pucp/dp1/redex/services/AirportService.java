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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public AirportDTO getByCode(String code){
        try {
            Airport airport = airportRepository.findAirportByCodigoIATA(code);
            return convertToDTO(airport);
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

    // public AirportDTO getByCityId(int cityId){
    //     try {
    //         Airport airport = airportRepository.findAirportByCityId(cityId);
    //         return convertToDTO(airport);
    //     } catch (Exception e) {
    //         LOGGER.error(e.getMessage(), e);
    //         return null;
    //     }
    // }

    @Transactional
    public void processFile(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length >= 8) {
                    String codigoIATA = fields[0];
                    int capacity = Integer.parseInt(fields[4]);
                    double latitude = Double.parseDouble(fields[6]);
                    double longitude = Double.parseDouble(fields[7]);
                    // Assuming city ID is provided for now, you need to map it correctly
                    int cityId = 1; // Replace with actual logic to determine cityId
                    AirportDTO airportDTO = new AirportDTO(0, codigoIATA, latitude, longitude, capacity, 0, cityId);
                    register(airportDTO);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error processing file", e);
            throw e;
        }
    }

    private AirportDTO convertToDTO(Airport airport) {
        return new AirportDTO(
                airport.getId(),
                airport.getCodigoIATA(),
                airport.getLatitude(),
                airport.getLongitude(),
                airport.getCapacity(),
                airport.getCurrentCapacity(),
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
                airportDTO.getCapacity(),
                airportDTO.getCurrentCapacity()
        );
    }
}

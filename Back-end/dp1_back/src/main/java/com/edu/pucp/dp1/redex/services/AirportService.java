package com.edu.pucp.dp1.redex.services;


import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.repository.AirportRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AirportService.class);

    public Airport register(Airport airport){
        try {
            return airportRepository.save(airport);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Airport> getAll(){
        try {
            return airportRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Airport get(int id){
        try {
            return airportRepository.findAirportById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Airport update(Airport airport){
        try {
            return airportRepository.save(airport);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            airportRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public List<Airport> listAirportsByIds(int idInicio, int idFinal){
        try{
            List<Airport> aeropuertos = airportRepository.findAirportByIds(idInicio, idFinal);
            return aeropuertos;
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

}

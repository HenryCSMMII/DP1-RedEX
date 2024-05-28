package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.model.City;
import com.edu.pucp.dp1.redex.repository.CityRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);

    public City register(City city){
        try {
            return cityRepository.save(city);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<City> getAll(){
        try {
            return cityRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public City get(int id){
        try {
            return cityRepository.findCityById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public City update(City city){
        try {
            return cityRepository.save(city);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            cityRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public List<City> listCityByIds(int idInicio, int idFinal){
        try{
            List<City> cities = cityRepository.findCityByIds(idInicio, idFinal);
            return cities;
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }    
}

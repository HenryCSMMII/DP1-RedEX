package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.model.Continent;
import com.edu.pucp.dp1.redex.model.Country;
import com.edu.pucp.dp1.redex.repository.CountryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    public Country register(Country country){
        try {
            return countryRepository.save(country);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Country> getAll(){
        try {
            return countryRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Country get(int id){
        try {
            return countryRepository.findCountryById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Country update(Country country){
        try {
            return countryRepository.save(country);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            countryRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public List<Country> listCountryByIds(int idInicio, int idFinal){
        try{
            List<Country> countries = countryRepository.findCountryByIds(idInicio, idFinal);
            return countries;
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}

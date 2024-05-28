package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.dto.CountryDTO;
import com.edu.pucp.dp1.redex.model.Country;
import com.edu.pucp.dp1.redex.repository.CountryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<CountryDTO> getAll(){
        try {
            List<Country> countries = countryRepository.findAll();
            return countries.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public CountryDTO get(int id){
        try {
            Country country = countryRepository.findCountryById(id);
            return convertToDTO(country);
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

    public List<CountryDTO> listCountryByIds(int idInicio, int idFinal){
        try {
            List<Country> countries = countryRepository.findCountryByIds(idInicio, idFinal);
            return countries.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    private CountryDTO convertToDTO(Country country) {
        return new CountryDTO(country.getId(), country.getName(), country.getShortname(), country.getContinent().getId());
    }
}

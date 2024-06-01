package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.dto.CityDTO;
import com.edu.pucp.dp1.redex.model.City;
import com.edu.pucp.dp1.redex.repository.CityRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);

    public CityDTO register(CityDTO cityDTO){
        try {
            City city = convertToEntity(cityDTO);
            city = cityRepository.save(city);
            return convertToDTO(city);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public List<CityDTO> getAll(){
        try {
            return cityRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public CityDTO get(int id){
        try {
            City city = cityRepository.findCityById(id);
            return city != null ? convertToDTO(city) : null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public CityDTO update(CityDTO cityDTO){
        try {
            City city = convertToEntity(cityDTO);
            city = cityRepository.save(city);
            return convertToDTO(city);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void delete(int id){
        try {
            cityRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<CityDTO> listCityByIds(int idInicio, int idFinal){
        try {
            return cityRepository.findCityByIds(idInicio, idFinal).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public CityDTO getByName(String name){
        try {
            City city = cityRepository.findCityByName(name);
            return city != null ? convertToDTO(city) : null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private CityDTO convertToDTO(City city) {
        return new CityDTO(
                city.getId(),
                city.getNombre(),
                city.getAbreviatura(),
                city.getZonahoraria(),
                city.getCountry().getId()
        );
    }

    private City convertToEntity(CityDTO cityDTO) {
        City city = new City();
        city.setId(cityDTO.getId());
        city.setNombre(cityDTO.getNombre());
        city.setAbreviatura(cityDTO.getAbreviatura());
        city.setZonahoraria(cityDTO.getZonahoraria());
        // Asumimos que el country ya ha sido cargado de la base de datos
        // y se establece mediante algún mecanismo externo.
        return city;
    }
}

package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.model.City;
import com.edu.pucp.dp1.redex.model.Continent;
import com.edu.pucp.dp1.redex.repository.ContinentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContinentService {

    @Autowired
    private ContinentRepository continentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContinentService.class);

    public Continent register(Continent continent){
        try {
            return continentRepository.save(continent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Continent> getAll(){
        try {
            return continentRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Continent get(int id){
        try {
            return continentRepository.findContinentById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Continent update(Continent continent){
        try {
            return continentRepository.save(continent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            continentRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public List<Continent> listContinentByIds(int idInicio, int idFinal){
        try{
            List<Continent> continents = continentRepository.findContinentByIds(idInicio, idFinal);
            return continents;
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}

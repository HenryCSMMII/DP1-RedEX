package com.edu.pucp.dp1.redex.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pucp.dp1.redex.model.PackageFlight;
import com.edu.pucp.dp1.redex.repository.PackageFlightRepository;

@Service
public class PackageFlightService {
    
    @Autowired
    private PackageFlightRepository packageFlightRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageFlightService.class);

    public PackageFlight register(PackageFlight packageFlight){
        try {
            return packageFlightRepository.save(packageFlight);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<PackageFlight> getAll(){
        try {
            return packageFlightRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public PackageFlight get(int id){
        try {
            return packageFlightRepository.findPackageFlightById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<PackageFlight> getByPaqueteId(int paqueteId){
        try {
            return packageFlightRepository.findByPaqueteId(paqueteId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<PackageFlight> getByFlightId(int flightId){
        try {
            return packageFlightRepository.findByFlightId(flightId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public PackageFlight update(PackageFlight packageFlight){
        try {
            return packageFlightRepository.save(packageFlight);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            packageFlightRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}

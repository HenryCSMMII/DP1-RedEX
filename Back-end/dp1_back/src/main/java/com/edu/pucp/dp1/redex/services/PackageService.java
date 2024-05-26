package com.edu.pucp.dp1.redex.services;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.pucp.dp1.redex.repository.PackageRepository;

@Service
public class PackageService {
    
    @Autowired
    private PackageRepository paqueteRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageService.class);

    public Package register(Package paquete){
        try {
            return paqueteRepository.save(paquete);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<Package> getAll(){
        try {
            return paqueteRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Package get(int id){
        try {
            return paqueteRepository.findPackageById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Package update(Package paquete){
        try {
            return paqueteRepository.save(paquete);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void delete(int id){
        try {
            paqueteRepository.deleteById(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}

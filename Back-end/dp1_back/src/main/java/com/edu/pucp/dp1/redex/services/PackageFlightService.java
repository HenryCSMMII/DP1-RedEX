package com.edu.pucp.dp1.redex.services;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edu.pucp.dp1.redex.model.PackageFlight;
import com.edu.pucp.dp1.redex.repository.PackageFlightRepository;
import com.edu.pucp.dp1.redex.dto.PackageFlightDTO;

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

    public List<PackageFlightDTO> getAll(){
        try {
            List<PackageFlight> packageFlights = packageFlightRepository.findAll();
            return packageFlights.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public PackageFlightDTO get(int id){
        try {
            PackageFlight packageFlight = packageFlightRepository.findPackageFlightById(id);
            return convertToDTO(packageFlight);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<PackageFlightDTO> getByPaqueteId(int paqueteId){
        try {
            List<PackageFlight> packageFlights = packageFlightRepository.findByPaqueteId(paqueteId);
            return packageFlights.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<PackageFlightDTO> getByFlightId(int flightId){
        try {
            List<PackageFlight> packageFlights = packageFlightRepository.findByFlightId(flightId);
            return packageFlights.stream().map(this::convertToDTO).collect(Collectors.toList());
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

    public List<PackageFlightDTO> listFlightByIds(int idInicio, int idFinal){
        try {
            List<PackageFlight> packageFlights = packageFlightRepository.findPackageFlightByIds(idInicio, idFinal);
            return packageFlights.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    } 

    private PackageFlightDTO convertToDTO(PackageFlight packageFlight) {
        return new PackageFlightDTO(
                packageFlight.getId(),
                packageFlight.getPaquete().getId(),
                packageFlight.getFlight().getId(),
                packageFlight.getEstadoTrazabilidad().getId()
        );
    }   
}

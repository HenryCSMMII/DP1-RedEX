package com.edu.pucp.dp1.redex.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.services.AirportService;

@RestController
@RequestMapping("/aeropuerto")
@CrossOrigin
public class AirportController {

    @Autowired
    private AirportService airportService;

    //Trae todos los vuelos
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @GetMapping(value = "/")
    List<Airport> getAll(){
        return airportService.getAll();
    }

    
    //Trae a un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @GetMapping(value = "/{id}")
    Airport get(@PathVariable int id){
        return airportService.get(id);
    }

    //Registra un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @PostMapping(value = "/")
    Airport register(@RequestBody Airport airport) throws SQLException{
         
        // if(flightService.duplicadoPropio(flight.getId())!=null){
        //     throw new SQLException();
        // }
        return airportService.register(airport);
    }

    //Actualiza un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @PutMapping(value = "/")
    Airport update(@RequestBody Airport airport) throws SQLException{
        return airportService.update(airport);
    }
    
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        airportService.delete(id);
    }    

}

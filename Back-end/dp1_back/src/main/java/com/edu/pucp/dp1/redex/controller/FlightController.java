package com.edu.pucp.dp1.redex.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.services.FlightService;

@RestController
@RequestMapping("/vuelo")
@CrossOrigin
public class FlightController {
    
    @Autowired
    private FlightService flightService;

    //Trae todos los vuelos
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @GetMapping(value = "/")
    List<Flight> getAll(){
        return flightService.getAll();
    }

    
    //Trae a un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @GetMapping(value = "/{id}")
    Flight get(@PathVariable int id){
        return flightService.get(id);
    }

    //Registra un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @PostMapping(value = "/")
    Flight register(@RequestBody Flight flight) throws SQLException{
         
        // if(flightService.duplicadoPropio(flight.getId())!=null){
        //     throw new SQLException();
        // }
        return flightService.register(flight);
    }

    @PostMapping(value = "/list", consumes = "multipart/form-data")
    public void registerList(@RequestParam("file") MultipartFile file) {
        List<Flight> flights = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Aquí convertimos cada línea en un objeto Flight
                Flight flight = flightService.parseFlightFromLine(line);
                flights.add(flight);
            }
            flightService.registerListFlight(flights);
            // for (Flight flight : flights) {
            //     System.out.println(flight);
            // }            
        } catch (IOException e) {
            // Manejar la excepción
            e.printStackTrace();
        }
    }

    //Actualiza un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @PutMapping(value = "/")
    Flight update(@RequestBody Flight flight) throws SQLException{
        return flightService.update(flight);
    }
    
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        flightService.delete(id);
    }

}

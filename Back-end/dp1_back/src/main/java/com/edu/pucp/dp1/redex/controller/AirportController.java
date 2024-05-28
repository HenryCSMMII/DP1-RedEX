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

import com.edu.pucp.dp1.redex.dto.AirportDTO;
import com.edu.pucp.dp1.redex.services.AirportService;

@RestController
@RequestMapping("/airport")
@CrossOrigin
public class AirportController {

    @Autowired
    private AirportService airportService;

    @GetMapping(value = "/")
    List<AirportDTO> getAll(){
        return airportService.getAll();
    }

    @GetMapping(value = "/{id}")
    AirportDTO get(@PathVariable int id){
        return airportService.get(id);
    }

    @PostMapping(value = "/")
    AirportDTO register(@RequestBody AirportDTO airportDTO) throws SQLException{
        return airportService.register(airportDTO);
    }

    @PutMapping(value = "/")
    AirportDTO update(@RequestBody AirportDTO airportDTO) throws SQLException{
        return airportService.update(airportDTO);
    }
    
    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        airportService.delete(id);
    }

    @GetMapping(value = "/{idInicio}/{idFinal}")
    List<AirportDTO> listaAirportsPorIds(@PathVariable int idInicio, @PathVariable int idFinal){
        return airportService.listAirportsByIds(idInicio, idFinal);
    }
}

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

import com.edu.pucp.dp1.redex.dto.FlightDTO;
import com.edu.pucp.dp1.redex.services.FlightService;

@RestController
@RequestMapping("/vuelo")
@CrossOrigin
public class FlightController {
    
    @Autowired
    private FlightService flightService;

    @GetMapping(value = "/")
    List<FlightDTO> getAll(){
        return flightService.getAll();
    }

    @GetMapping(value = "/{id}")
    FlightDTO get(@PathVariable int id){
        return flightService.get(id);
    }

    @PostMapping(value = "/")
    FlightDTO register(@RequestBody FlightDTO flightDTO) throws SQLException{
        return flightService.register(flightDTO);
    }

    @PostMapping(value = "/list", consumes = "multipart/form-data")
    public void registerList(@RequestParam("file") MultipartFile file) {
        List<FlightDTO> flights = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                FlightDTO flightDTO = flightService.parseFlightFromLine(line);
                flights.add(flightDTO);
            }
            for (FlightDTO flightDTO : flights) {
                flightService.register(flightDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PutMapping(value = "/")
    FlightDTO update(@RequestBody FlightDTO flightDTO) throws SQLException{
        return flightService.update(flightDTO);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        flightService.delete(id);
    }

    @GetMapping(value = "/listarVuelosPorEstado/{idEstado}")
    List<FlightDTO> listarVuelosPorEstado(@PathVariable int idEstado){
        return flightService.listarVuelosPorEstado(idEstado);
    }

    @GetMapping(value = "/{idInicio}/{idFinal}")
    List<FlightDTO> listarFlightsByIds(@PathVariable int idInicio, @PathVariable int idFinal){
        return flightService.listFlightByIds(idInicio, idFinal);
    }
}

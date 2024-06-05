package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.dto.AirportDTO;
import com.edu.pucp.dp1.redex.model.Continent;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/continent")
public class ContinentController {

    @GetMapping("/")
    public List<Continent> getAllContinents() {
        return BD.continents;
    }

    @GetMapping(value = "/{id}")
    public Continent getContinent(@PathVariable int id){
        return BD.continents.get(id-1);
    }

    @GetMapping("/read")
    public String readContinents() {
        try {
            BD.readContinents();
            return "Continentes leídos exitosamente.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al leer el archivo de continentes.";
        }
    }    
}
package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.dto.CityDTO;
import com.edu.pucp.dp1.redex.model.City;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ciudad")
public class CityController {

    @GetMapping("/")
    public List<CityDTO> getAllCities() {
        return BD.cities.stream()
                .map(city -> new CityDTO(
                        city.getId(),
                        city.getCountryId(),
                        city.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/read")
    public String readCities() {
        try {
            BD.readCities();
            return "Ciudades leídas exitosamente.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al leer el archivo de ciudades.";
        }
    }    
}

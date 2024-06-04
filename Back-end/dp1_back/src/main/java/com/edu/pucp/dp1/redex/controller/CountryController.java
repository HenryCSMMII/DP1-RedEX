package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.dto.CountryDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @GetMapping("/all")
    public List<CountryDTO> getAllCountries() {
        return BD.countries.stream()
                .map(country -> new CountryDTO(
                        country.getId(),
                        country.getName(),
                        country.getAbbrev(),
                        country.getContinent().getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/read")
    public String readCountries() {
        try {
            BD.readCountries();
            return "Países leídos exitosamente.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al leer el archivo de países.";
        }
    }
}

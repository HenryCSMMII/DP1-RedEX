package com.edu.pucp.dp1.redex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.model.Continent;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/continent")
public class ContinentController {

    @GetMapping("/all")
    public List<Continent> getAllContinents() {
        return BD.continents;
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
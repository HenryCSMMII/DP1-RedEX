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

import com.edu.pucp.dp1.redex.model.EstadoTrazabilidad;
import com.edu.pucp.dp1.redex.services.EstadoTrazabilidadService;

@RestController
@RequestMapping("/estadoTrazabilidad")
@CrossOrigin
public class EstadoTrazabilidadController {
    
    @Autowired
    private EstadoTrazabilidadService estadoTrazabilidadService;

    @GetMapping(value = "/")
    List<EstadoTrazabilidad> getAll(){
        return estadoTrazabilidadService.getAll();
    }

    @GetMapping(value = "/{id}")
    EstadoTrazabilidad get(@PathVariable int id){
        return estadoTrazabilidadService.get(id);
    }

    @PostMapping(value = "/")
    EstadoTrazabilidad register(@RequestBody EstadoTrazabilidad estadoTrazabilidad) throws SQLException{
        return estadoTrazabilidadService.register(estadoTrazabilidad);
    }

    @PutMapping(value = "/")
    EstadoTrazabilidad update(@RequestBody EstadoTrazabilidad estadoTrazabilidad) throws SQLException{
        return estadoTrazabilidadService.update(estadoTrazabilidad);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        estadoTrazabilidadService.delete(id);
    }
}
